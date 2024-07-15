package dev.mkomarov.screen;

import dev.mkomarov.image.ImageController;
import dev.mkomarov.image.ImageControllerWayland;
import dev.mkomarov.mouse.MouseController;
import dev.mkomarov.mouse.MouseControllerWayland;
import dev.mkomarov.terminal.TerminalController;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ScreenControllerWayland implements ScreenController {
    private final MouseController mouseController = new MouseControllerWayland();
    private final ImageController imageController = new ImageControllerWayland();

    public static final int SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public static final int SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    private static final String DEFAULT_TEMPORARY_SCREENSHOT_PATH = "tmp_screenshot.png";

    public BufferedImage takeScreenshot() {
        File defaultScreenshotFile = new File(DEFAULT_TEMPORARY_SCREENSHOT_PATH);
        if (defaultScreenshotFile.exists()) {
            System.out.println("Rewriting file: " + DEFAULT_TEMPORARY_SCREENSHOT_PATH);
        }

        TerminalController.executeCommand("gnome-screenshot --file=" + DEFAULT_TEMPORARY_SCREENSHOT_PATH, false, true);
        BufferedImage image = imageController.getImageFromPath(DEFAULT_TEMPORARY_SCREENSHOT_PATH);

        boolean isFileDeleted = defaultScreenshotFile.delete();

        if (!isFileDeleted) {
            throw new RuntimeException("Failed to delete the screenshot file: " + DEFAULT_TEMPORARY_SCREENSHOT_PATH);
        }

        return image;
    }

    public boolean findImageAndClick(BufferedImage imageToSearchFor) {
        BufferedImage screenshot = takeScreenshot();
        Pixel imageCenter = imageController.findSubImageLocation(imageToSearchFor, screenshot);
        if (imageCenter != null) {
            mouseController.mouseMove(imageCenter.getX(), imageCenter.getY());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            mouseController.mouseClick();
            return true;
        }

        return false;
    }

    public boolean waitForScreenChange(BufferedImage initialState, String actionName, int attempts, int delay) {
        int attemptsCounter = 0;
        boolean compareResult;
        try {
            BufferedImage currentState;
            do {
                Thread.sleep(delay);
                currentState = takeScreenshot();
                compareResult = imageController.compareImages(initialState, currentState);
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
