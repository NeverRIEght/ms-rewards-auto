package dev.mkomarov.screen;

import dev.mkomarov.image.ImageController;
import dev.mkomarov.mouse.MouseController;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface ScreenController {
    int SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    int SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    static ScreenController getInstance() {
        return new ScreenControllerWayland();
    }

    BufferedImage takeScreenshot();

    default boolean findImageAndClick(BufferedImage imageToSearchFor) {
        ImageController imageController = ImageController.getInstance();
        MouseController mouseController = MouseController.getInstance();

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

    default boolean waitForScreenChange(BufferedImage initialState, String actionName, int attempts, int delay) {
        ImageController imageController = ImageController.getInstance();

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