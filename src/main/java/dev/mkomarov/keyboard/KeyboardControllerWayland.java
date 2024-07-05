package dev.mkomarov.keyboard;

import java.io.IOException;
import java.util.Random;

public class KeyboardControllerWayland implements KeyboardController {
    private static void executeCommand(String command) {
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void printInstantly(String text) {
        print(text, 0, 1);
    }

    @Override
    public void print(String text, int minDelayMs, int maxDelayMs) {
        Random random = new Random();
        char[] chars = text.toCharArray();
        for (char c : chars) {
            executeCommand("ydotool type '" + c + "'");
            try {
                Thread.sleep(random.nextInt(minDelayMs, maxDelayMs));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        executeCommand("ydotool type '" + text + "'");
    }
}
