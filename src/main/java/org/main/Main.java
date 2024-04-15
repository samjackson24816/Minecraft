package org.main;

import org.engine.graphics.*;
import org.engine.maths.Vector2f;
import org.lwjgl.glfw.GLFW;

import org.engine.io.Input;
import org.engine.io.Window;
import org.engine.maths.Vector3f;
import org.engine.objects.Camera;
import org.engine.objects.GameObject;

import java.util.ArrayList;
import java.util.Random;

import static org.engine.maths.MathUtils.modulusAngle;
import static org.engine.maths.MathUtils.modulusAngleVector;
import static org.lwjgl.opengl.GL11.*;

public class Main implements Runnable {
	public Thread game;
	public Window window;
	public Renderer renderer;
	public Shader shader;
	public BlockUtils blockUtils = new BlockUtils();
	public final int WIDTH = 1280, HEIGHT = 760;

	public Material material = new Material("/textures/MinecraftTextures.png");
	Chunk chunk = new Chunk(0, 0, 0, 16, 16, 16);



	public Camera camera = new Camera(new Vector3f(0, 5, 1), new Vector3f(0, -1, 0));
	
	public void start() {
		game = new Thread(this, "game");
		game.start();

	}
	
	public void init() {

		chunk.fillRandom();

		window = new Window(WIDTH, HEIGHT, "Game");
		shader = new Shader("/shaders/mainVertex.glsl", "/shaders/mainFragment.glsl");
		renderer = new Renderer(window, shader);
		window.setBackgroundColor(1.0f, 0, 0);
		window.create();

		material.create();
		shader.create();
		glEnable(GL_DEPTH_TEST);
		//glDisable(GL_CULL_FACE);


	}
	
	public void run() {
		init();
		while (!window.shouldClose() && !Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
			update();
			render();
			if (Input.isKeyDown(GLFW.GLFW_KEY_F11)) window.setFullscreen(!window.isFullscreen());
			if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) window.mouseState(true);
		}
		close();
	}
	
	private void update() {
		window.update();
		camera.update();

		String titleData = "Position: " + camera.getPosition().getX() + ", " + camera.getPosition().getY() + ", " + camera.getPosition().getZ();
		var displayRot = modulusAngleVector(camera.getRotation());
		titleData += " | Rotation: " + displayRot.getX() + ", " + displayRot.getY() + ", " + displayRot.getZ();
		var playerForward = Vector3f.normalize(new Vector3f((float) -Math.sin(Math.toRadians(displayRot.getY())), (float) Math.sin(Math.toRadians(displayRot.getX())), (float) -Math.cos(Math.toRadians(displayRot.getY()))));
		titleData += " | Forward: " + playerForward.getX() + ", " + playerForward.getY() + ", " + playerForward.getZ();
		window.setTitleData(titleData);

		// Log the data of the object that the player is looking at
		var hitInfo = chunk.raycast(camera.getPosition(), playerForward, 100);

		// Print out the info
		if (hitInfo.hit) {
			System.out.println("Hit block: " + hitInfo.blockId + " at " + hitInfo.x + ", " + hitInfo.y + ", " + hitInfo.z);
		}

		// If the player is looking at a block and they click the left mouse button, destroy the block
		if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT) && hitInfo.hit) {
			chunk.setBlock(hitInfo.x, hitInfo.y, hitInfo.z, 0);
		}
	}

	private void render() {

		var object = chunk.getGameObject(material);

		renderer.renderMesh(object, camera);

		window.swapBuffers();
		object.getMesh().destroy();
	}






	
	private void close() {
		window.destroy();

		material.destroy();
		shader.destroy();
	}
	
	public static void main(String[] args) {



		new Main().start();
	}
}