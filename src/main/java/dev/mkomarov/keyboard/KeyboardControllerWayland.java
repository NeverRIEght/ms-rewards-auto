package dev.mkomarov.keyboard;

import dev.mkomarov.TerminalController;

import java.util.Random;

import static dev.mkomarov.TerminalController.executeCommand;

public class KeyboardControllerWayland implements KeyboardController {
    @Override
    public void keyClick(String validKey) {
        executeCommand("ydotool key " + validKey, true, false);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void printInstantly(String text) {
        executeCommand("ydotool type --key-delay 0 '" + text + "'", true, false);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void print(String text, int minDelayMs, int maxDelayMs) {
        Random random = new Random();
        char[] chars = text.toCharArray();
        try {
            for (char c : chars) {
                Thread.sleep(random.nextInt(minDelayMs, maxDelayMs));
                keyClick(String.valueOf(c));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
