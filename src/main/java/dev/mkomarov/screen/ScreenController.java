package dev.mkomarov.screen;

import dev.mkomarov.TerminalController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public interface ScreenController {
    public BufferedImage takeScreenshot();
    public Pixel findPixel(Pixel.Color color);
    public Pixel findPixel(Pixel.Color color, int startX, int startY);
    public Pixel findPixel(Pixel.Color color, int startX, int startY, int endX, int endY);
    public Pixel.Color getPixelColor(int x, int y);
}