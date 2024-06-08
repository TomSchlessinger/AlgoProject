package com.tomschlessinger.util;

public record Pixel(int r, int g, int b){
    public int getRed(){return r;}
    public int getGreen(){return g;}
    public int getBlue(){return b;}
    public static Pixel of(int r, int g, int b){
        return new Pixel(r,g,b);
    }
}