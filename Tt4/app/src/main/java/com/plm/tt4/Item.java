package com.plm.tt4;

import java.util.ArrayList;
import java.util.List;

public class Item {
    public Item(){
    }

    public Item(Item parent, String name, boolean done){
        this.name=name;
        this.done=done;
        parent.getChilds().add(this);
        this.parent = parent;
    }

    private String name;
    private Item parent;
    private List<Item> childs=new ArrayList();
    private boolean done;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
