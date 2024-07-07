package dev.mkomarov.screen;

import java.awt.image.BufferedImage;

public interface ScreenController {
    public BufferedImage takeScreenshot();
    public Pixel findPixel(Color color);
    public Pixel findPixel(Color color, int startX, int startY);
    public Pixel findPixel(Color color, int startX, int startY, int endX, int endY);
    public Pixel findLastPixel(Color color);
    public Pixel findLastPixel(Color color, int startX, int startY);
    public Pixel findLastPixel(Color color, int startX, int startY, int endX, int endY);
    public Color getPixelColor(int x, int y);
}