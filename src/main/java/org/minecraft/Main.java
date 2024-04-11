package org.minecraft;

import org.engine.io.Window;

public class Main implements Runnable {

    public Thread game;
    public static Window window;
    public final static int WIDTH = 1280, HEIGHT = 760;


    public void start() {
        game = new Thread(this, "game");
        game.start();
    }

    public static void init() {
        //System.out.println("Initializing Game!");

        window = new Window(WIDTH, HEIGHT, "Minecraft");
        window.create();

    }

    public void run() {
        init();

        while (!window.shouldClose()) {
            update();
            render();
        }
    }

    public void update() {
        //System.out.println("Updating Game!");
        window.update();

    }

    public void render() {
        //System.out.println("Rendering Game!");

        window.swapBuffers();
    }

    public static void main(String[] args) {
        new Main().start();
    }
}
