package depth.jeonsilog.domain.follow.application;

import depth.jeonsilog.domain.alarm.application.AlarmCreateService;
import depth.jeonsilog.domain.follow.converter.FollowConverter;
import depth.jeonsilog.domain.follow.domain.Follow;
import depth.jeonsilog.domain.follow.domain.repository.FollowRepository;
import depth.jeonsilog.domain.follow.dto.FollowResponseDto;
import depth.jeonsilog.domain.user.application.UserService;
import depth.jeonsilog.domain.user.domain.User;
import depth.jeonsilog.global.DefaultAssert;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import depth.jeonsilog.global.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;
    private final AlarmCreateService alarmService;

    @Transactional
    public void follow(UserPrincipal userPrincipal, Long userId) throws IOException {
        User user = userService.validateUserByToken(userPrincipal);
        User followUser = userService.validateUserById(userId);
        DefaultAssert.isTrue((!Objects.equals(user, followUser)), "나를 팔로우 할 수 없습니다.");

        List<Follow> followings = followRepository.findAllByUser(user);
        for (Follow follow : followings) {
            DefaultAssert.isTrue(!(Objects.equals(follow.getFollow(), followUser)), "이미 팔로우 한 유저입니다.");
        }

        Follow follow = FollowConverter.toFollow(user, followUser);
        followRepository.save(follow);
        alarmService.makeFollowAlarm(user, follow);
    }

    @Transactional
    public void deleteFollowing(UserPrincipal userPrincipal, Long userId) {
        User findUser = userService.validateUserByToken(userPrincipal);
        User followUser = userService.validateUserById(userId);
        DefaultAssert.isTrue((!Objects.equals(findUser, followUser)), "나를 팔로우 취소할 수 없습니다.");

        Optional<Follow> findFollow = followRepository.findByUserAndFollow(findUser, followUser);
        DefaultAssert.isTrue(findFollow.isPresent(), "유저를 팔로우하고있지 않습니다.");
        followRepository.delete(findFollow.get());
    }

    @Transactional
    public void deleteFollower(UserPrincipal userPrincipal, Long userId) {
        User findUser = userService.validateUserByToken(userPrincipal);
        User followUser = userService.validateUserById(userId);
        DefaultAssert.isTrue((!Objects.equals(findUser, followUser)), "나를 팔로워 취소할 수 없습니다.");

        Optional<Follow> findFollow = followRepository.findByUserAndFollow(followUser, findUser);
        DefaultAssert.isTrue(findFollow.isPresent(), "해당 유저가 나를 팔로우하지 않습니다.");
        followRepository.delete(findFollow.get());
    }

    public ResponseEntity<?> findMyFollowingList(UserPrincipal userPrincipal) {
        User findUser = userService.validateUserByToken(userPrincipal);
        List<Follow> followList = followRepository.findAllByUser(findUser);
        DefaultAssert.isTrue(!followList.isEmpty(), "팔로잉 유저가 존재하지 않습니다.");

        List<FollowResponseDto.MyFollowingListRes> followListRes = FollowConverter.toMyFollowingListRes(followList);
        ApiResponse apiResponse = ApiResponse.toApiResponse(followListRes);
        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> findMyFollowerList(UserPrincipal userPrincipal) {
        User findUser = userService.validateUserByToken(userPrincipal);
        List<Follow> followList = followRepository.findAllByFollow(findUser);
        DefaultAssert.isTrue(!followList.isEmpty(), "팔로워가 존재하지 않습니다.");

        List<FollowResponseDto.MyFollowerListRes> followingListRes = FollowConverter.toMyFollowerListRes(followList, followRepository);
        ApiResponse apiResponse = ApiResponse.toApiResponse(followingListRes);
        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> findUserFollowingList(UserPrincipal userPrincipal, Long userId) {
        User me = userService.validateUserByToken(userPrincipal);
        User findUser = userService.validateUserById(userId);
        List<Follow> followList = followRepository.findAllByUser(findUser);
        DefaultAssert.isTrue(!followList.isEmpty(), "팔로잉 유저가 존재하지 않습니다.");

        List<FollowResponseDto.UserFollowingListRes> followListRes = FollowConverter.toUserFollowingListRes(me, followList, followRepository);
        ApiResponse apiResponse = ApiResponse.toApiResponse(followListRes);
        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> findUserFollowerList(UserPrincipal userPrincipal, Long userId) {
        User me = userService.validateUserByToken(userPrincipal);
        User findUser = userService.validateUserById(userId);
        List<Follow> followList = followRepository.findAllByFollow(findUser);
        DefaultAssert.isTrue(!followList.isEmpty(), "팔로워가 존재하지 않습니다.");

        List<FollowResponseDto.UserFollowerListRes> followingListRes = FollowConverter.toUserFollowerListRes(me, followList, followRepository);
        ApiResponse apiResponse = ApiResponse.toApiResponse(followingListRes);
        return ResponseEntity.ok(apiResponse);
    }
}
