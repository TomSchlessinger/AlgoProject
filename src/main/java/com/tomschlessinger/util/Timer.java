package com.tomschlessinger.util;

public class Timer {
    public static double getTime(){
        return System.nanoTime() / 1000000000d;
    }
}
