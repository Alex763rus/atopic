package com.example.atopic.config;

import com.example.atopic.enums.UserRole;
import lombok.Getter;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.atopic.constant.Constant.Command.*;
import static com.example.atopic.constant.Constant.ConfigParams.*;
import static com.example.atopic.enums.UserRole.*;
import static org.example.tgcommons.constant.Constant.Command.COMMAND_DEFAULT;
import static org.example.tgcommons.constant.Constant.Command.COMMAND_START;
import static org.example.tgcommons.constant.Constant.ConfigParams.*;

@Configuration
@Getter
@PropertySource(PROPERTY_SOURCE)
public class BotConfig {

    @Value(BOT_VERSION)
    String botVersion;

    @Value(BOT_USERNAME)
    String botUserName;

    @Value(BOT_TOKEN)
    String botToken;

    @Bean
    public Map<UserRole, List<String>> roleAccess() {
        val roleAccess = new HashMap<UserRole, List<String>>();
        roleAccess.put(BLOCKED, List.of(COMMAND_DEFAULT, COMMAND_START));
        roleAccess.put(UNREGISTERED, List.of(COMMAND_DEFAULT, COMMAND_START, COMMAND_REGISTER));
        roleAccess.put(EMPLOYEE, List.of(COMMAND_DEFAULT, COMMAND_START, COMMAND_CALENDAR, COMMAND_TAKE_QUIZ));
        roleAccess.put(ADMIN, List.of(COMMAND_DEFAULT, COMMAND_START, COMMAND_CALENDAR, COMMAND_TAKE_QUIZ, COMMAND_EXPORT_NEW_QUIZZES, COMMAND_EXPORT_ALL_QUIZZES, COMMAND_EXPORT_STATISTIC_DAY));
        return roleAccess;
    }

}
