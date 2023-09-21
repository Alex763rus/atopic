package com.example.atopic.model.jpa;

import com.example.atopic.enums.QuestExportStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuestRepository extends CrudRepository<Quest, Long> {

    @Override
    public List<Quest> findAll();

    List<Quest> findQuestByQuestExportStatus(QuestExportStatus questExportStatus);

    public Quest findByQuestKey(QuestKey key);
}
