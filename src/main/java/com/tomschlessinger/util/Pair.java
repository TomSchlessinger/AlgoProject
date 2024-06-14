package com.tomschlessinger.util;

import java.util.Objects;

public class Pair<A,B> {
    private A left;
    private B right;

    public Pair(A left, B right){
        this.left=left;
        this.right=right;
    }

    public A getLeft(){return left;}
    public B getRight(){return right;}
    public void setLeft(A left){this.left=left;}
    public void setRight(B right){this.right=right;}
    public void set(A left, B right){
        setLeft(left);
        setRight(right);
    }
    public void set(Pair<A,B> pair){
        set(pair.getLeft(),pair.getRight());
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof Pair pair){
            return(pair.getLeft().equals(this.getLeft()) && pair.getRight().equals(this.getRight()));
        }
        return false;
    }
    public static <A,B> Pair<A,B> of(A left, B right){return new Pair<>(left,right);}

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
