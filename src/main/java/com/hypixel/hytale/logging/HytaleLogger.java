package com.hypixel.hytale.logging;

/**
 * Stub class for Hytale API - HytaleLogger
 * This is a temporary stub until the official Hytale API is released.
 */
public class HytaleLogger {
    public static HytaleLogger forEnclosingClass() {
        return new HytaleLogger();
    }
    
    public LogBuilder atInfo() {
        return new LogBuilder("INFO");
    }
    
    public LogBuilder atWarn() {
        return new LogBuilder("WARN");
    }
    
    public LogBuilder atError() {
        return new LogBuilder("ERROR");
    }
    
    public void info(String message) {
        System.out.println("[INFO] " + message);
    }
    
    public void warn(String message) {
        System.out.println("[WARN] " + message);
    }
    
    public void error(String message) {
        System.err.println("[ERROR] " + message);
    }
    
    public void error(String message, Throwable throwable) {
        System.err.println("[ERROR] " + message);
        throwable.printStackTrace();
    }
    
    public static class LogBuilder {
        private String level;
        
        public LogBuilder(String level) {
            this.level = level;
        }
        
        public void log(String message) {
            System.out.println("[" + level + "] " + message);
        }
    }
}
