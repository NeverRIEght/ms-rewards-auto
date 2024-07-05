package dev.mkomarov;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class RobotController {

    private RobotController() {
        throw new IllegalStateException("Utility class");
    }

    public static final Robot robot;

    static {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

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

    public static Robot getRobot() {
        return robot;
    }

    public static Map<Integer, String> getRobotCodes() {
        List<String> lines = getLinesFromTxt(ROBOT_CODES_TXT_PATH);
        Map<Integer, String> robotCodes = new HashMap<>();

        for (String line : lines) {
            String[] parts = line.split(" -- ");
            robotCodes.put(Integer.parseInt(parts[0]), parts[1]);
        }

        return robotCodes;
    }

    public static List<String> getLinesFromTxt(String path) {
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

    public static void printWordWithRobot(String word) {
        printWordWithRobot(word, 100, 300);
    }

    public static void printWordInstantlyWithRobot(String word) {
        printWordWithRobot(word, 0, 1);
    }

    public static void printWordWithRobot(String word, int delayMin, int delayMax) {
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
                    robot.delay(random.nextInt(delayMin, delayMax));
                    robot.keyRelease(keyCode);
                    robot.delay(random.nextInt(delayMin, delayMax));
                    break;
                }
            }
        }
    }
}
