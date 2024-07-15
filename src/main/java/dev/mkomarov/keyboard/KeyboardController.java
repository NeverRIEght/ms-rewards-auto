package dev.mkomarov.keyboard;

public interface KeyboardController {
    static KeyboardController getInstance() {
        return new KeyboardControllerWayland();
    }

    void keyDown(int keyCode);

    void keyDown(String validKey);

    void keyUp(int keyCode);

    void keyUp(String validKey);

    void keyClick(int keyCode);

    void keyClick(String validKey);

    void print(String text);

    void print(String text, int minDelayMs, int maxDelayMS);
}
