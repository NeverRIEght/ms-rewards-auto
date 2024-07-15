package dev.mkomarov.screen;

import java.util.Objects;

public class Pixel {
    private final int x;
    private final int y;
    private final Color color;

    public Pixel(int x, int y) {
        this.x = x;
        this.y = y;
        this.color = null;
    }

    public Pixel(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pixel pixel = (Pixel) o;
        return x == pixel.x && y == pixel.y && Objects.equals(color, pixel.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, color);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "x=" + x +
                ", y=" + y +
                ", color=" + color +
                '}';
    }
}
