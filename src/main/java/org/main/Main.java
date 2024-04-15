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

import static org.lwjgl.opengl.GL11.*;

public class Main implements Runnable {
	public Thread game;
	public Window window;
	public Renderer renderer;
	public Shader shader;
	public BlockUtils blockUtils = new BlockUtils();
	public final int WIDTH = 1280, HEIGHT = 760;

	public int[][][] blocks = new int[10][10][10];

	public Material material = new Material("/textures/MinecraftTextures.png");
	public Mesh mesh = new Mesh(new Vertex[2], new int[2], material);
	
	public GameObject object = new GameObject(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1,1,1), mesh);



	public Camera camera = new Camera(new Vector3f(0, 5, 1), new Vector3f(0, -1, 0));
	
	public void start() {
		game = new Thread(this, "game");
		game.start();

	}
	
	public void init() {


		for (int i = 0; i < 2; i++) {
			mesh.getVertices()[i] = new Vertex(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector2f(0, 0));
		}
		window = new Window(WIDTH, HEIGHT, "Game");
		shader = new Shader("/shaders/mainVertex.glsl", "/shaders/mainFragment.glsl");
		renderer = new Renderer(window, shader);
		window.setBackgroundColor(1.0f, 0, 0);
		window.create();

		material.create();
		mesh.create();
		shader.create();
		glEnable(GL_DEPTH_TEST);
		//glDisable(GL_CULL_FACE);


		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				for (int z = 0; z < 10; z++) {
					blocks[x][y][z] = 3;
				}
			}
		}
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
		System.out.println("Rendering");
		var vert = new ArrayList<Vertex>();
		var ind = new ArrayList<Integer>();

		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				for (int z = 0; z < 10; z++) {
					if (blocks[x][y][z] > 0) {
						var position = new Vector3f(x, y, z);
						for (int i = 0; i < 6; i++) {

							blockUtils.addBlockDataToLists(blocks[x][y][z], i, position, vert, ind);
						}

					}
				}
			}
		}

		// Print out the contents of the vert and ind lists

		var vertArr = new Vertex [vert.size()];
		for (int i = 0; i < vert.size(); i++) {
			vertArr[i] = vert.get(i);
		}
		var indArr = new int [ind.size()];
		for (int i = 0; i < ind.size(); i++) {
			indArr[i] = ind.get(i);
		}

		var thisMesh = new Mesh(vertArr, indArr, material);

		thisMesh.create();
		object.setMesh(thisMesh);
		renderer.renderMesh(object, camera);

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