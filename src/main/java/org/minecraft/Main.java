package org.minecraft;

import org.engine.graphics.*;
import org.engine.io.Input;
import org.engine.io.Window;
import org.engine.math.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.engine.math.Vector3f;

public class Main implements Runnable {

    public Thread game;
    public Window window;

    public final int WIDTH = 1280, HEIGHT = 760;

    public Mesh mesh = new Mesh(new Vertex[] {
            new Vertex(new Vector3f(-0.5f,  0.5f, 0.0f), new Vector3f(1.0f, 0.0f, 0.0f), new Vector2f(0.0f, 0.0f)),
            new Vertex(new Vector3f(-0.5f, -0.5f, 0.0f), new Vector3f(0.0f, 1.0f, 0.0f), new Vector2f(0.0f, 1.0f)),
            new Vertex(new Vector3f( 0.5f, -0.5f, 0.0f), new Vector3f(0.0f, 0.0f, 1.0f), new Vector2f(1.0f, 1.0f)),
            new Vertex(new Vector3f( 0.5f,  0.5f, 0.0f), new Vector3f(1.0f, 1.0f, 0.0f), new Vector2f(1.0f, 0.0f))
    }, new int[] {
            0, 1, 2,
            0, 3, 2
    }, new Material("/textures/beautiful.png"));
    private Renderer renderer;

    private Shader shader;


    public void start() {
        game = new Thread(this, "game");
        game.start();
    }

    public void init() {
        //System.out.println("Initializing Game!");

        window = new Window(WIDTH, HEIGHT, "Minecraft");

        window.create();

        // Set the background color of the window
        window.setBackgroundColor(new Vector3f(1.0f, 0.0f, 0.0f));

        shader = new Shader("/shaders/mainVertex.glsl", "/shaders/mainFragment.glsl");




        renderer = new Renderer(shader);

        mesh.create();

        shader.create();
    }

    public void run() {
        init();

        while (!window.shouldClose()) {
            update();
            render();
            if (Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
                System.out.println("Key: " + GLFW.GLFW_KEY_ESCAPE + " Action: " + GLFW.GLFW_PRESS);
                break;
            }
        }

        close();

    }

    private void close() {
        window.destroy();
        shader.destroy();
        mesh.destroy();
    }

    public void update() {
        //System.out.println("Updating Game!");
        window.update();

        // Display mouse coords when left mouse button is pressed
        if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            System.out.println("Mouse X: " + Input.getMouseX() + " Mouse Y: " + Input.getMouseY());
        }

        // Enter fullscreen mode when F11 is pressed
        if (Input.isKeyDown(GLFW.GLFW_KEY_F11)) {
            window.setFullscreen(!window.isFullscreen());
        }

        // Display mouse scroll when scrolling
        if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
            System.out.println("Scroll X: " + Input.getScrollX() + " Scroll Y: " + Input.getScrollY());
        }

    }

    public void render() {
        //System.out.println("Rendering Game!");

        renderer.renderMesh(mesh);
        window.swapBuffers();
    }

    public static void main(String[] args) {
        new Main().start();
    }
}
