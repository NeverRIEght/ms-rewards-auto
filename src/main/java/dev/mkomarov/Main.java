package dev.mkomarov;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {
    public static final String SEP = File.separator;
    public static final String WORDS_TXT_PATH = Paths.get("").toAbsolutePath()
            + SEP + "src"
            + SEP + "main"
            + SEP + "resources"
            + SEP + "words_alpha.txt";
    public static final String ROBOT_CODES_TXT_PATH = Paths.get("").toAbsolutePath()
            + SEP + "src"
            + SEP + "main"
            + SEP + "resources"
            + SEP + "robot_codes.txt";

    public static void main(String[] args) {
        List<String> words = getWordsFromTxt(WORDS_TXT_PATH);
        Random random = new Random();

        try {
            Robot robot = new Robot();
            TimeUnit.MILLISECONDS.sleep(500);
            doDailySites(robot);

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

    public static void doDailySites(Robot robot) {
        openNewTabWithRobot(robot);
        printWordInstantlyWithRobot(robot, "https://rewards.bing.com/");

        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);

        robot.delay(1500);

        robot.keyPress(KeyEvent.VK_END);
        robot.keyRelease(KeyEvent.VK_END);

        robot.delay(300);

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_F);

        robot.delay(150);

        robot.keyRelease(KeyEvent.VK_F);
        robot.keyRelease(KeyEvent.VK_CONTROL);

        robot.delay(500);

        printWordWithRobot(robot, "daily");
        robot.delay(300);

        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);

        robot.delay(1000);

        BufferedImage screenshot = robot.createScreenCapture(new Rectangle(0, 0, 1920, 1080));
        robot.delay(300);

        boolean pixelFound = false;
        int pixelX = 0;
        int pixelY = 0;

        for (int y = 0; y < screenshot.getHeight(); y++) {
            if (pixelFound) break;
            for (int x = 52; x < screenshot.getWidth(); x++) {
                if (pixelFound) break;
                int color = screenshot.getRGB(x, y);

                int red = (color & 0x00ff0000) >> 16;
                int green = (color & 0x0000ff00) >> 8;
                int blue = color & 0x000000ff;

                if (red == 255 && green == 150 && blue == 50) {
                    pixelFound = true;
                    pixelX = x + 20;
                    pixelY = y + 150;
                    robot.mouseMove(pixelX, pixelY);
                    robot.delay(200);
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                }
            }
        }
        robot.delay(5000);
        closeTabWithRobot(robot);

        pixelX += 920;

        robot.mouseMove(pixelX, pixelY);
        robot.delay(200);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(5000);
        closeTabWithRobot(robot);

        pixelX += 450;

        robot.mouseMove(pixelX, pixelY);
        robot.delay(200);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(5000);
        closeTabWithRobot(robot);
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

    public static Map<Integer, String> getRobotCodes() {
        List<String> lines = getWordsFromTxt(ROBOT_CODES_TXT_PATH);
        Map<Integer, String> robotCodes = new HashMap<>();

        for (String line : lines) {
            String[] parts = line.split(" -- ");
            robotCodes.put(Integer.parseInt(parts[0]), parts[1]);
        }

        return robotCodes;
    }

    public static void printWordWithRobot(Robot robot, String word) {
        Map<Integer, String> robotCodes = getRobotCodes();
        Random random = new Random();
        for (char c : word.toCharArray()) {
            for (int keyCode : robotCodes.keySet()) {
                if (robotCodes.get(keyCode).toLowerCase().equals(String.valueOf(c))) {
                    if (keyCode == 513) {
                        robot.keyPress(KeyEvent.VK_SHIFT);
                        robot.keyPress(KeyEvent.VK_SEMICOLON);
                        robot.keyRelease(KeyEvent.VK_SEMICOLON);
                        robot.keyRelease(KeyEvent.VK_SHIFT);
                        break;
                    }
                    robot.keyPress(keyCode);
                    robot.delay(random.nextInt(100, 300));
                    robot.keyRelease(keyCode);
                    robot.delay(random.nextInt(100, 300));
                    break;
                }
            }
        }
    }

    public static void printWordInstantlyWithRobot(Robot robot, String word) {
        Map<Integer, String> robotCodes = getRobotCodes();
        for (char c : word.toCharArray()) {
            for (int keyCode : robotCodes.keySet()) {
                if (robotCodes.get(keyCode).toLowerCase().equals(String.valueOf(c))) {
                    if (keyCode == 513) {
                        robot.keyPress(KeyEvent.VK_SHIFT);
                        robot.keyPress(KeyEvent.VK_SEMICOLON);
                        robot.keyRelease(KeyEvent.VK_SEMICOLON);
                        robot.keyRelease(KeyEvent.VK_SHIFT);
                        break;
                    }
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    break;
                }
            }
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
