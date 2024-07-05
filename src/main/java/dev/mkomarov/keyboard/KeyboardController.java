package dev.mkomarov.keyboard;

public interface KeyboardController {
    public void keyClick(String validKey);
    public void printInstantly(String text);
    public void print(String text, int minDelayMs, int maxDelayMS);
}
