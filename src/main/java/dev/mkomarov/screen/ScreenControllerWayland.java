package dev.mkomarov.screen;

import dev.mkomarov.image.ImageController;
import dev.mkomarov.image.ImageControllerWayland;
import dev.mkomarov.terminal.TerminalController;

import java.awt.image.BufferedImage;
import java.io.File;

public class ScreenControllerWayland implements ScreenController {
    private static final ImageController imageController = new ImageControllerWayland();
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
}
