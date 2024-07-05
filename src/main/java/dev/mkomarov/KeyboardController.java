package dev.mkomarov;

public interface KeyboardController {
    public void printInstantly(String text);
    public void print(String text, int minDelayMs, int maxDelayMS);
}
