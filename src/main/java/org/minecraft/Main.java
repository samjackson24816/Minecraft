package org.minecraft;

import org.engine.io.Input;
import org.engine.io.Window;
import org.lwjgl.glfw.GLFW;
import org.engine.math.Vector3f;

public class Main implements Runnable {

    public Thread gameThread;
    public Window window;

    public final int WIDTH = 1280, HEIGHT = 760;

    private final Game game;

    public Main() {
        this.game = new Game();
    }


    public void start() {
        gameThread = new Thread(this, "game");
        gameThread.start();
    }

    public void init() {
        //System.out.println("Initializing Game!");

        window = new Window(WIDTH, HEIGHT, "Minecraft");

        window.create();

        game.start();

        // Set the background color of the window
        window.setBackgroundColor(new Vector3f(1.0f, 0.0f, 0.0f));

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
        game.end();
    }

    public void update() {
        //System.out.println("Updating Game!");
        window.update();

        game.update(1, window);



    }

    public void render() {
        //System.out.println("Rendering Game!");

        game.render();

        window.swapBuffers();
    }

    public static void main(String[] args) {
        new Main().start();
    }
}
