package com.example.atopic.model.jpa;

import com.example.atopic.enums.QuestExportStatus;
import com.example.atopic.enums.quest.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity(name = "quest")
public class Quest {

    @EmbeddedId
    private QuestKey questKey;

    @Column(name = "export_status")
    private QuestExportStatus questExportStatus;

    @Column(name = "answer1")
    private Answer answer1;

    @Column(name = "answer2")
    private Answer answer2;

    @Column(name = "answer3")
    private Answer answer3;

    @Column(name = "answer4")
    private Answer answer4;

    @Column(name = "answer5")
    private Answer answer5;

    @Column(name = "answer6")
    private Answer answer6;

    @Column(name = "answer7")
    private Answer answer7;

    @Column(name = "answer8")
    private Answer answer8;

    @Column(name = "answer9")
    private Answer answer9;

    @Column(name = "answer10")
    private Answer answer10;

    @Column(name = "answer11")
    private Answer answer11;

    @Column(name = "quest_date")
    private Timestamp questDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quest quest = (Quest) o;
        return Objects.equals(questKey, quest.questKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questKey);
    }
}
