package dev.mkomarov.mouse;

import static dev.mkomarov.terminal.TerminalController.executeCommand;
import static dev.mkomarov.terminal.TerminalController.getCommandLog;
import static dev.mkomarov.screen.ScreenControllerWayland.SCREEN_HEIGHT;
import static dev.mkomarov.screen.ScreenControllerWayland.SCREEN_WIDTH;

public class MouseControllerWayland implements MouseController {
    private static final String GET_MOUSE_SPEED_COMMAND =
            "gsettings get org.gnome.desktop.peripherals.mouse speed ";
    private static final String GET_MOUSE_ACCEL_COMMAND =
            "gsettings get org.gnome.desktop.peripherals.mouse accel-profile ";
    private static final String SET_MOUSE_SPEED_COMMAND =
            "gsettings set org.gnome.desktop.peripherals.mouse speed ";
    private static final String SET_MOUSE_ACCEL_COMMAND =
            "gsettings set org.gnome.desktop.peripherals.mouse accel-profile ";

    private static final String DEFAULT_ACCEL_PROFILE = "flat";
    private static final double DEFAULT_SPEED = 0.0;

    @Override
    public void mouseMove(int x, int y) {
        String originalAccelProfileLog = getCommandLog(executeCommand(GET_MOUSE_ACCEL_COMMAND));
        String originalSpeedLog = getCommandLog(executeCommand(GET_MOUSE_SPEED_COMMAND));
        double originalSpeed = Double.parseDouble(originalSpeedLog);

        setMouseSpeed(DEFAULT_SPEED);
        setMouseAccelProfile(DEFAULT_ACCEL_PROFILE);

        executeCommand("ydotool mousemove --absolute -x " + x + " -y " + y, true, true);

        setMouseSpeed(originalSpeed);
        setMouseAccelProfile(originalAccelProfileLog);
    }

    private void setMouseSpeed(double speed) {
        executeCommand(SET_MOUSE_SPEED_COMMAND + speed);
    }

    private void setMouseAccelProfile(String profile) {
        if (!profile.contains("'")) {
            profile = "'" + profile + "'";
        }
        executeCommand(SET_MOUSE_ACCEL_COMMAND + profile);
    }

    @Override
    public void mouseMove(Direction direction, int pixels) {
        switch (direction) {
            case UP:
                executeCommand("ydotool mousemove_relative -- -0 " + (-pixels));
                break;
            case DOWN:
                executeCommand("ydotool mousemove_relative -- -0 " + pixels);
                break;
            case LEFT:
                executeCommand("ydotool mousemove_relative -- -" + pixels + " 0");
                break;
            case RIGHT:
                executeCommand("ydotool mousemove_relative " + pixels + " 0");
                break;
        }
    }

    @Override
    public void resetMousePosition() {
        // Storing cursor inside Waydroid window while session is active = system crash
        mouseMove(SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    @Override
    public void mouseClick() {
        executeCommand("ydotool click 0x40", true, true);
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        executeCommand("ydotool click 0x80", true, true);
    }

    @Override
    public void mouseDoubleClick() {
        executeCommand("ydotool click --repeat=2 1", true, true);
    }

    @Override
    public void mouseRightClick() {
        executeCommand("ydotool click 2", true, true);
    }

    @Override
    public void mouseMiddleClick() {
        executeCommand("ydotool click 3", true, true);
    }

    @Override
    public void mouseScroll(Direction direction, int amount) {
        switch (direction) {
            case UP:
                for (int i = 0; i < amount; i++) {
                    executeCommand("ydotool mousemove -w -- 0 1", true, true);
                }
                break;
            case DOWN:
                for (int i = 0; i < amount; i++) {
                    executeCommand("ydotool mousemove -w -- 0 -1", true, true);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid scroll direction: " + direction);
        }
    }

    @Override
    public void mouseSwipe(int startX, int startY, int endX, int endY, int speed) {
        mouseMove(startX, startY);

        executeCommand("ydotool mousedown 1");

        int steps = 50;
        int delay = speed / steps;

        int deltaX = (endX - startX) / steps;
        int deltaY = (endY - startY) / steps;

        try {
            for (int i = 0; i < steps; i++) {
                int currentX = startX + (deltaX * i);
                int currentY = startY + (deltaY * i);
                mouseMove(currentX, currentY);
                Thread.sleep(delay);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        mouseMove(endX, endY);

        executeCommand("ydotool mouseup 1");
    }
}
