package dev.mkomarov.screen;

public class Color {
    private int code;

    public Color(int code) {
        this.code = code;
    }

    public Color(int red, int green, int blue) {
        code = (red << 16) | (green << 8) | blue;
    }

    public void setColor(int code) {
        this.code = code;
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

}
