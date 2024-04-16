package org.engine.graphics;

import java.io.IOException;

import org.lwjgl.opengl.GL13;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import static org.lwjgl.opengl.GL11.GL_NEAREST;


/*
 * The material class is a container for textures and texture data
 * It is used to store textures and their properties
 * In a more complex implementation of the game, multiple materials would be used to store different textures
 * When you create a material, you pass in the path
 * When you call create(), it loads the texture from the path
 * Make sure not to call create() often!
 */
public class Material {
	private String path;
	private Texture texture;
	private float width, height;
	private int textureID;
	
	public Material(String path) {
		this.path = path;
	}

	
	public void create() {
		try {
			texture = TextureLoader.getTexture(path.split("[.]")[1], Material.class.getResourceAsStream(path), GL_NEAREST);
			width = texture.getWidth();
			height = texture.getHeight();
			textureID = texture.getTextureID();
		} catch (IOException e) {
			System.err.println("Can't find texture at " + path);
		}
	}
	
	public void destroy() {
		GL13.glDeleteTextures(textureID);
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public int getTextureID() {
		return textureID;
	}
}