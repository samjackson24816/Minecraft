package org.main;

import org.engine.graphics.*;
import org.engine.maths.Vector2f;
import org.lwjgl.glfw.GLFW;

import org.engine.io.Input;
import org.engine.io.Window;
import org.engine.maths.Vector3f;
import org.engine.objects.Camera;
import org.engine.objects.GameObject;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glEnable;

public class Main implements Runnable {
	public Thread game;
	public Window window;
	public Renderer renderer;
	public Shader shader;
	public final int WIDTH = 1280, HEIGHT = 760;
	
	public Mesh mesh = new Mesh(
		new Vertex[] {
			new Vertex(new Vector3f(-1, -1, 0), new Vector3f(0, 0, 1), new Vector2f(0, 0)),
			new Vertex(new Vector3f(-1, 1, 0), new Vector3f(0, 0, 1), new Vector2f(0, 1)),
			new Vertex(new Vector3f(1, 1, 0), new Vector3f(0, 0, 1), new Vector2f(1, 1)),
			new Vertex(new Vector3f(1, -1, 0), new Vector3f(0, 0, 1), new Vector2f(1, 0))
		},
		new int[] {
			0, 1, 2,
			0, 2, 3
		},
		new Material("/textures/beautiful.png")
	);//ModelLoader.loadModel("resources/models/bunny.stl", "/textures/beautiful.png");
	


	// Create 100 game objects, and give them meshes
	public GameObject[] objects = new GameObject[10000];


	public Camera camera = new Camera(new Vector3f(0, 0, 1), new Vector3f(0, 0, 0));
	
	public void start() {
		game = new Thread(this, "game");
		game.start();

		for (int i = 0; i < objects.length; i++) {
			objects[i] = new GameObject(new Vector3f((float) (Math.random() * 100 - 50), (float) (Math.random() * 100 - 50), (float) (Math.random() * 100 - 50)), new Vector3f(0f, 0f, 0f), new Vector3f(1f, 1f, 1f), mesh);
		}
	}
	
	public void init() {
		window = new Window(WIDTH, HEIGHT, "Game");
		shader = new Shader("/shaders/mainVertex.glsl", "/shaders/mainFragment.glsl");
		renderer = new Renderer(window, shader);
		window.setBackgroundColor(1.0f, 0, 0);
		window.create();
		mesh.create();
		shader.create();
		glEnable(GL_DEPTH_TEST);
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
	}
	
	private void render() {
		for (GameObject object : objects) {
			renderer.renderMesh(object, camera);
		}
		window.swapBuffers();
	}
	
	private void close() {
		window.destroy();
		mesh.destroy();
		shader.destroy();
	}
	
	public static void main(String[] args) {
		new Main().start();
	}
}