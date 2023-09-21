package com.example.atopic.service.menu;

import com.example.atopic.model.jpa.User;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalendarService {


    private Map<User, Calendar> calendarTmp = new HashMap<>();

    public void add(User user, Calendar calendar) {
        calendarTmp.put(user, calendar);
    }

    public Calendar get(User user) {
        return calendarTmp.get(user);
    }
}
