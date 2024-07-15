package dev.mkomarov.screen;

import java.util.Objects;

public class Color {
    private int code;

    public Color(int code) {
        this.code = code & 0x00ffffff;
    }

    public Color(int red, int green, int blue) {
        code = (red << 16) | (green << 8) | blue;
    }

    public void setColor(int code) {
        this.code = code & 0x00ffffff;
    }

    public int getColor() {
        return this.code;
    }

    public int getRed() {
        return getRed(this.code);
    }

    public static int getRed(int code) {
        return (code & 0x00ff0000) >> 16;
    }

    public int getGreen() {
        return getGreen(this.code);
    }

    public static int getGreen(int code) {
        return (code & 0x0000ff00) >> 8;
    }

    public int getBlue() {
        return getBlue(this.code);
    }

    public static int getBlue(int code) {
        return code & 0x000000ff;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color color = (Color) o;
        return (code & 0x00ffffff) == (color.code & 0x00ffffff);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code & 0x00ffffff);
    }

    @Override
    public String toString() {
        return "Color{" +
                "red=" + getRed() +
                ", green=" + getGreen() +
                ", blue=" + getBlue() +
                '}';
    }
}
