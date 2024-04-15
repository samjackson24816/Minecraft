package org.engine.maths;

public class MathUtils {

    public static float modulusAngle(float angle) {
        // Modulus the angle to make it between -180 and 180
        // Take into account negative angles
        if (angle > 180) return (angle + 180) % 360 - 180;
        if (angle < -180) return (angle - 180) % 360 + 180;
        return angle;
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(value, max));
    }

    public static Vector3f modulusAngleVector(Vector3f angles) {
        return new Vector3f(modulusAngle(angles.getX()), modulusAngle(angles.getY()), modulusAngle(angles.getZ()));
    }


}
