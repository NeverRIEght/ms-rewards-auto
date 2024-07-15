package dev.mkomarov.mouse;

public interface MouseController {
    public static final int DEFAULT_SWIPE_SPEED = 100;
    public static final int DEFAULT_SWIPE_AMOUNT = 20;

    public void mouseMove(int x, int y);
    public void mouseMove(Direction direction, int pixels);
    public void resetMousePosition();
    public void mouseClick();
    public void mouseDoubleClick();
    public void mouseRightClick();
    public void mouseMiddleClick();
    public void mouseScroll(Direction direction, int amount);
    public void mouseSwipe(int startX, int startY, int endX, int endY, int speed);
}