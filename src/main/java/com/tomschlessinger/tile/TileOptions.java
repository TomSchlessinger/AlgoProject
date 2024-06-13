package com.tomschlessinger.tile;

public class TileOptions {
    private boolean solid = true;
    public TileOptions(){
    }
    public TileOptions setSolid(boolean solid){
        this.solid=solid;
        return this;
    }
    public boolean getSolid(){
        return solid;
    }
}
