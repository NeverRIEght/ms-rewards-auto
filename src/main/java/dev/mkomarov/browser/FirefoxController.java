package dev.mkomarov.browser;

import dev.mkomarov.keyboard.KeyboardController;
import dev.mkomarov.keyboard.KeyboardControllerWayland;
import dev.mkomarov.mouse.MouseController;
import dev.mkomarov.mouse.MouseControllerWayland;
import dev.mkomarov.screen.Pixel;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static dev.mkomarov.robot.RobotController.*;
import static dev.mkomarov.screen.ScreenControllerRobot.findPixel;

public class FirefoxController implements BrowserController {
    public static final String LAUNCH_FIREFOX_COMMAND;
    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static final KeyboardController keyboardController = new KeyboardControllerWayland();
    private static final MouseController mouseController = new MouseControllerWayland();

    private static final Pixel.Color SEARCH_SELECTION_COLOR = new Pixel.Color(56,216,120);

    static {
        if (OS.contains("win")) {
            LAUNCH_FIREFOX_COMMAND = "cmd /c start firefox";
        } else if (OS.contains("nix") || OS.contains("nux") || OS.contains("mac")) {
            LAUNCH_FIREFOX_COMMAND = "firefox";
        } else {
            throw new RuntimeException("Unsupported operating system");
        }
    }

    @Override
    public void launchBrowser() throws IOException {
        Runtime.getRuntime().exec(LAUNCH_FIREFOX_COMMAND);
    }

    @Override
    public void closeBrowser() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void createNewTab() {
        keyboardController.keyClick("ctrl+t");
    }

    @Override
    public void closeTab() {
        keyboardController.keyClick("ctrl+w");
    }

    @Override
    public void navigateTo(String url) {
        keyboardController.printInstantly(url);
    }

    @Override
    public Pixel searchOnPage(String text) {
        try {
        keyboardController.keyClick("ctrl+f");
            Thread.sleep(500);

            printWordWithRobot("daily");
            Thread.sleep(300);

            keyboardController.keyClick("enter");

            Thread.sleep(1000);

            Pixel pixelFound = findPixel(SEARCH_SELECTION_COLOR, 52, 0);

            if (pixelFound == null) throw new RuntimeException("Pixel not found");
            return pixelFound;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doDailySites() {

    }

    @Override
    public void doDailySearches(int amount) {

    }
}
