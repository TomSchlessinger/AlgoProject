package com.tomschlessinger.tile;

import com.tomschlessinger.collision.BoundingBox;

public class TileState {
    private AbstractTile tile;
    private final BoundingBox boundingBox;
    public TileState(BoundingBox box, AbstractTile tile){
        this.boundingBox=box;
        this.tile = tile;
    }
    public TileState(BoundingBox box){
        this(box,TileRegistry.getTile("air"));
    }
    public void setTile(AbstractTile tile) {
        this.tile = tile;
    }
    public BoundingBox getBoundingBox(){
        return boundingBox;
    }
    public AbstractTile getTile(){
        return tile;
    }
}
