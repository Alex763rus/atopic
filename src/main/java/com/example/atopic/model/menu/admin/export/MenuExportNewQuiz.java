package com.example.atopic.model.menu.admin.export;

import com.example.atopic.model.jpa.User;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.example.atopic.constant.Constant.Command.COMMAND_EXPORT_NEW_QUIZZES;
import static com.example.atopic.enums.QuizExportStatus.EXPORTED_QUEST;
import static com.example.atopic.enums.QuizExportStatus.NEW_QUEST;

@Component(COMMAND_EXPORT_NEW_QUIZZES)
@Slf4j
public class MenuExportNewQuiz extends MenuExportQuizBase {

    @Override
    public String getMenuComand() {
        return COMMAND_EXPORT_NEW_QUIZZES;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        try {
            return switch (stateService.getState(user)) {
                case FREE -> exportNewLeadsToExcel(user, update);
                default -> createErrorDefaultMessage(user);
            };
        } catch (Exception ex) {
            log.error(ex.toString());
            return createMessageList(user, ex.toString());
        }
    }

    private List<PartialBotApiMethod> exportNewLeadsToExcel(User user, Update update) {
        val questList = quizService.getAll(NEW_QUEST);
        val answer = exportToExcel(user, questList);
        questList.forEach(e -> e.setQuizExportStatus(EXPORTED_QUEST));
        quizService.save(questList);
        return answer;
    }

    @Override
    public String getDescription() {
        return "Новые опросы";
    }
}
