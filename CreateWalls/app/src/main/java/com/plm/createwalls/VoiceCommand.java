package com.plm.createwalls;

import java.util.ArrayList;
import java.util.List;

public class VoiceCommand {

    private String command;
    private VoiceCommand parent;
    private List<VoiceCommand> childs;

    public VoiceCommand() {
        this.childs = new ArrayList<>();
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public VoiceCommand getParent() {
        return parent;
    }

    public void setParent(VoiceCommand parent) {
        this.parent = parent;
    }

    public List<VoiceCommand> getChilds() {
        return childs;
    }

    public void addChild(VoiceCommand child) {
        this.childs.add(child);
    }

}
