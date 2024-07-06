package dev.mkomarov.keyboard;

import dev.mkomarov.TerminalController;

import java.util.Random;

public class KeyboardControllerWayland implements KeyboardController {
    @Override
    public void keyClick(String validKey) {
        TerminalController.executeCommand("ydotool key " + validKey, true, true);
    }

    @Override
    public void printInstantly(String text) {
        TerminalController.executeCommand("ydotool type --key-delay 0 '" + text + "'", true, true);
    }

    @Override
    public void print(String text, int minDelayMs, int maxDelayMs) {
        Random random = new Random();
        char[] chars = text.toCharArray();
        try {
            for (char c : chars) {
                keyClick(String.valueOf(c));
                Thread.sleep(random.nextInt(minDelayMs, maxDelayMs));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
