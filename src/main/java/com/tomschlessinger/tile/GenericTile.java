package com.tomschlessinger.tile;

public class GenericTile extends AbstractTile{
    public GenericTile(String s, String texturePath){
        super(s,texturePath);
    }
    public GenericTile(String s, String texturePath, TileOptions options){
        super(s,texturePath, options);
    }
}
