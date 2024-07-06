package dev.mkomarov.keyboard;

import dev.mkomarov.TerminalController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import static dev.mkomarov.Main.ROOT_PASSWORD;

public class KeyboardControllerWayland implements KeyboardController {
    @Override
    public void keyClick(String validKey) {
        TerminalController.executeCommand("ydotool key " + validKey);
    }

    @Override
    public void printInstantly(String text) {
        TerminalController.executeCommand("ydotool type --key-delay 0 '" + text + "'");
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
