package com.tomschlessinger.font;

import org.newdawn.slick.util.ResourceLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Font {
    private final String fontname;
    private final Map<Character, Glyph> glyphs;
    private final boolean antiAlias;
    private int fontHeight;
    private int imageWidth;
    private int imageHeight;
    private int size;

    public Font(String fontname, boolean antiAlias){
        this.fontname=fontname;
        glyphs=new HashMap<>();
        this.antiAlias=antiAlias;
    }
    public void prepareFont() {
        java.awt.Font font = null;

        // load font from a .ttf file
        try {
            InputStream inputStream = ResourceLoader.getResourceAsStream("assets/fonts/" + fontname + ".ttf");
            font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, inputStream);
            font = font.deriveFont(24f); // set font size
        } catch (Exception e) {
            e.printStackTrace();
        }
        int width = 0, height = 0;
        for (int i = 32; i < 256; i++) {
            if (i == 127) {
                continue;
            }
            char c = (char) i;
            BufferedImage ch = createCharImage(font, c, antiAlias);

            imageWidth += ch.getWidth();
            imageHeight = Math.max(imageHeight, ch.getHeight());
        }
        fontHeight=imageHeight;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        for(int i = 32; i < 256; i++) {
            int x = 0;
            if (i == 127) continue;
            char c = (char) i;
            BufferedImage charImage = createCharImage(font, c, antiAlias);
            int charWidth = charImage.getWidth();
            int charHeight = charImage.getHeight();
            Glyph ch = new Glyph(charWidth, charHeight, x, image.getHeight() - charHeight);
            g.drawImage(charImage, x, 0, null);
            x += ch.width;
            glyphs.put(c, ch);
        }

    }
    public BufferedImage createCharImage(java.awt.Font font, char c, boolean antiAlias){
        BufferedImage image = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        if(antiAlias)g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        g.dispose();
        int charWidth = metrics.charWidth(c);
        int charHeight = metrics.getHeight();
        image = new BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();
        if (antiAlias) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        g.setFont(font);
        g.setPaint(Color.WHITE);
        g.drawString(String.valueOf(c), 0, metrics.getAscent());
        g.dispose();
        return image;
    }

    public void render(String text){
        int lines = 1;
        for(int i = 0; i < text.length(); i++){
            char c = text.charAt(i);
            if(c == '\n') {
                lines++;
            }
        }
        int height = lines * fontHeight;

//        float drawX = x;

    }
}
