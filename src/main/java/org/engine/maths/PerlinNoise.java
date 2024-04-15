package org.engine.maths;

import java.util.Random;

public class PerlinNoise {
    private final int[] permutation;

    public PerlinNoise() {
        permutation = new int[512];
        Random rand = new Random();
        for (int i = 0; i < 512; i++) {
            permutation[i] = rand.nextInt(256);
        }
    }

    private double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }

    private double grad(int hash, double x, double y) {
        int h = hash & 15;
        double u = h < 8 ? x : y;
        double v = h < 4 ? y : h == 12 || h == 14 ? x : 0;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }

    public double noise(double x, double y) {
        int X = (int) Math.floor(x) & 255;
        int Y = (int) Math.floor(y) & 255;
        x -= Math.floor(x);
        y -= Math.floor(y);
        double u = fade(x);
        double v = fade(y);
        int A = permutation[X] + Y, AA = permutation[A], AB = permutation[A + 1],
                B = permutation[X + 1] + Y, BA = permutation[B], BB = permutation[B + 1];
        return lerp(v, lerp(u, grad(permutation[AA], x, y),
                        grad(permutation[BA], x - 1, y)),
                lerp(u, grad(permutation[AB], x, y - 1),
                        grad(permutation[BB], x - 1, y - 1)));
    }
}
