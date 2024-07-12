package dev.mkomarov.phone;

import dev.mkomarov.mouse.MouseController;
import dev.mkomarov.mouse.MouseControllerWayland;
import dev.mkomarov.screen.Color;
import dev.mkomarov.screen.Pixel;
import dev.mkomarov.screen.ScreenController;
import dev.mkomarov.screen.ScreenControllerWayland;

import java.awt.image.BufferedImage;
import java.io.Closeable;

import static dev.mkomarov.terminal.TerminalController.executeCommand;
import static dev.mkomarov.terminal.TerminalController.getCommandLog;

public class WaydroidController implements PhoneController {
    private static final ScreenController screenController = new ScreenControllerWayland();
    private static final MouseController mouseController = new MouseControllerWayland();
    public static final String BING_APP_PACKAGE_NAME = "com.microsoft.bing";
    public static final int WAYDROID_WINDOW_WIDTH = 720;
    private Thread sessionThread;

    private static final int SESSION_LAUNCH_ATTTEMPTS = 5;
    private static final int SESSION_STOP_ATTTEMPTS = 5;
    private static final int GET_STATUS_ATTTEMPTS = 5;

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
        setWaydroidWindowWidth(WAYDROID_WINDOW_WIDTH);
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
                    Thread.sleep(100);
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
            Pixel bottomRightCorner = screenController.findLastPixel(black);

            Pixel[] corners = screenController.findBorder(bottomRightCorner, black);
            Pixel screenMiddle = screenController.findMiddlePixel(corners[0], corners[1]);

            Thread.sleep(10000);

            openApp("com.microsoft.bing");
            Thread.sleep(2000);


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void collectDailyBonus() {

    }

    @Override
    public void doDailySearches(int amount) {

    }

    @Override
    public void doDailyNews() {

    }

    private static class WaydroidSessionDaemonThread extends Thread implements Closeable {
        private Process process;

        @Override
        public void run() {
            process = executeCommand("waydroid session start");
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
