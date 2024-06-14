//I can't find who I took this from, but this isn't my code. Of course, the Perlin Noise Generation algorithm is a very
// well-known algorithm, so this code wouldn't have been unique even if I had written it myself

package com.tomschlessinger.random;

import java.util.Random;

public class PerlinNoiseGenerator {
    private final int[] permutation;

    public PerlinNoiseGenerator(long seed) {
        permutation = new int[512];
        Random random = new Random(seed);
        for (int i = 0; i < 256; i++) {
            permutation[i] = i;
        }
        for (int i = 0; i < 256; i++) {
            int j = random.nextInt(256);
            int swap = permutation[i];
            permutation[i] = permutation[j];
            permutation[j] = swap;
        }
        for (int i = 0; i < 256; i++) {
            permutation[256 + i] = permutation[i];
        }
    }

    public double noise(double x, double y) {
        int X = (int) Math.floor(x) & 255;
        int Y = (int) Math.floor(y) & 255;
        x -= Math.floor(x);
        y -= Math.floor(y);
        double u = fade(x);
        double v = fade(y);
        int A = permutation[X] + Y;
        int B = permutation[X + 1] + Y;
        return lerp(v, lerp(u, grad(permutation[A], x, y), grad(permutation[B], x - 1, y)),
                lerp(u, grad(permutation[A + 1], x, y - 1), grad(permutation[B + 1], x - 1, y - 1)));
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

}
