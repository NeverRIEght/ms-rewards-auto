package dev.mkomarov.image;

import dev.mkomarov.screen.Color;
import dev.mkomarov.screen.Pixel;

import java.awt.image.BufferedImage;

public interface ImageController {
    static ImageController getInstance() {
        return new ImageControllerWayland();
    }

    BufferedImage getImageFromPath(String path);

    boolean comparePixels(Color color1, Color color2);

    boolean compareImages(BufferedImage image1, BufferedImage image2);

    Color getPixelColor(int x, int y);

    Color getPixelColor(BufferedImage screenshot, int x, int y);

    Pixel findPixelByColor(Color color);

    Pixel findPixelByColor(Color color, Pixel startPixel, Pixel endPixel);

    Pixel findPixelByColor(Color color, int startX, int startY, int endX, int endY);

    Pixel findLastPixelByColor(Color color);

    Pixel findLastPixelByColor(Color color, Pixel startPixel, Pixel endPixel);

    Pixel findLastPixelByColor(Color color, int startX, int startY, int endX, int endY);

    Pixel findCenterPixel(Pixel startPixel, Pixel endPixel);

    Pixel[] findBorder(Pixel startPixel);

    Pixel[] findBorder(BufferedImage screenshot, Pixel startPixel, Color borderColor);

    Pixel findSubImageLocation(BufferedImage imageToSearchFor, BufferedImage imageToSearchOn);
}
