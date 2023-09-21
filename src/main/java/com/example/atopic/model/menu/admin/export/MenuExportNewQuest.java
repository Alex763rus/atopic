package com.example.atopic.model.menu.admin.export;

import com.example.atopic.model.jpa.User;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.example.atopic.constant.Constant.Command.COMMAND_EXPORT_NEW_LEADS;
import static com.example.atopic.enums.QuestExportStatus.EXPORTED_QUEST;
import static com.example.atopic.enums.QuestExportStatus.NEW_QUEST;

@Component(COMMAND_EXPORT_NEW_LEADS)
@Slf4j
public class MenuExportNewQuest extends MenuExportQuestBase {

    @Override
    public String getMenuComand() {
        return COMMAND_EXPORT_NEW_LEADS;
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
        val questList = questService.getAll(NEW_QUEST);
        val answer = exportToExcel(user, questList);
        questList.forEach(e -> e.setQuestExportStatus(EXPORTED_QUEST));
        questService.save(questList);
        return answer;
    }

    @Override
    public String getDescription() {
        return "Новые опросы";
    }
}
