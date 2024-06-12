package com.tomschlessinger.util;

import java.util.Objects;

public class Vector2f {
    public static Vector2f ZERO = new Vector2f(0,0);
    private float x;
    private float y;
    public Vector2f(float x, float y){
        this.x=x;
        this.y=y;
    }
    public Vector2f(Vector2f v){
        this.x=v.x;
        this.y=v.y;
    }

    public Vector2f setX(float x) {
        this.x = x;
        return this;
    }

    public Vector2f setY(float y) {
        this.y = y;
        return this;
    }
    public Vector2f set(Vector2f v){
        this.x=v.x;
        this.y=v.y;
        return this;
    }

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }
    public Vector2f subtract(float n){
        x-=n;
        y-=n;
        return this;
    }
    public Vector2f add(float n){
        x+=n;
        y+=n;
        return this;
    }
    public Vector2f subtract(float x, float y){
        this.x-=x;
        this.y-=y;
        return this;
    }
    public Vector2f subtract(Vector2f delta){
        this.x-=delta.x;
        this.y-=delta.y;
        return this;
    }

    public Vector2f add(Vector2f delta){
        this.x+=delta.x;
        this.y+=delta.y;
        return this;
    }
    public Vector2f add(float x, float y){
        this.x+=x;
        this.y+=y;
        return this;
    }
    public Vector2f multiply(float n){
        x*=n;
        y*=n;
        return this;
    }
    public Vector2f divide(float n){
        x/=n;
        y/=n;
        return this;
    }
    public double normalize(){
        return Math.sqrt(x*x+y*y);
    }
    public Vector2f copy(){
        return new Vector2f(this);
    }
    public Vector2f mod(float n){
        x%=n;
        y%=n;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2f vector2i = (Vector2f) o;
        return x == vector2i.x && y == vector2i.y;
    }

    public Vector2f abs(){
        x=Math.abs(x);
        y=Math.abs(y);
        return this;
    }
    public Vector2f absX(){
        x=Math.abs(x);
        return this;
    }
    public Vector2f absY(){
        y=Math.abs(y);
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
