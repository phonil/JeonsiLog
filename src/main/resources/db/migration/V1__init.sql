CREATE TABLE IF NOT EXISTS token (
                                     user_email          VARCHAR(255)                                    NOT NULL,
                                     refresh_token       TEXT                                            NOT NULL,
                                     created_date        DATETIME(6)                                     NOT NULL,
                                     modified_date       DATETIME(6)                                     NOT NULL,
                                     status              ENUM ('DELETE','ACTIVE')                        NOT NULL,
                                     CONSTRAINT pk_token PRIMARY KEY (user_email)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS `user` (
                                      user_id             BIGINT AUTO_INCREMENT                           NOT NULL,
                                      password            VARCHAR(255)                                    NOT NULL,
                                      nickname            VARCHAR(255)                                    NOT NULL,
                                      email               VARCHAR(255)                                    NOT NULL,
                                      profile_img         VARCHAR(255)                                    NULL,
                                      is_open             BOOLEAN                                         NOT NULL,
                                      is_recv_exhibition  BOOLEAN                                         NOT NULL,
                                      is_recv_active      BOOLEAN                                         NOT NULL,
                                      role                ENUM('ADMIN','USER')                            NOT NULL,
                                      provider            ENUM('LOCAL','KAKAO','NAVER','GOOGLE')          NOT NULL,
                                      provider_id         VARCHAR(255)                                    NOT NULL,
                                      fcm_token           VARCHAR(255)                                    NULL,
                                      user_level          ENUM('NON', 'DONE', 'BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'MASTER') NOT NULL,
                                      created_date        DATETIME(6)                                     NOT NULL,
                                      modified_date       DATETIME(6)                                     NOT NULL,
                                      status              ENUM('DELETE','ACTIVE')                         NOT NULL,
                                      CONSTRAINT pk_user PRIMARY KEY (user_id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS device_token (
                                      id                  BIGINT AUTO_INCREMENT                           NOT NULL,
                                      user_id             BIGINT                                          NOT NULL,
                                      device_token        VARCHAR(255)                                    NOT NULL,
                                      created_date        DATETIME(6)                                     NOT NULL,
                                      modified_date       DATETIME(6)                                     NOT NULL,
                                      status              ENUM('DELETE','ACTIVE')                         NOT NULL,
                                      CONSTRAINT pk_device_token PRIMARY KEY (id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS place (
                                     id                  BIGINT AUTO_INCREMENT                           NOT NULL,
                                     seq                 INT                                             NULL,
                                     name                VARCHAR(255)                                    NULL,
                                     address             VARCHAR(255)                                    NULL,
                                     tel                 VARCHAR(255)                                    NULL,
                                     home_page           VARCHAR(255)                                    NULL,
                                     created_date        DATETIME(6)                                     NOT NULL,
                                     modified_date       DATETIME(6)                                     NOT NULL,
                                     status              ENUM('DELETE','ACTIVE')                         NOT NULL,
                                     CONSTRAINT pk_place PRIMARY KEY (id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS alarm (
                                     id                  BIGINT AUTO_INCREMENT                           NOT NULL,
                                     user_id             BIGINT                                          NOT NULL,
                                     sender_id           BIGINT                                          NULL,
                                     target_id           BIGINT                                          NOT NULL,
                                     click_id            BIGINT                                          NOT NULL,
                                     contents            VARCHAR(255)                                    NULL,
                                     is_checked          BOOLEAN                                         NOT NULL,
                                     alarm_type          ENUM('RATING','REVIEW','REPLY','FOLLOW','EXHIBITION') NOT NULL,
                                     created_date        DATETIME(6)                                     NOT NULL,
                                     modified_date       DATETIME(6)                                     NOT NULL,
                                     status              ENUM('DELETE','ACTIVE')                         NOT NULL,
                                     CONSTRAINT pk_alarm PRIMARY KEY (id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS calendar (
                                        id                  BIGINT AUTO_INCREMENT                           NOT NULL,
                                        user_id             BIGINT                                          NOT NULL,
                                        photo_date          DATE                                            NOT NULL,
                                        image_url           VARCHAR(2048)                                   NULL,
                                        caption             VARCHAR(255)                                    NULL,
                                        created_date        DATETIME(6)                                     NOT NULL,
                                        modified_date       DATETIME(6)                                     NOT NULL,
                                        status              ENUM('DELETE','ACTIVE')                         NOT NULL,
                                        CONSTRAINT pk_calendar PRIMARY KEY (id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS follow (
                                      id                  BIGINT AUTO_INCREMENT                           NOT NULL,
                                      user_id             BIGINT                                          NOT NULL,
                                      follow_id           BIGINT                                          NOT NULL,
                                      created_date        DATETIME(6)                                     NOT NULL,
                                      modified_date       DATETIME(6)                                     NOT NULL,
                                      status              ENUM('DELETE','ACTIVE')                         NOT NULL,
                                      CONSTRAINT pk_follow PRIMARY KEY (id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS `stop` (
                                      id                  BIGINT AUTO_INCREMENT                           NOT NULL,
                                      user_id             BIGINT                                          NOT NULL,
                                      reason              VARCHAR(255)                                    NOT NULL,
                                      is_first_access     BOOLEAN                                         NOT NULL,
                                      created_date        DATETIME(6)                                     NOT NULL,
                                      modified_date       DATETIME(6)                                     NOT NULL,
                                      status              ENUM('DELETE','ACTIVE')                         NOT NULL,
                                      CONSTRAINT pk_stop PRIMARY KEY (id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS report (
                                      id                  BIGINT AUTO_INCREMENT                           NOT NULL,
                                      user_id             BIGINT                                          NOT NULL,
                                      reported_id         BIGINT                                          NOT NULL,
                                      counting            INT                                             NOT NULL,
                                      is_checked          BOOLEAN                                         NOT NULL,
                                      report_type         ENUM('EXHIBITION','REVIEW','REPLY','ADDRESS','PHONE_NUMBER','LINK') NOT NULL,
                                      created_date        DATETIME(6)                                     NOT NULL,
                                      modified_date       DATETIME(6)                                     NOT NULL,
                                      status              ENUM('DELETE','ACTIVE')                         NOT NULL,
                                      CONSTRAINT pk_report PRIMARY KEY (id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS exhibition (
                                          id                  BIGINT AUTO_INCREMENT                           NOT NULL,
                                          place_id            BIGINT                                          NULL,
                                          name                VARCHAR(255)                                    NOT NULL,
                                          image_url           VARCHAR(255)                                    NULL,
                                          operating_keyword   ENUM('ON_DISPLAY','BEFORE_DISPLAY','AFTER_DISPLAY') NULL,
                                          price_keyword       ENUM('FREE','PAY')                              NULL,
                                          price               VARCHAR(255)                                    NULL,
                                          start_date          VARCHAR(255)                                    NULL,
                                          end_date            VARCHAR(255)                                    NULL,
                                          information         TEXT                                            NULL,
                                          rate                DOUBLE                                          NULL,
                                          exhibition_seq      INT                                             NOT NULL,
                                          sequence            INT                                             NOT NULL,
                                          created_date        DATETIME(6)                                     NOT NULL,
                                          modified_date       DATETIME(6)                                     NOT NULL,
                                          status              ENUM('DELETE','ACTIVE')                         NOT NULL,
                                          CONSTRAINT pk_exhibition PRIMARY KEY (id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS interest (
                                        id                  BIGINT AUTO_INCREMENT                           NOT NULL,
                                        user_id             BIGINT                                          NOT NULL,
                                        exhibition_id       BIGINT                                          NOT NULL,
                                        created_date        DATETIME(6)                                     NOT NULL,
                                        modified_date       DATETIME(6)                                     NOT NULL,
                                        status              ENUM('DELETE','ACTIVE')                         NOT NULL,
                                        CONSTRAINT pk_interest PRIMARY KEY (id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS rating (
                                      id                  BIGINT AUTO_INCREMENT                           NOT NULL,
                                      user_id             BIGINT                                          NOT NULL,
                                      exhibition_id       BIGINT                                          NOT NULL,
                                      rate                DOUBLE                                          NOT NULL,
                                      created_date        DATETIME(6)                                     NOT NULL,
                                      modified_date       DATETIME(6)                                     NOT NULL,
                                      status              ENUM('DELETE','ACTIVE')                         NOT NULL,
                                      CONSTRAINT pk_rating PRIMARY KEY (id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS review (
                                      id                  BIGINT AUTO_INCREMENT                           NOT NULL,
                                      user_id             BIGINT                                          NOT NULL,
                                      exhibition_id       BIGINT                                          NOT NULL,
                                      contents            TEXT                                            NOT NULL,
                                      num_reply           INT                                             NOT NULL,
                                      created_date        DATETIME(6)                                     NOT NULL,
                                      modified_date       DATETIME(6)                                     NOT NULL,
                                      status              ENUM('DELETE','ACTIVE')                         NOT NULL,
                                      CONSTRAINT pk_review PRIMARY KEY (id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS reply (
                                     id                  BIGINT AUTO_INCREMENT                           NOT NULL,
                                     user_id             BIGINT                                          NOT NULL,
                                     review_id           BIGINT                                          NOT NULL,
                                     contents            TEXT                                            NOT NULL,
                                     created_date        DATETIME(6)                                     NOT NULL,
                                     modified_date       DATETIME(6)                                     NOT NULL,
                                     status              ENUM('DELETE','ACTIVE')                         NOT NULL,
                                     CONSTRAINT pk_reply PRIMARY KEY (id)
) engine=InnoDB;

ALTER TABLE `user`
    ADD CONSTRAINT uk_user_nickname UNIQUE (nickname);

ALTER TABLE device_token
    ADD CONSTRAINT uk_device_token UNIQUE (device_token);

ALTER TABLE place
    ADD CONSTRAINT uk_place_seq UNIQUE (seq);

ALTER TABLE exhibition
    ADD CONSTRAINT uk_exhibition_seq UNIQUE (exhibition_seq);

ALTER TABLE interest
    ADD CONSTRAINT uk_interest UNIQUE (user_id, exhibition_id);

ALTER TABLE rating
    ADD CONSTRAINT uk_rating UNIQUE (user_id, exhibition_id);

ALTER TABLE alarm
    ADD CONSTRAINT fk_alarm_to_user
        FOREIGN KEY (user_id)
            REFERENCES `user` (user_id) ON DELETE CASCADE;

ALTER TABLE calendar
    ADD CONSTRAINT fk_calendar_to_user
        FOREIGN KEY (user_id)
            REFERENCES `user` (user_id) ON DELETE CASCADE;

ALTER TABLE follow
    ADD CONSTRAINT fk_follow_to_user_self
        FOREIGN KEY (user_id)
            REFERENCES `user` (user_id) ON DELETE CASCADE;

ALTER TABLE follow
    ADD CONSTRAINT fk_follow_to_user_others
        FOREIGN KEY (follow_id)
            REFERENCES `user` (user_id) ON DELETE CASCADE;

ALTER TABLE `stop`
    ADD CONSTRAINT fk_stop_to_user
        FOREIGN KEY (user_id)
            REFERENCES `user` (user_id);

ALTER TABLE report
    ADD CONSTRAINT fk_report_to_user
        FOREIGN KEY (user_id)
            REFERENCES `user` (user_id) ON DELETE CASCADE;

ALTER TABLE exhibition
    ADD CONSTRAINT fk_exhibition_to_place
        FOREIGN KEY (place_id)
            REFERENCES place (id);

ALTER TABLE interest
    ADD CONSTRAINT fk_interest_to_user
        FOREIGN KEY (user_id)
            REFERENCES `user` (user_id);

ALTER TABLE interest
    ADD CONSTRAINT fk_interest_to_exhibition
        FOREIGN KEY (exhibition_id)
            REFERENCES exhibition (id);

ALTER TABLE rating
    ADD CONSTRAINT fk_rating_to_user
        FOREIGN KEY (user_id)
            REFERENCES `user` (user_id);

ALTER TABLE rating
    ADD CONSTRAINT fk_rating_to_exhibition
        FOREIGN KEY (exhibition_id)
            REFERENCES exhibition (id);

ALTER TABLE review
    ADD CONSTRAINT fk_review_to_user
        FOREIGN KEY (user_id)
            REFERENCES `user` (user_id) ON DELETE CASCADE;

ALTER TABLE review
    ADD CONSTRAINT fk_review_to_exhibition
        FOREIGN KEY (exhibition_id)
            REFERENCES exhibition (id);

ALTER TABLE reply
    ADD CONSTRAINT fk_reply_to_user
        FOREIGN KEY (user_id)
            REFERENCES `user` (user_id);

ALTER TABLE reply
    ADD CONSTRAINT fk_reply_to_review
        FOREIGN KEY (review_id)
            REFERENCES review (id);

ALTER TABLE device_token
    ADD CONSTRAINT fk_device_token_to_user
        FOREIGN KEY (user_id)
            REFERENCES `user` (user_id);