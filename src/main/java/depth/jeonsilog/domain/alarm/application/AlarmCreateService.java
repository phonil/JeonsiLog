package depth.jeonsilog.domain.alarm.application;

import depth.jeonsilog.domain.alarm.converter.AlarmConverter;
import depth.jeonsilog.domain.alarm.domain.Alarm;
import depth.jeonsilog.domain.alarm.domain.AlarmType;
import depth.jeonsilog.domain.alarm.domain.repository.AlarmRepository;
import depth.jeonsilog.domain.devide_token.domain.DeviceToken;
import depth.jeonsilog.domain.devide_token.domain.repository.DeviceTokenRepository;
import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.exhibition.domain.OperatingKeyword;
import depth.jeonsilog.domain.follow.domain.Follow;
import depth.jeonsilog.domain.follow.domain.repository.FollowRepository;
import depth.jeonsilog.domain.interest.domain.Interest;
import depth.jeonsilog.domain.interest.domain.repository.InterestRepository;
import depth.jeonsilog.domain.rating.domain.Rating;
import depth.jeonsilog.domain.reply.domain.Reply;
import depth.jeonsilog.domain.review.domain.Review;
import depth.jeonsilog.domain.user.domain.User;
import depth.jeonsilog.infrastructure.fcm.application.FcmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static depth.jeonsilog.domain.alarm.constant.AlarmConstant.AlarmCreateConstant.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmCreateService {

    private final AlarmRepository alarmRepository;
    private final FollowRepository followRepository;
    private final InterestRepository interestRepository;
    private final DeviceTokenRepository deviceTokenRepository;
    private final FcmService fcmService;

    // TODO: 팔로잉한 사람의 new 감상평 -> 알림 생성
    @Transactional
    public void makeReviewAlarm(User sender, Review review) throws IOException {
        List<Follow> follows = followRepository.findAllByFollow(sender);
        String message = sender.getNickname() + REVIEW_ALARM_SUFFIX_MESSAGE;

        for (Follow follow : follows) {
            User receiver = follow.getUser();
            if (!receiver.getIsRecvActive()) continue;
            List<DeviceToken> deviceTokenList = deviceTokenRepository.findAllByUser(receiver);
            if (deviceTokenList.isEmpty()) continue;

            Alarm alarm = Alarm.builder()
                    .user(receiver)
                    .senderId(sender.getId())
                    .alarmType(AlarmType.REVIEW)
                    .targetId(review.getId())
                    .clickId(review.getId())
                    .isChecked(false)
                    .build();
            alarmRepository.save(alarm);

            deviceTokenList.stream()
                    .map(DeviceToken::getDeviceToken)
                    .forEach(token -> fcmService.makeActiveAlarm(token, message));
        }
    }

    // TODO: 팔로잉한 사람의 new 별점 -> 알림 생성
    @Transactional
    public void makeRatingAlarm(User sender, Rating rating) throws IOException {
        List<Follow> follows = followRepository.findAllByFollow(sender);
        String message = sender.getNickname() + RATING_ALARM_SUFFIX_MESSAGE;

        for (Follow follow : follows) {
            User receiver = follow.getUser();
            if (!receiver.getIsRecvActive()) continue;
            List<DeviceToken> deviceTokenList = deviceTokenRepository.findAllByUser(receiver);
            if (deviceTokenList.isEmpty()) continue;

            Alarm alarm = Alarm.builder()
                    .user(receiver)
                    .senderId(sender.getId())
                    .alarmType(AlarmType.RATING)
                    .targetId(rating.getId())
                    .clickId(rating.getExhibition().getId())
                    .isChecked(false)
                    .build();
            alarmRepository.save(alarm);

            deviceTokenList.stream()
                    .map(DeviceToken::getDeviceToken)
                    .forEach(token -> fcmService.makeActiveAlarm(token, message));
        }
    }

    // TODO: 나의 감상평에 달린 댓글 -> 알림 생성
    @Transactional
    public void makeReplyAlarm(User sender, Reply reply) throws IOException {
        User receiver = reply.getReview().getUser();
        if (receiver.equals(sender)) {
            log.info("알림의 sender와 receiver가 동일 인물입니다.");
            return;
        }
        if (!receiver.getIsRecvActive()) return;
        List<DeviceToken> deviceTokenList = deviceTokenRepository.findAllByUser(receiver);
        if (deviceTokenList.isEmpty()) return;
        String message = sender.getNickname() + REPLY_ALARM_SUFFIX_MESSAGE;

        Alarm alarm = Alarm.builder()
                .user(receiver)
                .senderId(sender.getId())
                .alarmType(AlarmType.REPLY)
                .targetId(reply.getId())
                .clickId(reply.getReview().getId())
                .isChecked(false)
                .build();
        alarmRepository.save(alarm);

        deviceTokenList.stream()
                .map(DeviceToken::getDeviceToken)
                .forEach(token -> fcmService.makeActiveAlarm(token, message));
    }

    // TODO: 나를 팔로우 -> 알림 생성
    @Transactional
    public void makeFollowAlarm(User sender, Follow follow) throws IOException {
        User receiver = follow.getFollow();
        if (!receiver.getIsRecvActive()) return;
        List<DeviceToken> deviceTokenList = deviceTokenRepository.findAllByUser(receiver);
        if (deviceTokenList.isEmpty()) return;
        String message = sender.getNickname() + FOLLOW_ALARM_SUFFIX_MESSAGE;

        Alarm alarm = Alarm.builder()
                .user(receiver)
                .senderId(sender.getId())
                .alarmType(AlarmType.FOLLOW)
                .targetId(follow.getId())
                .clickId(follow.getUser().getId())
                .isChecked(false)
                .build();
        alarmRepository.save(alarm);

        deviceTokenList.stream()
                .map(DeviceToken::getDeviceToken)
                .forEach(token -> fcmService.makeActiveAlarm(token, message));
    }

    // TODO: 관심 전시회 시작 전 -> 알림 생성
    @Transactional
    @Scheduled(cron = "0 0 9 * * *") // 오전 9시에 실행
    public void makeExhibitionAlarm() {
        List<Interest> interests = interestRepository.findByExhibition_OperatingKeyword(OperatingKeyword.BEFORE_DISPLAY);
        LocalDate currentDate = LocalDate.now();

        for (Interest interest : interests) {
            Exhibition exhibition = interest.getExhibition();
            User receiver = interest.getUser();

            if (!receiver.getIsRecvActive()) continue;
            List<DeviceToken> deviceTokenList = deviceTokenRepository.findAllByUser(receiver);
            if (deviceTokenList.isEmpty()) continue;

            LocalDate startDate = LocalDate.parse(exhibition.getStartDate(), DateTimeFormatter.ofPattern("yyyyMMdd"));
            long daysDifference = ChronoUnit.DAYS.between(currentDate, startDate);
            if (daysDifference != 7 && daysDifference != 3 && daysDifference != 1) continue;
            if (checkDuplicateAlarm(receiver.getId(), exhibition.getId(), getBeforeAlarmType(daysDifference))) continue;

            Alarm alarm = AlarmConverter.toExhibitionAlarm(interest, getBeforeAlarmType(daysDifference));
            alarmRepository.save(alarm);

            deviceTokenList.stream()
                    .map(DeviceToken::getDeviceToken)
                    .forEach(token -> fcmService.makeExhibitionAlarm(token, exhibition.getName(), alarm.getContents()));
        }
    }

    // TODO: 관심 전시회 중 별점, 리뷰 남기지 않은 전시회에 한해 종료 임박 알림 생성
    @Transactional
    @Scheduled(cron = "0 0 9 * * *") // 오전 9시에 실행
    public void makeExhibitionEndAlarm() {
        List<Interest> interests = interestRepository.findInterestsByUserAndExhibitionWithoutRatingAndReview();
        LocalDate currentDate = LocalDate.now();

        for (Interest interest : interests) {
            User receiver = interest.getUser();
            Exhibition exhibition = interest.getExhibition();

            if (!receiver.getIsRecvActive()) continue;
            List<DeviceToken> deviceTokenList = deviceTokenRepository.findAllByUser(receiver);
            if (deviceTokenList.isEmpty()) continue;

            LocalDate endDate = LocalDate.parse(exhibition.getEndDate(), DateTimeFormatter.ofPattern("yyyyMMdd"));
            long daysDifference = ChronoUnit.DAYS.between(currentDate, endDate);
            if (daysDifference != 7 && daysDifference != 3 && daysDifference != 1) continue;
            if (checkDuplicateAlarm(receiver.getId(), exhibition.getId(), getEndAlarmType(daysDifference))) continue;

            Alarm alarm = AlarmConverter.toExhibitionAlarm(interest, getEndAlarmType(daysDifference));
            alarmRepository.save(alarm);

            deviceTokenList.stream()
                    .map(DeviceToken::getDeviceToken)
                    .forEach(token -> fcmService.makeExhibitionAlarm(token, exhibition.getName(), alarm.getContents()));
        }
    }

    public boolean checkDuplicateAlarm(Long userId, Long targetId, String contents) {
        return alarmRepository.existsByUserIdAndTargetIdAndContents(userId, targetId, contents);
    }

    private String getBeforeAlarmType(long daysDifference) {
        return switch ((int) daysDifference) {
            case 7 -> BEFORE_7DAYS;
            case 3 -> BEFORE_3DAYS;
            case 1 -> BEFORE_1DAYS;
            default -> "";
        };
    }

    private String getEndAlarmType(long daysDifference) {
        return switch ((int) daysDifference) {
            case 7 -> END_7DAYS;
            case 3 -> END_3DAYS;
            case 1 -> END_1DAYS;
            default -> "";
        };
    }
}
