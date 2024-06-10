package com.tomschlessinger.tile;


import com.tomschlessinger.texture.Texture;

public abstract class AbstractTile {
    private final String textureFile;
    private final String id;
    private final Texture texture;
    public AbstractTile(String s, String texturePath){
        textureFile = texturePath;
        id=s;
        texture = texturePath == null ? null : new Texture(getTexturePath());

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
        return String.copyValueOf(new char[]{this.id.toCharArray()[0]});
    }
}
