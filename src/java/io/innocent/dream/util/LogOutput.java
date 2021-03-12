package io.innocent.dream.util;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogOutput extends PrintStream {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public LogOutput() {
        super(System.out);
    }

    @Override
    public void print(String x) {
        String s;
        LocalDateTime now = LocalDateTime.now();
        s = "[" + now.format(formatter) + "]: " + x;
        super.print(s);
    }

    public void print(boolean b) {
        print(b ? "true" : "false");
    }

    public void print(char c) {
        print(String.valueOf(c));
    }

    public void print(int i) {
        print(String.valueOf(i));
    }

    public void print(long l) {
        print(String.valueOf(l));
    }

    public void print(float f) {
        print(String.valueOf(f));
    }

    public void print(double d) {
        print(String.valueOf(d));
    }

    public void print(char[] s) {
        print(new String(s));
    }

    public void print(Object obj) {
        print(String.valueOf(obj));
    }

}
