package com.example.atopic.exception;

import lombok.val;

import static org.example.tgcommons.constant.Constant.TextConstants.NEW_LINE;

public class InputLongException extends RuntimeException {
    public InputLongException(String message, int maxValue) {
        super(getCheckPhoneErrorMessageText(message, maxValue));
    }

    public InputLongException(String message, int maxRank, long minValue, long maxValue) {
        super(getCheckPhoneErrorMessageText(message, maxRank, minValue, maxValue));
    }

    private static String getCheckPhoneErrorMessageText(String message, int maxValue) {
        val errorText = new StringBuilder();
        errorText.append("Введено некорректное значение: ").append(message).append(NEW_LINE)
                .append("Требования к искомому значению:").append(NEW_LINE)
                .append("- введенное значение должно быть числом").append(NEW_LINE)
                .append("- количество символов в числе должно быть не больше ").append(maxValue).append(NEW_LINE).append(NEW_LINE)
                .append("Повторите ввод:");
        return errorText.toString();
    }

    private static String getCheckPhoneErrorMessageText(String message, int maxRank, long minValue, long maxValue) {
        val errorText = new StringBuilder();
        errorText.append("Введено некорректное значение: ").append(message).append(NEW_LINE)
                .append("Требования к искомому значению:").append(NEW_LINE)
                .append("- введенное значение должно быть числом").append(NEW_LINE)
                .append("- количество символов в числе должно быть не больше ").append(maxRank).append(NEW_LINE).append(NEW_LINE)
                .append("- число должно быть больше ").append(minValue).append(NEW_LINE).append(NEW_LINE)
                .append("- число должно быть меньше ").append(maxValue).append(NEW_LINE).append(NEW_LINE)
                .append("Повторите ввод:");
        return errorText.toString();
    }
}
