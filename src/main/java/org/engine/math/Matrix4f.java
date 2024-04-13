package org.engine.math;

public class Matrix4f {

    public static final int SIZE = 4;

    private float[] elements;

    public float get(int x, int y) {
        return elements[y * SIZE + x];
    }

    public void set(int x, int y, float value) {
        elements[y * SIZE + x] = value;
    }

    public Matrix4f() {
        elements = new float[SIZE * SIZE];
    }

    public float[] getAll() {
        return elements;
    }

    public Matrix4f(float[] elements) {
        if (elements.length != SIZE * SIZE) {
            throw new IllegalArgumentException("Matrix4f: Invalid elements length");
        }
        this.elements = elements;
    }
}
