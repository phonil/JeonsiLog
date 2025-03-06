package depth.jeonsilog.domain.exhibition.application;

import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.exhibition.domain.repository.ExhibitionRepository;
import depth.jeonsilog.domain.exhibition.dto.ExhibitionRequestDto;
import depth.jeonsilog.domain.place.domain.Place;
import depth.jeonsilog.domain.user.application.UserService;
import depth.jeonsilog.domain.user.domain.Role;
import depth.jeonsilog.domain.user.domain.User;
import depth.jeonsilog.global.DefaultAssert;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import depth.jeonsilog.infrastructure.s3.application.FileUploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Slf4j
public class AdminExhibitionService {

    private final ExhibitionRepository exhibitionRepository;

    private final FileUploader fileUploader;
    private final ExhibitionService exhibitionService;
    private final UserService userService;

    private static final String DIRNAME = "exhibition_img";

    @Transactional
    public void updateExhibitionDetail(UserPrincipal userPrincipal, ExhibitionRequestDto.UpdateExhibitionDetailReq updateExhibitionDetailReq, MultipartFile img) throws IOException {
        User user = userService.validateUserByToken(userPrincipal);
        DefaultAssert.isTrue(user.getRole().equals(Role.ADMIN), "관리자만 수정할 수 있습니다.");
        Exhibition exhibition = exhibitionService.validateExhibitionById(updateExhibitionDetailReq.getExhibitionId());

        String storedFileName = null;
        if (updateExhibitionDetailReq.getIsImageChange()) { // 이미지를 변경하는 경우
            if (img != null)  // 이미지 삭제가 아닌 이미지를 변경하거나 추가하는 경우
                storedFileName = fileUploader.multipartFileUpload(img, DIRNAME);
            fileUploader.deleteFile(exhibition.getImageUrl(), DIRNAME);
        }

        exhibition.updateExhibitionDetail(updateExhibitionDetailReq, storedFileName);
        Place place = exhibition.getPlace();
        place.updatePlaceWithExhibitionDetail(updateExhibitionDetailReq.getUpdatePlaceInfo());
    }

    @Transactional
    public void updateExhibitionSequence(UserPrincipal userPrincipal, ExhibitionRequestDto.UpdateExhibitionSequenceList updateSequenceReq) {
        User findUser = userService.validateUserByToken(userPrincipal);
        DefaultAssert.isTrue(findUser.getRole() == Role.ADMIN, "관리자만 전시회 순서를 변경할 수 있습니다.");

        List<ExhibitionRequestDto.UpdateExhibitionSequence> updateExhibitionSequenceList = updateSequenceReq.getUpdateSequenceInfo();
        int size = updateExhibitionSequenceList.size();
        for (int i = 0; i < size; i++) {
            ExhibitionRequestDto.UpdateExhibitionSequence updateExhibitionSequence = updateExhibitionSequenceList.get(i);
            Optional<Exhibition> exhibition = exhibitionRepository.findById(updateExhibitionSequence.getExhibitionId());
            DefaultAssert.isTrue(exhibition.isPresent(), "전시회 정보가 올바르지 않습니다.");

            Optional<Exhibition> duplicatedExhibition = exhibitionRepository.findBySequence(updateExhibitionSequence.getSequence());
            duplicatedExhibition.ifPresent(value -> value.updateSequence(11));  // 해당 sequence를 가지고 있던 기존의 전시회는 11번으로 순서를 변경한다.

            Exhibition findExhibition = exhibition.get();
            findExhibition.updateSequence(updateExhibitionSequence.getSequence());
        }
        // sequence 삭제 로직
        for (int i = size; i < 10 ;i++) {
            Optional<Exhibition> tempExhibition = exhibitionRepository.findBySequence(i + 1);
            if (tempExhibition.isPresent()) {
                tempExhibition.get().updateSequence(11);
            } else {
                break;
            }
        }
    }
}
