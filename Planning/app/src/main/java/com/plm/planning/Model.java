package com.plm.planning;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Model {
    public static List<Task> TASKS=new ArrayList<>();

    static{
        try {
            TASKS.add(new Task(DateUtils.makeDate("28/08/2023"),DateUtils.makeDate("02/09/2023")));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
