package com.example.atopic.model.menu.admin.export;

import com.example.atopic.model.jpa.User;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.example.atopic.constant.Constant.Command.COMMAND_EXPORT_ALL_QUIZZES;

@Component(COMMAND_EXPORT_ALL_QUIZZES)
@Slf4j
public class MenuExportAllQuiz extends MenuExportQuizBase {

    @Override
    public String getMenuComand() {
        return COMMAND_EXPORT_ALL_QUIZZES;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        try {
            return switch (stateService.getState(user)) {
                case FREE -> exportAllLeadsToExcel(user, update);
                default -> createErrorDefaultMessage(user);
            };
        } catch (Exception ex) {
            log.error(ex.toString());
            return createMessageList(user, ex.toString());
        }
    }

    private List<PartialBotApiMethod> exportAllLeadsToExcel(User user, Update update) {
        val userList = quizService.getAll();
        return exportToExcel(user, userList);
    }

    @Override
    public String getDescription() {
        return "Выгрузка всех опросов";
    }
}
