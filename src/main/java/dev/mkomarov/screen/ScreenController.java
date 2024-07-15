package dev.mkomarov.screen;

import java.awt.image.BufferedImage;

public interface ScreenController {
    BufferedImage takeScreenshot();
    Pixel findPixel(Color color);
    Pixel findPixel(Color color, int startX, int startY);
    Pixel findPixel(Color color, Pixel startPixel, Pixel endPixel);
    Pixel findPixel(Color color, int startX, int startY, int endX, int endY);
    Pixel findLastPixel(Color color);
    Pixel findLastPixel(Color color, int startX, int startY);
    Pixel findLastPixel(Color color, Pixel startPixel, Pixel endPixel);
    Pixel findLastPixel(Color color, int startX, int startY, int endX, int endY);
    Pixel findMiddlePixel(Pixel startPixel, Pixel endPixel);
    Color getPixelColor(int x, int y);
    Color getPixelColor(BufferedImage screenshot, int x, int y);
    Pixel[] findBorder(Pixel startPixel);
    Pixel[] findBorder(Pixel startPixel, Color borderColor);
    Pixel[] findBorder(BufferedImage screenshot, Pixel startPixel, Color borderColor);
    Pixel findImageOnImage(BufferedImage imageToSearchFor, BufferedImage imageToSearchOn);
    boolean findImageAndClick(BufferedImage imageToSearchFor);
    boolean compareImages(BufferedImage image1, BufferedImage image2);
    boolean waitForScreenChange(BufferedImage initialState, String actionName, int attempts, int delay);
    BufferedImage getImageFromPath(String path);
}