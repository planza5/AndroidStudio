package com.plm.voicecreator;

import java.util.List;

public class VoiceCommand {
    private String command;
    private VoiceCommand parent;
    private List<VoiceCommand> childs;

    public VoiceCommand getParent() {
        return parent;
    }

    public void setParent(VoiceCommand parent) {
        this.parent = parent;
    }
}
