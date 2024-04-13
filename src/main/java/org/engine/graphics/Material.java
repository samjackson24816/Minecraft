package org.engine.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.io.IOException;

import org.newdawn.slick.opengl.Texture;

import org.newdawn.slick.opengl.TextureLoader;

public class Material {

    private Texture texture;
    private float width, height;

    private final String path;
    private int textureID;

    public Material(String path) {
        this.path = path;
    }

    public void create() {
        try {
            texture = TextureLoader.getTexture(path.split("[.]")[1], Material.class.getResourceAsStream(path), GL11.GL_NEAREST);
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                System.err.println("Couldn't load texture at " + path);
                e.printStackTrace();
            } else if (e instanceof IOException) {
                System.err.println("Couldn't load texture at " + path);

            } else {
                e.printStackTrace();

            }
        }
        width = texture.getWidth();
        height = texture.getHeight();
        textureID = texture.getTextureID();
    }

    public void destroy() {
        GL13.glDeleteTextures(textureID);
    }

    public int getTextureID() {
        return textureID;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }


}
