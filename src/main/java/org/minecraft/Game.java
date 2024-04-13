package org.minecraft;

import org.engine.graphics.*;
import org.engine.io.Window;
import org.engine.math.Vector2f;
import org.engine.math.Vector3f;

public class Game {

    Shader shader = new Shader("/shaders/mainVertex.glsl", "/shaders/mainFragment.glsl");


    int[][][] blocks = new int[8][8][8];

    Vector3f playerPosition = new Vector3f(0.0f, 0.0f, 0.0f);



    Mesh[] meshes = new Mesh[] {

        new Mesh(new Vertex[] {
                new Vertex(new Vector3f(-0.5f,  0.5f, 0.0f), new Vector3f(1.0f, 0.0f, 0.0f), new Vector2f(0.0f, 0.0f)),
                new Vertex(new Vector3f(-0.5f, -0.5f, 0.0f), new Vector3f(0.0f, 1.0f, 0.0f), new Vector2f(0.0f, 1.0f)),
                new Vertex(new Vector3f( 0.5f, -0.5f, 0.0f), new Vector3f(0.0f, 0.0f, 1.0f), new Vector2f(1.0f, 1.0f)),
                new Vertex(new Vector3f( 0.5f,  0.5f, 0.0f), new Vector3f(1.0f, 1.0f, 0.0f), new Vector2f(1.0f, 0.0f))
        }, new int[] {
                0, 1, 2,
                0, 3, 2
        }, new Material("/textures/beautiful.png"))

    };

    public Game() {

    }

    public void start() {
        // Handle all stuff requiring OpenGL, etc. here

        shader.create();
        for (Mesh m : meshes) {
            m.create();
        }
    }


    public void update(double deltaTime, Window window) {


    }


    public void render() {
        for (Mesh mesh : meshes) {
            Renderer.renderMesh(shader, mesh);
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
