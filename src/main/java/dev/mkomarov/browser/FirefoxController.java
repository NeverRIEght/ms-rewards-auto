package dev.mkomarov.browser;

import dev.mkomarov.image.ImageController;
import dev.mkomarov.image.ImageControllerWayland;
import dev.mkomarov.random.RandomProvider;
import dev.mkomarov.terminal.TerminalController;
import dev.mkomarov.keyboard.KeyboardController;
import dev.mkomarov.keyboard.KeyboardControllerWayland;
import dev.mkomarov.mouse.MouseController;
import dev.mkomarov.mouse.MouseControllerWayland;
import dev.mkomarov.screen.Color;
import dev.mkomarov.screen.Pixel;
import dev.mkomarov.screen.ScreenController;
import dev.mkomarov.screen.ScreenControllerWayland;
import dev.mkomarov.search.SearchController;
import dev.mkomarov.search.SearchControllerImpl;

import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class FirefoxController implements BrowserController {
    public static final String LAUNCH_FIREFOX_COMMAND;
    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static final KeyboardController keyboardController = new KeyboardControllerWayland();
    private static final MouseController mouseController = new MouseControllerWayland();
    private static final ScreenController screenController = new ScreenControllerWayland();
    private static final ImageController imageController = new ImageControllerWayland();
    private static final SearchController searchController = new SearchControllerImpl();

    private static final int LAUNCH_ATTEMPTS = 10;
    private static final int CLOSE_ATTEMPTS = 10;
    private static final Color SEARCH_SELECTION_COLOR = new Color(56, 216, 120);

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
    public void launchBrowser() {
        BufferedImage initialState = screenController.takeScreenshot();
        TerminalController.executeCommand(LAUNCH_FIREFOX_COMMAND);
        boolean isLaunched = screenController.waitForScreenChange(
                initialState,
                "Firefox launched",
                LAUNCH_ATTEMPTS,
                100);
        if (!isLaunched) {
            System.out.println("Failed to launch Firefox");
        }
    }

    @Override
    public void closeBrowser() {
        BufferedImage initialState = screenController.takeScreenshot();
        keyboardController.keyClick("ctrl+q");
        boolean isClosureWindowAppeared = screenController.waitForScreenChange(
                initialState,
                "Firefox closure window appeared",
                CLOSE_ATTEMPTS,
                100);

        if (!isClosureWindowAppeared) {
            System.out.println("Failed to close Firefox");
            return;
        }

        initialState = screenController.takeScreenshot();
        keyboardController.keyClick("enter");
        boolean isFirefoxClosed = screenController.waitForScreenChange(
                initialState,
                "Firefox closed",
                CLOSE_ATTEMPTS,
                100);

        if (!isFirefoxClosed) {
            System.out.println("Failed to close Firefox");
        }
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
        keyboardController.print(url, 30, 50);
        keyboardController.keyClick("enter");
    }

    @Override
    public Pixel searchOnPage(String text) {
        try {
            keyboardController.keyClick("ctrl+f");
            Thread.sleep(700);

            keyboardController.print("Daily");
            Thread.sleep(300);

            keyboardController.keyClick("enter");

            Thread.sleep(1000);

            Pixel pixelFound = imageController.findPixelByColor(SEARCH_SELECTION_COLOR, 0, 0);

            if (pixelFound == null) throw new RuntimeException("Pixel not found");
            return pixelFound;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doDailySites() {
        try {
            Pixel dailySetCoords = searchOnPage("Daily");

            // Open first link
            mouseController.mouseMove(dailySetCoords.getX() + 200, dailySetCoords.getY() + 150);
            mouseController.mouseClick();
            TimeUnit.MILLISECONDS.sleep(3000);
            closeTab();

            // Open second link
            TimeUnit.MILLISECONDS.sleep(200);
            mouseController.mouseMove(dailySetCoords.getX() + 1000, dailySetCoords.getY() + 150);
            mouseController.mouseClick();
            TimeUnit.MILLISECONDS.sleep(3000);
            closeTab();

            // Open third link
            TimeUnit.MILLISECONDS.sleep(200);
            mouseController.mouseMove(dailySetCoords.getX() + 1500, dailySetCoords.getY() + 150);
            mouseController.mouseClick();
            TimeUnit.MILLISECONDS.sleep(3000);
            closeTab();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doDailySearches(int amount) {
        Random random = RandomProvider.getRandom();
        try {
            for (int i = 0; i < amount; i++) {
                String currentWord = searchController.getRandomWord();
                TimeUnit.MILLISECONDS.sleep(random.nextInt(1000, 2000));
                createNewTab();

                TimeUnit.MILLISECONDS.sleep(200);
                navigateTo("bing.com");

                TimeUnit.MILLISECONDS.sleep(random.nextInt(300, 500));
                keyboardController.print(currentWord, 450, 600);
                TimeUnit.MILLISECONDS.sleep(random.nextInt(300, 500));
                keyboardController.keyClick("enter");
                TimeUnit.MILLISECONDS.sleep(random.nextInt(2000, 3000));
                closeTab();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
