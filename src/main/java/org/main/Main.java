package org.main;

import org.engine.graphics.*;
import org.lwjgl.glfw.GLFW;

import org.engine.io.Input;
import org.engine.io.Window;
import org.engine.maths.Vector3f;

import static org.engine.maths.MathUtils.modulusAngleVector;
import static org.lwjgl.opengl.GL11.*;

public class Main implements Runnable {
	public Thread game;
	public Window window;
	public Renderer renderer;
	public Shader shader;
	public BlockUtils blockUtils = new BlockUtils();
	public final int WIDTH = 1280, HEIGHT = 760;
	public double time = 0;
	public Material material = new Material("/textures/MinecraftTextures.png");

	Chunk chunk = BlockUtils.generateHills();



	public Player player = new Player(new Vector3f(8, 18, 8), new Vector3f(0, -1, 0));
	
	public void start() {
		game = new Thread(this, "game");
		game.start();

	}
	
	public void init() {


		window = new Window(WIDTH, HEIGHT, "Game");
		shader = new Shader("/shaders/mainVertex.glsl", "/shaders/mainFragment.glsl");
		renderer = new Renderer(window, shader);
		window.setBackgroundColor(0.9f, 0.9f, 1.0f);
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

		var currentTime = System.nanoTime();
		var dt = (currentTime - time) / 1000000000.0;
		time = currentTime;

		player.update(dt, chunk);

		String titleData = "Position: " + player.getPosition().getX() + ", " + player.getPosition().getY() + ", " + player.getPosition().getZ();
		var displayRot = modulusAngleVector(player.getRotation());
		titleData += " | Rotation: " + displayRot.getX() + ", " + displayRot.getY() + ", " + displayRot.getZ();
		var playerForward = Vector3f.normalize(new Vector3f((float) -Math.sin(Math.toRadians(displayRot.getY())), (float) Math.tan(Math.toRadians(displayRot.getX())), (float) -Math.cos(Math.toRadians(displayRot.getY()))));
		titleData += " | Forward: " + playerForward.getX() + ", " + playerForward.getY() + ", " + playerForward.getZ();
		window.setTitleData(titleData);


	}

	private void render() {

		var object = chunk.getGameObject(material);

		renderer.renderMesh(object, player);




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