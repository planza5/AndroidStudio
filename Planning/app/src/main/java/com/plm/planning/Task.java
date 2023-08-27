package com.plm.planning;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Task {
    private static SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");

    public Task(String s1,String s2) throws ParseException {
        initTask=sdf.parse(s1);
        endTask=sdf.parse(s2);
    }

    private Task parent;

    private List<Task> childs=new ArrayList<>();

    private Date initTask;
    private Date endTask;

    public Task(Date d1, Date d2) {
        this.initTask=d1;
        this.endTask=d2;
    }

    public Date getInitTask() {
        return initTask;
    }

    public void setInitTask(Date initTask) {
        this.initTask = initTask;
    }

    public Date getEndTask() {
        return endTask;
    }

    public void setEndTask(Date endTask) {
        this.endTask = endTask;
    }
}
