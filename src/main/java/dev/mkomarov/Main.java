package dev.mkomarov;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main {
    public static final String SEP = File.separator;
    public static final String TXT_PATH = Paths.get("").toAbsolutePath()
            + SEP + "src"
            + SEP + "main"
            + SEP + "resources"
            + SEP + "words_alpha.txt";

    public static void main(String[] args) {
        List<String> words = getWordsFromTxt(TXT_PATH);
        Random random = new Random();

        try {
            Robot robot = new Robot();

            for (int i = 0; i < 30; i++) {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(1000, 2000));
                openNewTabWithRobot(robot);
                TimeUnit.MILLISECONDS.sleep(random.nextInt(300, 500));
                printWordWithRobot(robot, words.get(random.nextInt(words.size())));
                TimeUnit.MILLISECONDS.sleep(random.nextInt(300, 500));
                robot.keyPress(KeyEvent.VK_ENTER);
                TimeUnit.MILLISECONDS.sleep(random.nextInt(2000, 3000));
                closeTabWithRobot(robot);
            }

        } catch (AWTException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void openNewTabWithRobot(Robot robot) {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_T);
        robot.keyRelease(KeyEvent.VK_T);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    public static void closeTabWithRobot(Robot robot) {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_W);
        robot.keyRelease(KeyEvent.VK_W);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    public static void printWordWithRobot(Robot robot, String word) {
        Random random = new Random();
        for (char c : word.toCharArray()) {
            int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
            if (KeyEvent.CHAR_UNDEFINED == keyCode) {
                throw new RuntimeException(
                        "Key code not found for character '" + c + "'");
            }
            robot.keyPress(keyCode);
            robot.delay(random.nextInt(100, 300));
            robot.keyRelease(keyCode);
            robot.delay(random.nextInt(100, 300));
        }
    }

    public static List<String> getWordsFromTxt(String path) {
        List<String> words = new ArrayList<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();

            while (line != null) {
                words.add(line);
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return words;
    }
}
