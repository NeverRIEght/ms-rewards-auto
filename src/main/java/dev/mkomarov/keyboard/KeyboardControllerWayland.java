package dev.mkomarov.keyboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import static dev.mkomarov.Main.ROOT_PASSWORD;

public class KeyboardControllerWayland implements KeyboardController {
    private static void executeCommand(String command) {
        try {
            command = "echo " + ROOT_PASSWORD + " | sudo -S " + command;
            // Use ProcessBuilder to execute the command
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
            Process process = pb.start();

            // Read the output from the command
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String s;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

            process.waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void keyClick(String validKey) {
        executeCommand("ydotool key " + validKey);
    }

    @Override
    public void printInstantly(String text) {
        executeCommand("ydotool type --key-delay 0 '" + text + "'");
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
