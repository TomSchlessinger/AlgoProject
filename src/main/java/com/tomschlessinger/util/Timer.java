package com.tomschlessinger.util;

public class Timer {
    public static double getTime(){
        return System.nanoTime() / 1000000000d;
    }
    public static long getMillis(){
        return System.currentTimeMillis();
    }
    public static long getNanos(){
        return System.nanoTime();
    }
}
