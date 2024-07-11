package dev.mkomarov.keyboard;

import dev.mkomarov.terminal.TerminalController;

import java.util.Random;

import static dev.mkomarov.terminal.TerminalController.executeCommand;

public class KeyboardControllerWayland implements KeyboardController {
    private static final int DEFAULT_KEY_DELAY = 10;

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
    public void keyClick(int keyCode) {
        keyDown(keyCode);
        keyUp(keyCode);
    }

    @Override
    public void keyClick(String validKey) {
        validKey = translateKey(validKey);
        executeCommand("ydotool key " + validKey, true, false);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void print(String text) {
        executeCommand("ydotool type --key-delay=0 '" + text + "'", true, false);
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
                print(String.valueOf(c));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String translateKey(String input) {
        String result = "";

        switch (input.toLowerCase()) {
            case "esc":
                result = "1:1 1:0";
                break;
            case "enter":
                result = "28:1 28:0";
                break;
            case "1":
                result = "2:1 2:0";
                break;
            case "2":
                result = "3:1 3:0";
                break;
            case "3":
                result = "4:1 4:0";
                break;
            case "4":
                result = "5:1 5:0";
                break;
            case "5":
                result = "6:1 6:0";
                break;
            case "6":
                result = "7:1 7:0";
                break;
            case "7":
                result = "8:1 8:0";
                break;
            case "8":
                result = "9:1 9:0";
                break;
            case "9":
                result = "10:1 10:0";
                break;
            case "0":
                result = "11:1 11:0";
                break;
            case "a":
                result = "30:1 30:0";
                break;
            case "b":
                result = "48:1 48:0";
                break;
            case "c":
                result = "46:1 46:0";
                break;
            case "d":
                result = "32:1 32:0";
                break;
            case "e":
                result = "18:1 18:0";
                break;
            case "f":
                result = "33:1 33:0";
                break;
            case "g":
                result = "34:1 34:0";
                break;
            case "h":
                result = "35:1 35:0";
                break;
            case "i":
                result = "23:1 23:0";
                break;
            case "j":
                result = "36:1 36:0";
                break;
            case "k":
                result = "37:1 37:0";
                break;
            case "l":
                result = "38:1 38:0";
                break;
            case "m":
                result = "50:1 50:0";
                break;
            case "n":
                result = "49:1 49:0";
                break;
            case "o":
                result = "24:1 24:0";
                break;
            case "p":
                result = "25:1 25:0";
                break;
            case "q":
                result = "16:1 16:0";
                break;
            case "r":
                result = "19:1 19:0";
                break;
            case "s":
                result = "31:1 31:0";
                break;
            case "t":
                result = "20:1 20:0";
                break;
            case "u":
                result = "22:1 22:0";
                break;
            case "v":
                result = "47:1 47:0";
                break;
            case "w":
                result = "17:1 17:0";
                break;
            case "x":
                result = "45:1 45:0";
                break;
            case "y":
                result = "21:1 21:0";
                break;
            case "z":
                result = "44:1 44:0";
                break;
            default:
                System.err.println("Invalid key: " + input);
        }

        return result;
    }

}
