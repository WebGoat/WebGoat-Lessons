package org.owasp.webgoat.converter;

public class Logger {

    private static int level = 0;

    public static void log(String message, Object... args) {
        for (int i = 0; i < level; i++) {
            System.out.print("\t");
        }
        System.out.print(String.format(message, args) + "\n");
    }

    public static void start(String message, Object... args) {
        log(message, args);
        level = level + 1;
    }

    public static void end() {
        if (level > 0) level = level - 1;
    }

    public static void end(String message, String... args) {
        log(message, args);
        level = level - 1;
    }

}
