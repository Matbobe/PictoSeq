package com.pictoseq.models;

public class Log {
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";

    public static void println(String message) {
        String timeStamp = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
        System.out.println(ANSI_GREEN + "[" + timeStamp + "] " + message + ANSI_RESET);
    }
    public static void printError(String message) {
        String timeStamp = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
        System.out.println(ANSI_RED + "[" + timeStamp + "] " + message + ANSI_RESET);
    }
}
