package dev.mkomarov.mouse;

import java.io.IOException;

public class MouseControllerWayland implements MouseController {
    private static void executeCommand(String command) {
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void mouseMove(int x, int y) {
        executeCommand("ydotool mousemove " + x + " " + y);
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
    public void mouseClick() {
        executeCommand("ydotool click 1");
    }

    @Override
    public void mouseDoubleClick() {
        executeCommand("ydotool click --repeat 2 1");
    }

    @Override
    public void mouseRightClick() {
        executeCommand("ydotool click 3");
    }

    @Override
    public void mouseMiddleClick() {
        executeCommand("ydotool click 2");
    }

    @Override
    public void mouseScroll(Direction direction, int amount) {
        switch (direction) {
            case UP:
                executeCommand("ydotool scroll up " + amount);
                break;
            case DOWN:
                executeCommand("ydotool scroll down " + amount);
                break;
            case LEFT:
                executeCommand("ydotool scroll left " + amount);
                break;
            case RIGHT:
                executeCommand("ydotool scroll right " + amount);
                break;
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
