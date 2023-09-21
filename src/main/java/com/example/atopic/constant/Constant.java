package com.example.atopic.constant;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class Constant {
    @NoArgsConstructor(access = PRIVATE)
    public static final class Command {

        public static final String COMMAND_REGISTER = "/register";

        public static final String COMMAND_CALENDAR = "/calendar";
        public static final String COMMAND_QUEST = "/quest";

        public static final String COMMAND_EXPORT_NEW_LEADS = "/exportnewquests";
        public static final String COMMAND_EXPORT_ALL_QUEST = "/exportall";

    }

    @NoArgsConstructor(access = PRIVATE)
    public static final class ConfigParams {
        public static final String INPUT_FILE_PATH = "${input.file.path}";
        public static final String INPUT_FILE_PHOTO_PATH = "${input.file.photo.path}";
        public static final String INPUT_FILE_OFFER_PATH = "${input.file.offer.path}";

    }

    public static final String APP_NAME = "Atopic";

}
