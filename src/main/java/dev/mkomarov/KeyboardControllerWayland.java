package dev.mkomarov;

import java.io.IOException;

public class KeyboardControllerWayland implements KeyboardController {

    // Method to execute a command
    private static void executeCommand(String command) throws IOException {
        Runtime.getRuntime().exec(command);
    }

    @Override
    public void printInstantly(String text) {
        print(text, 0, 1);
    }

    @Override
    public void print(String text, int minDelayMs, int maxDelayMs) {
        executeCommand("ydotool type 'Hello'");
    }
}
