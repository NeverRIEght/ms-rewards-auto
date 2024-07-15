package dev.mkomarov.mouse;

public interface MouseController {
    static MouseController getInstance() {
        return new MouseControllerWayland();
    }

    void mouseMove(int x, int y);

    void resetMousePosition();

    void mouseClick();

    void mouseRightClick();

    void mouseMiddleClick();

    void mouseScroll(Direction direction, int amount);

    void mouseSwipe(int startX, int startY, int endX, int endY, int speed);
}