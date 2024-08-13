package dev.mkomarov.phone;

import dev.mkomarov.image.ImageController;
import dev.mkomarov.image.ImageControllerWayland;
import dev.mkomarov.keyboard.KeyboardController;
import dev.mkomarov.keyboard.KeyboardControllerWayland;
import dev.mkomarov.mouse.MouseController;
import dev.mkomarov.mouse.MouseControllerWayland;
import dev.mkomarov.random.RandomProvider;
import dev.mkomarov.screen.Color;
import dev.mkomarov.screen.Pixel;
import dev.mkomarov.screen.ScreenController;
import dev.mkomarov.screen.ScreenControllerWayland;
import dev.mkomarov.search.SearchController;
import dev.mkomarov.search.SearchControllerImpl;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.util.Random;

import static dev.mkomarov.terminal.TerminalController.executeCommand;
import static dev.mkomarov.terminal.TerminalController.getCommandLog;

public class WaydroidController implements PhoneController {
    private static final SearchController searchController = new SearchControllerImpl();
    private static final KeyboardController keyboardController = new KeyboardControllerWayland();
    private static final ScreenController screenController = new ScreenControllerWayland();
    private static final ImageController imageController = new ImageControllerWayland();
    private static final MouseController mouseController = new MouseControllerWayland();
    private static final String BING_APP_PACKAGE_NAME = "com.microsoft.bing";
    private static final int WAYDROID_WINDOW_WIDTH = 720;
    private Thread sessionThread;

    private static final int SESSION_LAUNCH_ATTTEMPTS = 5;
    private static final int SESSION_STOP_ATTTEMPTS = 5;
    private static final int GET_STATUS_ATTTEMPTS = 5;

    private Pixel[] waydroidWindow = new Pixel[2];

    public void setWaydroidWindowWidth(int width) {
        mouseController.resetMousePosition();
        sessionThread = new WaydroidSessionDaemonThread();
        sessionThread.setDaemon(true);
        sessionThread.start();

        executeCommand("waydroid prop set persist.waydroid.width " + width, false, true);
        closeSession();
    }

    @Override
    public void launchSession() {
//        setWaydroidWindowWidth(WAYDROID_WINDOW_WIDTH);
        sessionThread = new WaydroidSessionDaemonThread();
        sessionThread.setDaemon(true);
        sessionThread.start();

        try {
            do {
                Thread.sleep(1000);
            } while (getStatus() != Status.RUNNING);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        BufferedImage initialState = screenController.takeScreenshot();
        executeCommand("waydroid show-full-ui");
        boolean isUIAppeared = screenController.waitForScreenChange(
                initialState,
                "Waydroid session started",
                SESSION_LAUNCH_ATTTEMPTS,
                50);

        if (!isUIAppeared) {
            throw new RuntimeException("Failed to start Waydroid session");
        }
    }

    @Override
    public void closeSession() {
        mouseController.resetMousePosition();

        sessionThread.interrupt();
        executeCommand("waydroid session stop");

        int attemptsCount = 0;
        while (getStatus() != Status.STOPPED && attemptsCount < SESSION_STOP_ATTTEMPTS) {
            attemptsCount++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Status getStatus() {
        Status status = getStatus(GET_STATUS_ATTTEMPTS);
        if (status == null) {
            throw new RuntimeException("Failed to get Waydroid status");
        }
        return status;
    }

    private Status getStatus(int attempts) {
        if (sessionThread == null || !sessionThread.isAlive()) {
            return Status.STOPPED;
        }

        int attemptsCount = 0;
        Status status = null;
        try {
            while (status == null && attemptsCount < attempts) {
                String statusString = getCommandLog(executeCommand("waydroid status"));

                attemptsCount++;

                do {
                    Thread.sleep(1000);
                } while (statusString.isEmpty());

                if (statusString.contains("STOPPED")) {
                    status = Status.STOPPED;
                } else if (statusString.contains("RUNNING")) {
                    status = Status.RUNNING;
                } else {
                    status = null;
                }
            }
        } catch (InterruptedException e) {
            System.out.println("getStatus() interrupted");
        }

        return status;
    }

    private enum Status {
        STOPPED, RUNNING
    }

    @Override
    public void openApp(String appName) {
        mouseController.resetMousePosition();
        executeCommand("waydroid prop set persist.waydroid.fake_touch " + appName);

        BufferedImage initialState = screenController.takeScreenshot();
        executeCommand("waydroid app launch " + appName);

        boolean isAppOpened = screenController.waitForScreenChange(
                initialState,
                "App " + appName + " opened",
                5,
                100);

        if (!isAppOpened) {
            throw new RuntimeException("Failed to open app " + appName);
        }
    }

    @Override
    public void doDaily() {
        try {
            Color black = new Color(0, 0, 0);
            Pixel bottomRightCorner = imageController.findLastPixelByColor(black);

            waydroidWindow = imageController.findBorder(bottomRightCorner);
            Pixel screenCenter = imageController.findCenterPixel(waydroidWindow[0], waydroidWindow[1]);

            Thread.sleep(10000);

            openApp(BING_APP_PACKAGE_NAME);
            Thread.sleep(2000);

            doDailySearches(screenCenter, 20);
            Thread.sleep(2000);

            collectDailyBonus();
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void collectDailyBonus() {
        try {
            BufferedImage appsButtonImage = imageController.getImageFromPath("/home/mkomarov/Documents/Programming/Java/ms-rewards-auto/src/main/resources/apps_button.png");
            screenController.findImageAndClick(appsButtonImage);
            Thread.sleep(2000);

            BufferedImage rewardsImage = imageController.getImageFromPath("/home/mkomarov/Documents/Programming/Java/ms-rewards-auto/src/main/resources/rewards.png");
            screenController.findImageAndClick(rewardsImage);
            Thread.sleep(10000);

            //Day 1
            mouseController.mouseMove(125, 590);
            Thread.sleep(300);
            mouseController.mouseClick();

            //Day 2
            mouseController.mouseMove(220, 590);
            Thread.sleep(300);
            mouseController.mouseClick();

            //Day 3
            mouseController.mouseMove(315, 590);
            Thread.sleep(300);
            mouseController.mouseClick();

            //Day 4
            mouseController.mouseMove(410, 590);
            Thread.sleep(300);
            mouseController.mouseClick();

            //Day 5
            mouseController.mouseMove(505, 590);
            Thread.sleep(300);
            mouseController.mouseClick();

            //Day 6
            mouseController.mouseMove(600, 590);
            Thread.sleep(300);
            mouseController.mouseClick();

            //Day 7
            mouseController.mouseMove(695, 590);
            Thread.sleep(300);
            mouseController.mouseClick();


            Thread.sleep(2000);

            BufferedImage homeButtonImage = imageController.getImageFromPath("/home/mkomarov/Documents/Programming/Java/ms-rewards-auto/src/main/resources/home_button.png");
            screenController.findImageAndClick(homeButtonImage);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doDailySearches(Pixel screenCenter, int amount) {
        Random random = RandomProvider.getRandom();
        try {
//            Pixel searchBoxPosition = new Pixel(screenCenter.getX(), screenCenter.getY() - 150);
            Pixel searchBoxPosition = imageController.findSubImageLocation(imageController.getImageFromPath("/home/mkomarov/Documents/Programming/Java/ms-rewards-auto/src/main/resources/search.png"), screenController.takeScreenshot());
            System.out.println("Search box position: " + searchBoxPosition);
            for (int i = 0; i < 20; i++) {
                Thread.sleep(random.nextInt(1000, 2000));
                mouseController.mouseMove(searchBoxPosition.getX(), searchBoxPosition.getY());
                Thread.sleep(500);
                mouseController.mouseClick();
                Thread.sleep(1500);
                String currentWord = searchController.getRandomWord();
                keyboardController.print(currentWord, 150, 400);
                Thread.sleep(random.nextInt(300, 500));
                keyboardController.keyClick("enter");
                Thread.sleep(random.nextInt(2000, 3000));
                keyboardController.keyClick("esc");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doDailyNews() {

    }

    private static class WaydroidSessionDaemonThread extends Thread implements Closeable {
        private Process process;

        @Override
        public void run() {
            process = executeCommand("waydroid session start", false, true);
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                System.out.println("Waydroid session interrupted");
            } finally {
                this.close();
            }
        }

        @Override
        public void close() {
            System.out.println("Waydroid session is closing...");
            try {
                if (process != null) {
                    process.destroy();
                    process.waitFor();
                    System.out.println("Waydroid session is closed.");
                }
            } catch (Exception e) {
                System.err.println("Could not close Waydroid session process");
                e.printStackTrace();
            }
        }
    }
}
