package org.engine.math;

public class Vector3f {

    public float x, y, z;

    public Vector3f() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f add(Vector3f r) {
        return new Vector3f(x + r.x, y + r.y, z + r.z);
    }

    public Vector3f subtract(Vector3f r) {
        return new Vector3f(x - r.x, y - r.y, z - r.z);
    }

    public Vector3f multiply(Vector3f r) {
        return new Vector3f(x * r.x, y * r.y, z * r.z);
    }

    public Vector3f multiply(float r) {
        return new Vector3f(x * r, y * r, z * r);
    }




    public static float angleBetween(Vector3f v1, Vector3f v2) {
        return (float) Math.acos(dot(v1, v2) / (v1.length() * v2.length()));
    }

    public Vector3f normalized() {
        float length = length();
        return new Vector3f(x / length, y / length, z / length);
    }


    public static float dot(Vector3f v1, Vector3f v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3f cross(Vector3f r) {
        float x_ = y * r.z - z * r.y;
        float y_ = z * r.x - x * r.z;
        float z_ = x * r.y - y * r.x;
        return new Vector3f(x_, y_, z_);
    }

    public Vector3f set(Vector3f r) {
        this.x = r.x;
        this.y = r.y;
        this.z = r.z;
        return this;
    }

    public Vector3f rotateY(float angle) {
        float sin = (float) Math.sin(angle);
        float cos = (float) Math.cos(angle);
        return new Vector3f(x * cos - z * sin, y, x * sin + z * cos);
    }

    public Vector3f rotateX(float angle) {
        float sin = (float) Math.sin(angle);
        float cos = (float) Math.cos(angle);
        return new Vector3f(x, y * cos - z * sin, y * sin + z * cos);
    }

}
