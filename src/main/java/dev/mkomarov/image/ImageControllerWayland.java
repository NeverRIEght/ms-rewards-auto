package dev.mkomarov.image;

import dev.mkomarov.screen.Color;
import dev.mkomarov.screen.Pixel;
import dev.mkomarov.screen.ScreenController;
import dev.mkomarov.screen.ScreenControllerWayland;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static dev.mkomarov.screen.ScreenControllerWayland.SCREEN_HEIGHT;
import static dev.mkomarov.screen.ScreenControllerWayland.SCREEN_WIDTH;

public class ImageControllerWayland implements ImageController {
    private static final ScreenController screenController = new ScreenControllerWayland();

    public BufferedImage getImageFromPath(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean comparePixels(Color color1, Color color2) {
        return color1.equals(color2);
    }

    public boolean compareImages(BufferedImage image1, BufferedImage image2) {
        if (image1 == null || image2 == null) return false;
        if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight()) return false;

        for (int y = 0; y < image1.getHeight(); y++) {
            for (int x = 0; x < image1.getWidth(); x++) {
                if (image1.getRGB(x, y) != image2.getRGB(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }

    public Color getPixelColor(int x, int y) {
        BufferedImage screenshot = screenController.takeScreenshot();
        return getPixelColor(screenshot, x, y);
    }

    public Color getPixelColor(BufferedImage screenshot, int x, int y) {
        return new Color(screenshot.getRGB(x, y));
    }

    public Pixel findPixelByColor(Color color) {
        return findPixelByColor(color, 0, 0, SCREEN_WIDTH - 1, SCREEN_HEIGHT - 1);
    }

    public Pixel findPixelByColor(Color color, Pixel startPixel, Pixel endPixel) {
        return findPixelByColor(color, startPixel.getX(), startPixel.getY(), endPixel.getX(), endPixel.getY());
    }

    public Pixel findPixelByColor(Color color, int startX, int startY, int endX, int endY) {
        BufferedImage screenshot = screenController.takeScreenshot();

        if (endX >= SCREEN_WIDTH) throw new IllegalArgumentException("endX is out of bounds: " + endX);
        if (endY >= SCREEN_HEIGHT) throw new IllegalArgumentException("endY is out of bounds: " + endY);

        Pixel pixelFound = null;
        for (int y = startY; y < endY; y++) {
            if (pixelFound != null) break;
            for (int x = startX; x < endX; x++) {
                if (pixelFound != null) break;

                int thisColor = screenshot.getRGB(x, y);
                int thisRed = Color.getRed(thisColor);
                int thisGreen = Color.getGreen(thisColor);
                int thisBlue = Color.getBlue(thisColor);

                if (comparePixels(new Color(thisRed, thisGreen, thisBlue), color)) {
                    pixelFound = new Pixel(x, y, new Color(thisColor));
                }
            }
        }

        return pixelFound;
    }

    public Pixel findLastPixelByColor(Color color) {
        return findLastPixelByColor(color, 0, 0, SCREEN_WIDTH - 1, SCREEN_HEIGHT - 1);
    }

    public Pixel findLastPixelByColor(Color color, Pixel startPixel, Pixel endPixel) {
        return findLastPixelByColor(color, startPixel.getX(), startPixel.getY(), endPixel.getX(), endPixel.getY());
    }

    public Pixel findLastPixelByColor(Color color, int startX, int startY, int endX, int endY) {
        BufferedImage screenshot = screenController.takeScreenshot();

        if (endX >= SCREEN_WIDTH) throw new IllegalArgumentException("endX is out of bounds: " + endX);
        if (endY >= SCREEN_HEIGHT) throw new IllegalArgumentException("endY is out of bounds: " + endY);

        Pixel pixelFound = null;
        for (int y = endY; y >= startY; y--) {
            if (pixelFound != null) break;
            for (int x = endX; x >= startX; x--) {
                if (pixelFound != null) break;

                int thisColor = screenshot.getRGB(x, y);
                int thisRed = Color.getRed(thisColor);
                int thisGreen = Color.getGreen(thisColor);
                int thisBlue = Color.getBlue(thisColor);

                if (comparePixels(new Color(thisRed, thisGreen, thisBlue), color)) {
                    pixelFound = new Pixel(x, y, new Color(thisColor));
                }
            }
        }

        return pixelFound;
    }

    public Pixel findCenterPixel(Pixel startPixel, Pixel endPixel) {
        return new Pixel(
                (startPixel.getX() + endPixel.getX()) / 2,
                (startPixel.getY() + endPixel.getY()) / 2
        );
    }

    public Pixel[] findBorder(Pixel startPixel) {
        BufferedImage screenshot = screenController.takeScreenshot();
        Color pixelColor = getPixelColor(screenshot, startPixel.getX(), startPixel.getY());
        return findBorder(screenshot, startPixel, pixelColor);
    }

    public Pixel[] findBorder(Pixel startPixel, Color borderColor) {
        BufferedImage screenshot = screenController.takeScreenshot();
        return findBorder(screenshot, startPixel, borderColor);
    }

    public Pixel[] findBorder(BufferedImage screenshot, Pixel startPixel, Color borderColor) {
        Pixel leftPixel = new Pixel(startPixel.getX(), startPixel.getY());
        Pixel rightPixel = new Pixel(startPixel.getX(), startPixel.getY());

        while (getPixelColor(screenshot, leftPixel.getX(), leftPixel.getY()).equals(borderColor)) {
            leftPixel = new Pixel(leftPixel.getX() - 1, leftPixel.getY());
        }

        while (getPixelColor(screenshot, rightPixel.getX(), rightPixel.getY()).equals(borderColor)) {
            rightPixel = new Pixel(rightPixel.getX() + 1, rightPixel.getY());
        }

        Pixel topPixel = new Pixel(startPixel.getX(), startPixel.getY());
        Pixel bottomPixel = new Pixel(startPixel.getX(), startPixel.getY());

        while (getPixelColor(screenshot, topPixel.getX(), topPixel.getY()).equals(borderColor)) {
            topPixel = new Pixel(topPixel.getX(), topPixel.getY() - 1);
        }

        while (getPixelColor(screenshot, bottomPixel.getX(), bottomPixel.getY()).equals(borderColor)) {
            bottomPixel = new Pixel(bottomPixel.getX(), bottomPixel.getY() + 1);
        }

        Pixel topLeftPixel = new Pixel(leftPixel.getX(), topPixel.getY());
        Pixel bottomRightPixel = new Pixel(rightPixel.getX(), bottomPixel.getY());

        return new Pixel[]{topLeftPixel, bottomRightPixel};
    }

    public Pixel findSubImageLocation(BufferedImage imageToSearchFor, BufferedImage imageToSearchOn) {
        for (int y = 0; y < imageToSearchOn.getHeight(); y++) {
            for (int x = 0; x < imageToSearchOn.getWidth(); x++) {
                if (x + imageToSearchFor.getWidth() > imageToSearchOn.getWidth() || y + imageToSearchFor.getHeight() > imageToSearchOn.getHeight()) {
                    break;
                }

                BufferedImage subImage = imageToSearchOn.getSubimage(x, y, imageToSearchFor.getWidth(), imageToSearchFor.getHeight());
                if (compareImages(subImage, imageToSearchFor)) {
                    return new Pixel(x + subImage.getWidth() / 2, y + subImage.getHeight() / 2);
                }
            }
        }

        return null;
    }
}
