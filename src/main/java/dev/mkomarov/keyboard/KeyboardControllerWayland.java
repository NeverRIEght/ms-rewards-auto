package dev.mkomarov.keyboard;

import dev.mkomarov.terminal.TerminalController;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static dev.mkomarov.terminal.TerminalController.executeCommand;

public class KeyboardControllerWayland implements KeyboardController {
    private static final int DEFAULT_KEY_DELAY = 10;
    private static final String KEYCODES_FILE_PATH = Paths.get("").toAbsolutePath()
            + File.separator + "src"
            + File.separator + "main"
            + File.separator + "resources"
            + File.separator + "ydotool_codes.txt";

    @Override
    public void keyDown(int keyCode) {
        executeCommand("ydotool key "
                        + keyCode + ":1",
                true,
                false);
        try {
            Thread.sleep(DEFAULT_KEY_DELAY);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void keyDown(String validKey) {
        keyDown(getKeyCode(validKey));
    }

    @Override
    public void keyUp(int keyCode) {
        executeCommand("ydotool key "
                        + keyCode + ":0",
                true,
                false);
        try {
            Thread.sleep(DEFAULT_KEY_DELAY);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void keyUp(String validKey) {
        keyUp(getKeyCode(validKey));
    }

    @Override
    public void keyClick(int keyCode) {
        keyDown(keyCode);
        keyUp(keyCode);
    }

    @Override
    public void keyClick(String validKey) {
        if (!validKey.contains("+")) {
            keyClick(getKeyCode(validKey));
        } else {
            String[] keys = validKey.split("\\+");
            for (int i = 0; i < keys.length - 1; i++) {
                keyDown(getKeyCode(keys[i]));
            }
            keyClick(getKeyCode(keys[keys.length - 1]));
            for (int i = keys.length - 2; i >= 0; i--) {
                keyUp(getKeyCode(keys[i]));
            }
        }
    }

    @Override
    public void print(String text) {
        executeCommand("ydotool type --key-delay=0 '" + text + "'", true, false);
    }

    @Override
    public void print(String text, int minDelayMs, int maxDelayMs) {
        Random random = new Random();
        try {
            for (char c : text.toCharArray()) {
                print(String.valueOf(c));
                Thread.sleep(random.nextInt(minDelayMs, maxDelayMs));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Integer> getKeycodes() {
        Map<String, Integer> keyCodes = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(
                new FileReader(KEYCODES_FILE_PATH))) {
            String str;

            while ((str = reader.readLine()) != null) {
                if (str.isEmpty()) continue;

                String[] parts = str.split(" ");
                int keyCode = Integer.parseInt(parts[parts.length - 1]);
                for (int i = 0; i < parts.length - 1; i++) {
                    String keyName = parts[i];
                    keyCodes.put(keyName, keyCode);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return keyCodes;
    }

    private int getKeyCode(String keyName) {
        Map<String, Integer> keyCodes = getKeycodes();
        Integer result = keyCodes.get(keyName);
        if (result == null) throw new RuntimeException("Invalid key: " + keyName);
        return result;
    }

    private String getKeyName(int keyCode) {
        Map<String, Integer> keyCodes = getKeycodes();
        for (Map.Entry<String, Integer> entry : keyCodes.entrySet()) {
            if (entry.getValue() == keyCode) {
                return entry.getKey();
            }
        }

        return null;
    }
}