package com.hypixel.hytale.server.core.ui.builder;

import com.hypixel.hytale.server.core.Message;

public class UICommandBuilder {

    public UICommandBuilder() {
    }

    public UICommandBuilder append(String path) {
        return this;
    }

    public UICommandBuilder append(String parent, String child) {
        return this;
    }

    public UICommandBuilder appendInline(String selector, String inlineUI) {
        return this;
    }

    public UICommandBuilder set(String selector, String value) {
        return this;
    }

    public UICommandBuilder set(String selector, Message value) {
        return this;
    }

    public UICommandBuilder clear(String selector) {
        return this;
    }
}
