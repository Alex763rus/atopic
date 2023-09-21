package com.example.atopic.enums;

public enum State {

    FREE,

    FAQ_WAIT_QUESTION,

    REGISTER_WAIT_SURNAME, REGISTER_WAIT_NAME, REGISTER_WAIT_AGE,

    CALENDAR_WAIT_CHOOSE_DATE, CALENDAR_HAVE_DATE,

    WAIT_QUEST,
//    QUEST_WAIT_1, QUEST_WAIT_2, QUEST_WAIT_3, QUEST_WAIT_4, QUEST_WAIT_5, QUEST_WAIT_6,
//    QUEST_WAIT_7, QUEST_WAIT_8, QUEST_WAIT_9, QUEST_WAIT_10, QUEST_WAIT_11,

    CALC_WAIT_TYPE,
    CALC_WAIT_FORM, CALC_WAIT_MODE, CALC_WAIT_EMPLOYEE, CALC_WAIT_MONEY_TURNOVER, CALC_WAIT_OPERATION,
    CALC_WAIT_NDS_AGENT, CALC_WAIT_NDFL_AGENT, CALC_WAIT_AGENCY_CONTRACT, CALC_WAIT_VED, CALC_WAIT_DETACHED,
    CALC_WAIT_DOCUMENT_MATCHING,

    CANCEL
}