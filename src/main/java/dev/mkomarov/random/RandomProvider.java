package dev.mkomarov.random;

import java.util.Random;

public final class RandomProvider {
    private static final Random random = new Random();

    private RandomProvider() {
        throw new IllegalStateException("Utility class");
    }

    public static Random getRandom() {
        return random;
    }
}
