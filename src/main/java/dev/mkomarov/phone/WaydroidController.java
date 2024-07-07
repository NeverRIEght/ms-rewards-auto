package dev.mkomarov.phone;

import static dev.mkomarov.TerminalController.executeCommand;
import static dev.mkomarov.TerminalController.getCommandLog;

public class WaydroidController implements PhoneController {
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
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        executeCommand("waydroid show-full-ui");
    }

    @Override
    public void closeSession() {
        sessionThread.interrupt();
        executeCommand("waydroid session stop");
    }

    private Status getStatus() {
        String statusString = getCommandLog(executeCommand("waydroid status"));

        try {
            Thread.sleep(1000);
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
