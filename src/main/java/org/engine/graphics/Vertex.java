package org.engine.graphics;

import org.engine.math.Vector2f;
import org.engine.math.Vector3f;

public class Vertex {

    private Vector3f position;

    private Vector3f color;

    private Vector2f textureCoord;

    public Vertex(Vector3f position, Vector3f color, Vector2f textureCoord) {
        this.position = position;
        this.color = color;
        this.textureCoord = textureCoord;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getColor() {
        return color;
    }

    public Vector2f getTextureCoord() {
        return textureCoord;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

}
