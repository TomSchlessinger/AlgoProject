package com.tomschlessinger.util;

import java.util.Objects;

public class Vector2i {
    public static Vector2i ZERO = new Vector2i(0,0);
    private int x;
    private int y;
    public Vector2i(int x, int y){
        this.x=x;
        this.y=y;
    }
    public Vector2i(Vector2i v){
        this.x=v.x;
        this.y=v.y;
    }

    public Vector2i setX(int x) {
        this.x = x;
        return this;
    }

    public Vector2i setY(int y) {
        this.y = y;
        return this;
    }
    public Vector2i set(Vector2i v){
        this.x=v.x;
        this.y=v.y;
        return this;
    }
    public Vector2i set(int x, int y){
        this.x=x;
        this.y=y;
        return this;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }
    public Vector2i subtract(int n){
        x-=n;
        y-=n;
        return this;
    }
    public Vector2i add(int n){
        x+=n;
        y+=n;
        return this;
    }
    public Vector2i subtract(int x, int y){
        this.x-=x;
        this.y-=y;
        return this;
    }
    public Vector2i subtract(Vector2i delta){
        this.x-=delta.x;
        this.y-=delta.y;
        return this;
    }
    public Vector2i add(Vector2i delta){
        this.x+=delta.x;
        this.y+=delta.y;
        return this;
    }
    public Vector2i add(int x, int y){
        this.x+=x;
        this.y+=y;
        return this;
    }
    public Vector2i multiply(int n){
        x*=n;
        y*=n;
        return this;
    }
    public Vector2i divide(int n){
        x/=n;
        y/=n;
        return this;
    }
    public double normalize(){
        return Math.sqrt(x*x+y*y);
    }
    public Vector2i copy(){
        return new Vector2i(this);
    }
    public Vector2i mod(int n){
        x%=n;
        y%=n;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2i vector2i = (Vector2i) o;
        return x == vector2i.x && y == vector2i.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
