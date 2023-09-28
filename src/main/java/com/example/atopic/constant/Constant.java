package com.example.atopic.constant;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class Constant {

    public static final String APP_NAME = "Atopic";

    @NoArgsConstructor(access = PRIVATE)
    public static final class Command {

        public static final String COMMAND_REGISTER = "/register";

        public static final String COMMAND_CALENDAR = "/calendar";
        public static final String COMMAND_TAKE_QUIZ = "/take_quiz";

        public static final String COMMAND_EXPORT_NEW_QUIZZES = "/export_new_quizzes";
        public static final String COMMAND_EXPORT_ALL_QUIZZES = "/export_all_quizzes";
        public static final String COMMAND_EXPORT_STATISTIC_DAY = "/export_statistic_day";

    }

    @NoArgsConstructor(access = PRIVATE)
    public static final class ConfigParams {
        public static final String INPUT_FILE_PATH = "${input.file.path}";
        public static final String INPUT_FILE_PHOTO_PATH = "${input.file.photo.path}";
        public static final String INPUT_FILE_OFFER_PATH = "${input.file.offer.path}";

    }

}
