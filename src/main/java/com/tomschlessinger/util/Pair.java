package com.tomschlessinger.util;

import java.util.Objects;

public record Pair<A,B>(A left, B right) {
    public A getLeft(){return left;}
    public B getRight(){return right;}

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
