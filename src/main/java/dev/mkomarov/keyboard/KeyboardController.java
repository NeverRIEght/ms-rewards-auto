package dev.mkomarov.keyboard;

public interface KeyboardController {
    public void keyDown(int keyCode);
    public void keyDown(String validKey);
    public void keyUp(int keyCode);
    public void keyUp(String validKey);
    public void keyClick(int keyCode);
    public void keyClick(String validKey);
    public void print(String text);
    public void print(String text, int minDelayMs, int maxDelayMS);
}
