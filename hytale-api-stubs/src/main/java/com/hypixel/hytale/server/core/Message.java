package com.hypixel.hytale.server.core;

public class Message {
    public static Message raw(String text) {
        return new Message();
    }

    public static Message translation(String key) {
        return new Message();
    }

    public Message param(String key, String value) {
        return this;
    }
}
