package depth.jeonsilog.domain.alarm.constant;

public final class AlarmConstant {
    private AlarmConstant() {
    }

    public static final class FcmAlarmConstant {
        private FcmAlarmConstant() {
        }

        public static final String SILENT_PUSH_BODY = "NotRegistered";
        public static final String TITLE = "title";
        public static final String BODY = "body";
        public static final String TOKEN = "token";
        public static final String DATA = "data";
        public static final String MESSAGE = "message";
        public static final String TYPE = "type";
        public static final String SILENT_CHECK = "silent-check";
    }

    public static final class AlarmCreateConstant {
        private AlarmCreateConstant() {
        }
        public static final String BEFORE_7DAYS = "전시 시작까지 7일 남았어요";
        public static final String BEFORE_3DAYS = "전시 시작까지 3일 남았어요";
        public static final String BEFORE_1DAYS = "전시 시작까지 1일 남았어요";
        public static final String END_7DAYS = "전시 종료까지 7일 남았어요";
        public static final String END_3DAYS = "전시 종료까지 3일 남았어요";
        public static final String END_1DAYS = "전시 종료까지 1일 남았어요";
        public static final String REVIEW_ALARM_SUFFIX_MESSAGE = " 님이 감상평을 남겼어요";
        public static final String RATING_ALARM_SUFFIX_MESSAGE = " 님이 별점을 남겼어요";
        public static final String REPLY_ALARM_SUFFIX_MESSAGE = " 님이 댓글을 남겼어요";
        public static final String FOLLOW_ALARM_SUFFIX_MESSAGE = " 님이 나를 팔로우해요";
    }
}
