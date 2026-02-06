package com.hypixel.hytale.logger;

public class HytaleLogger {

    public static HytaleLogger forEnclosingClass() {
        return new HytaleLogger();
    }

    public LogEntry atInfo() {
        return new LogEntry();
    }

    public LogEntry atFine() {
        return new LogEntry();
    }

    public static class LogEntry {
        public void log(String message, Object... args) {
        }
    }
}
