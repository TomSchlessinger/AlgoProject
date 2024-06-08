package com.tomschlessinger.util;

public class Vector2i {
    private int x;
    private int y;
    public Vector2i(int x, int y){this.x=x; this.y=y;}
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }
    public Vector2i divide(int n){
        x/=n;
        y/=n;
        return this;
    }

}
