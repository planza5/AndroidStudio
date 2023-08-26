package com.plm.planning;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task {
    private static SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
    public Task(String s1,String s2) throws ParseException {
        initTask=sdf.parse(s1);
        endTask=sdf.parse(s2);
    }

    private Date initTask;
    private Date endTask;

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
