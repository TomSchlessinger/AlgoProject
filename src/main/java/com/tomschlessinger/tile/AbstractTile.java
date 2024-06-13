package com.tomschlessinger.tile;


import com.tomschlessinger.texture.Texture;

public abstract class AbstractTile {
    private final String textureFile;
    private final String id;
    private final Texture texture;
    protected boolean solid;
    public AbstractTile(String s, String texturePath, TileOptions options){
        solid = options.getSolid();
        textureFile = texturePath;
        id=s;
        texture = texturePath == null ? null : new Texture(getTexturePath());
    }
    public AbstractTile(String s, String texturePath){
        this(s,texturePath,new TileOptions());
    }
    public String getId(){return id;}
    public String getTexturePath(){return "/assets/textures/tiles/" + textureFile;}

    public Texture getTexture() {
        return texture;
    }

    public void bind(){
        if(texture!=null)texture.bind();
    }

    public String getTextureFile(){return textureFile;}

    public String toString(){
        return this.id;
    }
    public boolean isSolid(){
        return solid;
    }
    public void setSolid(boolean solid){
        this.solid=solid;
    }
}
