package com.github.demansh.eztrace;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static java.lang.System.currentTimeMillis;

public class EzTracer {
    private static boolean track;
    private static int tabulation;

    private static List<Line> lines = new ArrayList<>();

    public static void lap(String name, Runnable action) {
        lap(name, () -> {
            action.run();
            return null;
        });
    }
    public static <T> T lap(String name, Callable<T> action) {
        long start = currentTimeMillis();
        T result = null;
        tabulation++;
        Line l = new Line(name, tabulation);
        if (track) lines.add(l);
        try {
            result = action.call();
        } catch (Exception e) {
            stop();
            throw new RuntimeException(e);
        } finally {
            l.millis = currentTimeMillis() - start;
            tabulation--;
            return result;
        }
    }

    private static String getSpace(int space) {
        if (space > 0) {
            return new String(new char[2 * space]).replace('\0', ' ');
        }
        return "";
    }
    public static void start() {
        track = true;
    }
    public static void stop() {
        track = false;
        lines.forEach(Line::print);
        lines.clear();
        tabulation = 0;
    }

    private static class Line {
        private String name;
        private int tabulation;
        private long millis;

        public Line(String name, int tabulation) {
            this.name = name;
            this.tabulation = tabulation;
        }

        private void print() {
            System.out.println(String.format("%s%s\t\t%d", getSpace(tabulation), name, millis));
        }
    }
}
