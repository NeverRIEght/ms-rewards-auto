package dev.mkomarov.screen;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static dev.mkomarov.robot.RobotController.robot;

public class ScreenControllerRobot {
    private ScreenControllerRobot() {
        throw new IllegalStateException("Utility class");
    }

    public static Pixel findPixel(Color color) {
        return findPixel(color, 0, 0, 1920, 1080);
    }

    public static Pixel findPixel(Color color, int startX, int startY) {
        return findPixel(color, startX, startY, 1920, 1080);
    }

    public static Pixel findPixel(Color color, int startX, int startY, int endX, int endY) {
        robot.delay(300);
        BufferedImage screenshot = robot.createScreenCapture(new Rectangle(startX, startY, endX, endY));
        robot.delay(300);

        Pixel pixelFound = null;

        for (int y = 0; y < screenshot.getHeight(); y++) {
            if (pixelFound != null) break;
            for (int x = 0; x < screenshot.getWidth(); x++) {
                if (pixelFound != null) break;

                int thisColor = screenshot.getRGB(x, y);
                int thisRed = Color.getRed(thisColor);
                int thisGreen = Color.getGreen(thisColor);
                int thisBlue = Color.getBlue(thisColor);

                if (thisRed == color.getRed()
                        && thisGreen == color.getGreen()
                        && thisBlue == color.getBlue()) {
                    pixelFound = new Pixel(x, y, new Color(thisColor));
                }
            }
        }

        return pixelFound;
    }

    public static Color getPixelColor(int x, int y) {
        BufferedImage screenshot = robot.createScreenCapture(new Rectangle(x, y, 1, 1));
        robot.delay(300);
        return new Color(screenshot.getRGB(0, 0));
    }

    public static BufferedImage getImageFromPath(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean compareImages(BufferedImage image1, BufferedImage image2) {
        if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight()) {
            return false;
        }

        for (int y = 0; y < image1.getHeight(); y++) {
            for (int x = 0; x < image1.getWidth(); x++) {
                if (image1.getRGB(x, y) != image2.getRGB(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static Pixel findImageOnScreen(BufferedImage image) {
        BufferedImage screenshot = robot.createScreenCapture(new Rectangle(0, 0, 1920, 1080));


        for (int y = 0; y < screenshot.getHeight(); y++) {
            for (int x = 0; x < screenshot.getWidth(); x++) {
                if (x + image.getWidth() > screenshot.getWidth() || y + image.getHeight() > screenshot.getHeight()) {
                    break;
                }


                BufferedImage subImage = screenshot.getSubimage(x, y, image.getWidth(), image.getHeight());
                if (compareImages(subImage, image)) {
                    return new Pixel(x + image.getWidth() / 2, y + image.getHeight() / 2, new Color(screenshot.getRGB(x, y)));
                }
            }
        }

        return null;
    }
}
