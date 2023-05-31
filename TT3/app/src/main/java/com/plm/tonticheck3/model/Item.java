package com.plm.tonticheck3.model;

import java.util.ArrayList;
import java.util.List;

public class Item {
    private Item parent;
    private List<Item> childs=new ArrayList();
    private List<String> taskList = new ArrayList<>();
    private List<Boolean> taskStatus = new ArrayList<>();

    public Item getParent() {
        return parent;
    }

    public void setParent(Item parent) {
        this.parent = parent;
    }

    public List<Item> getChilds() {
        return childs;
    }

    public void setChilds(List<Item> childs) {
        this.childs = childs;
    }

    public List<String> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<String> taskList) {
        this.taskList = taskList;
    }

    public List<Boolean> getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(List<Boolean> taskStatus) {
        this.taskStatus = taskStatus;
    }
}
