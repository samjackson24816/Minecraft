package org.minecraft;

import org.engine.graphics.*;
import org.engine.io.Input;
import org.engine.io.Window;
import org.engine.math.Vector2f;
import org.engine.math.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Game {

    Shader shader = new Shader("/shaders/mainVertex.glsl", "/shaders/mainFragment.glsl");


    int[][][] blocks = new int[8][8][8];

    Vector3f playerPosition = new Vector3f(0.0f, 0.0f, -1.0f);
    Vector3f playerForward = new Vector3f(0.0f, 0.0f, 1.0f);



    Mesh[] meshes = new Mesh[10];

    public Game() {

        // Create 10000 faces

        for (int i = 0; i < 10; i++) {
            meshes[i] = new Mesh(new Vertex[] {
                    new Vertex(new Vector3f(i -1f,  i + 1f, i), new Vector3f(1.0f, 0.0f, 0.0f), new Vector2f(0.0f, 0.0f)),
                    new Vertex(new Vector3f(i -1f, i -1f, i), new Vector3f(0.0f, 1.0f, 0.0f), new Vector2f(0.0f, 1.0f)),
                    new Vertex(new Vector3f( i + 1f, i -1, i), new Vector3f(0.0f, 0.0f, 1.0f), new Vector2f(1.0f, 1.0f)),
                    new Vertex(new Vector3f( i + 1f,  i + 1f, i), new Vector3f(1.0f, 1.0f, 0.0f), new Vector2f(1.0f, 0.0f))
            }, new int[] {
                    0, 2, 1,
                    0, 3, 2
            }, new Material("/textures/beautiful.png"));
        }
    }

    public void start() {
        // Handle all stuff requiring OpenGL, etc. here

        shader.create();
        for (Mesh m : meshes) {
            m.create();
        }
    }


    public void update(double deltaTime, Window window) {

        // Move around
        if (Input.isKeyDown(GLFW.GLFW_KEY_W)) {
            playerPosition = playerPosition.add(playerForward.multiply((float) deltaTime));
        }

        if (Input.isKeyDown(GLFW.GLFW_KEY_S)) {
            playerPosition = playerPosition.subtract(playerForward.multiply((float) deltaTime));
        }

        if (Input.isKeyDown(GLFW.GLFW_KEY_A)) {
            playerPosition = playerPosition.subtract(playerForward.cross(new Vector3f(0.0f, 1.0f, 0.0f)).normalized().multiply((float) deltaTime));
        }

        if (Input.isKeyDown(GLFW.GLFW_KEY_D)) {
            playerPosition = playerPosition.add(playerForward.cross(new Vector3f(0.0f, 1.0f, 0.0f)).normalized().multiply((float) deltaTime));
        }

        if (Input.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
            playerPosition = playerPosition.add(new Vector3f(0.0f, 1.0f, 0.0f).multiply((float) deltaTime));
        }

        if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            playerPosition = playerPosition.subtract(new Vector3f(0.0f, 1.0f, 0.0f).multiply((float) deltaTime));
        }

        // Rotate
        if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT)) {
            playerForward = playerForward.rotateY((float) deltaTime);
        }

        if (Input.isKeyDown(GLFW.GLFW_KEY_RIGHT)) {
            playerForward = playerForward.rotateY(-(float) deltaTime);
        }

        if (Input.isKeyDown(GLFW.GLFW_KEY_UP)) {
            playerForward = playerForward.rotateX((float) deltaTime);
        }

        if (Input.isKeyDown(GLFW.GLFW_KEY_DOWN)) {
            playerForward = playerForward.rotateX(-(float) deltaTime);
        }

        // Print out the player position x, y, z
        System.out.println("Player Position: " + playerPosition.x + ", " + playerPosition.y + ", " + playerPosition.z);


        // Set whether the window is fullscreen
        if (Input.isKeyDown(GLFW.GLFW_KEY_F11)) {
            window.setFullscreen(!window.isFullscreen());
        }

    }


    public void render(Window window) {

        var aspectRatio = (float) window.getWidth() / (float) window.getHeight();

        for (Mesh m : meshes) {
            Renderer.renderMesh(shader, m, playerPosition, playerForward, 0.1f, 0.08f * 0.001f * window.getWidth(), 0.08f * 0.001f * window.getHeight());
        }




    }

    public void end() {
        // Handle all stuff requiring cleanup, etc. here
        shader.destroy();
        for (Mesh m : meshes) {
            m.destroy();
        }
    }

}
