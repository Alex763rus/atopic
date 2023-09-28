package com.example.atopic.enums.quiz;

public enum Answer {

    ANSWER_BACK("Назад"),

    ANSWER_YES("Да"),
    ANSWER_NO("Нет"),
    ANSWER_VERY_MUCH("Очень сильно"),
    ANSWER_STRONG_ENOUGH("Достаточно сильно"),
    ANSWER_MINOR_WAY("Незначительным образом"),
    ANSWER_STRONGLY("Сильно"),
    ANSWER_SLIGHTLY("Незначительно"),
    ANSWER_NOT_BOTHER("Не беспокоило");

    private String title;

    Answer(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
