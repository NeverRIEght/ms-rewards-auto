package dev.mkomarov.phone;

import dev.mkomarov.mouse.MouseController;
import dev.mkomarov.mouse.MouseControllerWayland;
import dev.mkomarov.screen.ScreenController;
import dev.mkomarov.screen.ScreenControllerWayland;

import static dev.mkomarov.TerminalController.executeCommand;
import static dev.mkomarov.TerminalController.getCommandLog;
import static dev.mkomarov.screen.ScreenControllerWayland.SCREEN_HEIGHT;
import static dev.mkomarov.screen.ScreenControllerWayland.SCREEN_WIDTH;

public class WaydroidController implements PhoneController {
    private static final ScreenController screenController = new ScreenControllerWayland();
    private static final MouseController mouseController = new MouseControllerWayland();
    public static final String BING_APP_PACKAGE_NAME = "com.microsoft.bing";
    public static final int WAYDROID_WINDOW_WIDTH = 720;
    private Thread sessionThread;

    public void setWaydroidWindowWidth(int width) {
        executeCommand("waydroid session start", false, true);
        executeCommand("waydroid prop set persist.waydroid.width " + width, false, true);
        closeSession();
    }

    @Override
    public void launchSession() {
        Runnable session = () -> {
            executeCommand("waydroid session start");
            try {
                do {
                    Thread.sleep(1000);
                } while (getStatus() == Status.RUNNING);
            } catch (InterruptedException ignored) {
            } finally {
                closeSession();
            }
        };

        sessionThread = new Thread(session);
        sessionThread.setDaemon(true);
        sessionThread.start();

        try {
            do {
                Thread.sleep(1000);
            } while (getStatus() != Status.RUNNING);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        executeCommand("waydroid show-full-ui");
    }

    @Override
    public void closeSession() {
        mouseController.mouseMove(SCREEN_WIDTH, SCREEN_HEIGHT);
        sessionThread.interrupt();
        executeCommand("waydroid session stop");
    }

    private Status getStatus() {
        String statusString = getCommandLog(executeCommand("waydroid status"));

        try {
            do {
                Thread.sleep(100);
            } while (statusString.isEmpty());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (statusString.contains("STOPPED")) {
            return Status.STOPPED;
        } else if (statusString.contains("RUNNING")) {
            return Status.RUNNING;
        } else {
            throw new RuntimeException("Unknown waydroid status");
        }
    }

    private enum Status {
        STOPPED, RUNNING
    }

    @Override
    public void openApp(String appName) {
        executeCommand("waydroid prop set persist.waydroid.fake_touch " + appName);
        executeCommand("waydroid app launch " + appName);
    }

    @Override
    public void doDailyNews() {

    }

    @Override
    public void doDailySearches(int amount) {

    }
}
