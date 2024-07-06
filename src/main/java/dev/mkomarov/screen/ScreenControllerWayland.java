package dev.mkomarov.screen;

import dev.mkomarov.TerminalController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ScreenControllerWayland implements ScreenController {
    private static final int SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private static final int SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    private static final String DEFAULT_TEMPORARY_SCREENSHOT_PATH = "/tmp/screenshot.png";

    private ScreenControllerWayland() {
        throw new IllegalStateException("Utility class");
    }

    public BufferedImage takeScreenshot() {
        if (new File(DEFAULT_TEMPORARY_SCREENSHOT_PATH).exists()) {
            throw new RuntimeException("File already exists: " + DEFAULT_TEMPORARY_SCREENSHOT_PATH);
        }

        TerminalController.executeCommand("gnome-screenshot -f " + DEFAULT_TEMPORARY_SCREENSHOT_PATH);
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

    public Pixel findPixel(Pixel.Color color) {
        return findPixel(color, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    public Pixel findPixel(Pixel.Color color, int startX, int startY) {
        return findPixel(color, startX, startY, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    public Pixel findPixel(Pixel.Color color, int startX, int startY, int endX, int endY) {
        BufferedImage screenshot = takeScreenshot();

        Pixel pixelFound = null;

        for (int y = 0; y < screenshot.getHeight(); y++) {
            if (pixelFound != null) break;
            for (int x = 0; x < screenshot.getWidth(); x++) {
                if (pixelFound != null) break;

                int thisColor = screenshot.getRGB(x, y);
                int thisRed = Pixel.Color.getRed(thisColor);
                int thisGreen = Pixel.Color.getGreen(thisColor);
                int thisBlue = Pixel.Color.getBlue(thisColor);

                if (thisRed == color.getRed()
                        && thisGreen == color.getGreen()
                        && thisBlue == color.getBlue()) {
                    pixelFound = new Pixel(x, y, new Pixel.Color(thisColor));
                }
            }
        }

        return pixelFound;
    }

    public Pixel.Color getPixelColor(int x, int y) {
        BufferedImage screenshot = takeScreenshot();
        return new Pixel.Color(screenshot.getRGB(x, y));
    }
}
