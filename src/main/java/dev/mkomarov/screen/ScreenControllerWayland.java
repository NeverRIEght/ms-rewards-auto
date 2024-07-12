package dev.mkomarov.screen;

import dev.mkomarov.terminal.TerminalController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ScreenControllerWayland implements ScreenController {
    public static final int SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public static final int SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    private static final String DEFAULT_TEMPORARY_SCREENSHOT_PATH = "tmp_screenshot.png";

    public BufferedImage takeScreenshot() {
        if (new File(DEFAULT_TEMPORARY_SCREENSHOT_PATH).exists()) {
            System.out.println("Rewriting file: " + DEFAULT_TEMPORARY_SCREENSHOT_PATH);
        }

        TerminalController.executeCommand("gnome-screenshot --file=" + DEFAULT_TEMPORARY_SCREENSHOT_PATH, false, true);
        BufferedImage image = getImageFromPath(DEFAULT_TEMPORARY_SCREENSHOT_PATH);

        boolean isFileDeleted = new File(DEFAULT_TEMPORARY_SCREENSHOT_PATH).delete();

        if (!isFileDeleted) {
            throw new RuntimeException("Failed to delete the screenshot file: " + DEFAULT_TEMPORARY_SCREENSHOT_PATH);
        }

        return image;
    }

    private static BufferedImage getImageFromPath(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Pixel findPixel(Color color) {
        return findPixel(color, 0, 0, SCREEN_WIDTH - 1, SCREEN_HEIGHT - 1);
    }

    public Pixel findPixel(Color color, int startX, int startY) {
        return findPixel(color, startX, startY, SCREEN_WIDTH - 1, SCREEN_HEIGHT - 1);
    }

    public Pixel findPixel(Color color, Pixel startPixel, Pixel endPixel) {
        return findPixel(color, startPixel.getX(), startPixel.getY(), endPixel.getX(), endPixel.getY());
    }

    public Pixel findPixel(Color color, int startX, int startY, int endX, int endY) {
        BufferedImage screenshot = takeScreenshot();

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

                if (thisRed == color.getRed()
                        && thisGreen == color.getGreen()
                        && thisBlue == color.getBlue()) {
                    pixelFound = new Pixel(x, y, new Color(thisColor));
                }
            }
        }

        return pixelFound;
    }

    public Pixel findLastPixel(Color color) {
        return findLastPixel(color, 0, 0, SCREEN_WIDTH - 1, SCREEN_HEIGHT - 1);
    }

    public Pixel findLastPixel(Color color, int startX, int startY) {
        return findLastPixel(color, startX, startY, SCREEN_WIDTH - 1, SCREEN_HEIGHT - 1);
    }

    public Pixel findLastPixel(Color color, Pixel startPixel, Pixel endPixel) {
        return findLastPixel(color, startPixel.getX(), startPixel.getY(), endPixel.getX(), endPixel.getY());
    }

    public Pixel findLastPixel(Color color, int startX, int startY, int endX, int endY) {
        BufferedImage screenshot = takeScreenshot();

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

                if (thisRed == color.getRed()
                        && thisGreen == color.getGreen()
                        && thisBlue == color.getBlue()) {
                    pixelFound = new Pixel(x, y, new Color(thisColor));
                }
            }
        }

        return pixelFound;
    }

    public Color getPixelColor(int x, int y) {
        BufferedImage screenshot = takeScreenshot();
        return getPixelColor(screenshot, x, y);
    }

    public Color getPixelColor(BufferedImage screenshot, int x, int y) {
        return new Color(screenshot.getRGB(x, y));
    }

    public Pixel[] findBorder(Pixel startPixel) {
        BufferedImage screenshot = takeScreenshot();
        Color pixelColor = getPixelColor(screenshot, startPixel.getX(), startPixel.getY());
        return findBorder(screenshot, startPixel, pixelColor);
    }

    public Pixel[] findBorder(Pixel startPixel, Color borderColor) {
        BufferedImage screenshot = takeScreenshot();
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

    public Pixel findMiddlePixel(Pixel startPixel, Pixel endPixel) {
        return new Pixel(
                (startPixel.getX() + endPixel.getX()) / 2,
                (startPixel.getY() + endPixel.getY()) / 2
        );
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

    @Override
    public boolean waitForScreenChange(BufferedImage initialState, String actionName, int attempts, int delay) {
        int attemptsCounter = 0;
        boolean compareResult;
        try {
            BufferedImage currentState;
            do {
                Thread.sleep(delay);
                currentState = takeScreenshot();
                compareResult = compareImages(initialState, currentState);
                attemptsCounter++;
            } while (compareResult && attemptsCounter < attempts);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (attemptsCounter < attempts) {
            System.out.println(actionName + " with " + attemptsCounter + " attempts");
            return true;
        }

        return false;
    }
}
