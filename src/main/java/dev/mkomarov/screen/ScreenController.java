package dev.mkomarov.screen;

import java.awt.image.BufferedImage;

public interface ScreenController {
    static ScreenController getInstance() {
        return new ScreenControllerWayland();
    }

    BufferedImage takeScreenshot();

    boolean findImageAndClick(BufferedImage imageToSearchFor);

    boolean waitForScreenChange(BufferedImage initialState, String actionName, int attempts, int delay);
}