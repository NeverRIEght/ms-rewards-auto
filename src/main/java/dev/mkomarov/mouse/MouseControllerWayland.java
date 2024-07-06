package dev.mkomarov.mouse;

import dev.mkomarov.TerminalController;

public class MouseControllerWayland implements MouseController {
    @Override
    public void mouseMove(int x, int y) {
        TerminalController.executeCommand("swaymsg input \"9011:26214:ydotoold_virtual_device\" accel_profile flat");
        TerminalController.executeCommand("ydotool mousemove --absolute -- " + x + " " + y);
    }

    @Override
    public void mouseMove(Direction direction, int pixels) {
        switch (direction) {
            case UP:
                TerminalController.executeCommand("ydotool mousemove_relative -- -0 " + (-pixels));
                break;
            case DOWN:
                TerminalController.executeCommand("ydotool mousemove_relative -- -0 " + pixels);
                break;
            case LEFT:
                TerminalController.executeCommand("ydotool mousemove_relative -- -" + pixels + " 0");
                break;
            case RIGHT:
                TerminalController.executeCommand("ydotool mousemove_relative " + pixels + " 0");
                break;
        }
    }

    @Override
    public void mouseClick() {
        TerminalController.executeCommand("ydotool click 1");
    }

    @Override
    public void mouseDoubleClick() {
        TerminalController.executeCommand("ydotool click --repeat 2 1");
    }

    @Override
    public void mouseRightClick() {
        TerminalController.executeCommand("ydotool click 3");
    }

    @Override
    public void mouseMiddleClick() {
        TerminalController.executeCommand("ydotool click 2");
    }

    @Override
    public void mouseScroll(Direction direction, int amount) {
        switch (direction) {
            case UP:
                TerminalController.executeCommand("ydotool scroll up " + amount);
                break;
            case DOWN:
                TerminalController.executeCommand("ydotool scroll down " + amount);
                break;
            case LEFT:
                TerminalController.executeCommand("ydotool scroll left " + amount);
                break;
            case RIGHT:
                TerminalController.executeCommand("ydotool scroll right " + amount);
                break;
        }
    }

    @Override
    public void mouseSwipe(int startX, int startY, int endX, int endY, int speed) {
        mouseMove(startX, startY);

        TerminalController.executeCommand("ydotool mousedown 1");

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

        TerminalController.executeCommand("ydotool mouseup 1");
    }
}
