package com.example.atopic.model.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class QuestKey implements Serializable {

    @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "chatId")
    private User user;

    @Column(name = "calendar_year")
    private Integer calendarYear;

    @Column(name = "calendar_month")
    private Integer calendarMonth;

    @Column(name = "calendar_day")
    private Integer calendarDay;

    @Override
    public String toString() {
        return LocalDate.of(calendarYear, calendarMonth, calendarDay).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestKey questKey = (QuestKey) o;
        return Objects.equals(user, questKey.user) && Objects.equals(calendarYear, questKey.calendarYear) && Objects.equals(calendarMonth, questKey.calendarMonth) && Objects.equals(calendarDay, questKey.calendarDay);
    }


    @Override
    public int hashCode() {
        return Objects.hash(user, calendarYear, calendarMonth, calendarDay);
    }
}
