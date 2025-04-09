package depth.jeonsilog.domain.alarm.constant;

public final class AlarmConstant {
    private AlarmConstant() {
    }

    public static final class AlarmCreateConstant {
        private AlarmCreateConstant() {
        }
        public static String BEFORE_7DAYS = "전시 시작까지 7일 남았어요";
        public static String BEFORE_3DAYS = "전시 시작까지 3일 남았어요";
        public static String BEFORE_1DAYS = "전시 시작까지 1일 남았어요";
        public static String END_7DAYS = "전시 종료까지 7일 남았어요";
        public static String END_3DAYS = "전시 종료까지 3일 남았어요";
        public static String END_1DAYS = "전시 종료까지 1일 남았어요";
        public static String REVIEW_ALARM_SUFFIX_MESSAGE = " 님이 감상평을 남겼어요";
        public static String RATING_ALARM_SUFFIX_MESSAGE = " 님이 별점을 남겼어요";
        public static String REPLY_ALARM_SUFFIX_MESSAGE = " 님이 댓글을 남겼어요";
        public static String FOLLOW_ALARM_SUFFIX_MESSAGE = " 님이 나를 팔로우해요";
    }
}
