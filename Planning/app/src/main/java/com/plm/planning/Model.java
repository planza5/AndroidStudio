package com.plm.planning;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import kotlin.collections.ArrayDeque;

public class Model {
    public static List<Task> TASKS;


    public static List<Task> getTasks(){
        if(TASKS==null){
            TASKS=new ArrayList<>();
        }

        return TASKS;
    }


}
