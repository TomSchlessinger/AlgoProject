package com.tomschlessinger.keybinds;

public record KeyBindContext(long window, int action, int scancode, int mods) {
    public long getWindow(){return window;}
    public int getScancode(){return scancode;}
    public int getMods(){return mods;}
    public int getAction(){return action;}
    public static KeyBindContext create(long window, int action, int scancode, int mods){
        return new KeyBindContext(window, action, scancode, mods);
    }
}
