package dev.mkomarov;

import javax.imageio.ImageIO;
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
    public static final Robot robot;

    static {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String SEP = File.separator;
    public static final String EDGE_PATH = "C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe";
    public static final String BLUESTACKS_PATH = "C:\\Program Files\\BlueStacks\\HD-Player.exe";
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

    public static void main(String[] args) throws IOException, InterruptedException, AWTException {

//        Runtime.getRuntime().exec(EDGE_PATH);
//        TimeUnit.MILLISECONDS.sleep(5000);
//        String[] launchBlueStacks = new String[] {
//                "C:\\Program Files\\BlueStacks_nxt\\HD-Player.exe",
//                "--instance",
//                "Rvc64",
//                "--cmd",
//                "launchApp",
//                "--package",
//                "com.microsoft.bing",
//                "--source",
//                "desktop_shortcut"
//        };
//        Runtime.getRuntime().exec(launchBlueStacks);

//        TimeUnit.MILLISECONDS.sleep(10000);
//        robot.keyPress(KeyEvent.VK_F11);
//        robot.delay(300);
//        robot.keyRelease(KeyEvent.VK_F11);


        doMobile();

//        doPC();
    }

    public static void doPC() {
        List<String> words = getLinesFromTxt(WORDS_TXT_PATH);
        Random random = new Random();

        try {
            TimeUnit.MILLISECONDS.sleep(500);
            doDailySites();

            for (int i = 0; i < 30; i++) {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(1000, 2000));
                newTabWithRobot();
                TimeUnit.MILLISECONDS.sleep(random.nextInt(300, 500));
                printWordWithRobot(words.get(random.nextInt(words.size())));
                TimeUnit.MILLISECONDS.sleep(random.nextInt(300, 500));
                robot.keyPress(KeyEvent.VK_ENTER);
                TimeUnit.MILLISECONDS.sleep(random.nextInt(2000, 3000));
                closeTabWithRobot();
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void doMobile() {
        List<String> words = getLinesFromTxt(WORDS_TXT_PATH);
        Random random = new Random();

        try {
            TimeUnit.MILLISECONDS.sleep(500);

//            doNews();

            TimeUnit.MILLISECONDS.sleep(random.nextInt(1000, 2000));
            doSwipe(960, 540, Direction.UP, 250);

            Pixel search = findImageOnScreen(getImageFromPath("C:\\Users\\LFKom\\Downloads\\search.png"));
            if (search == null) throw new RuntimeException("Pixel not found");

            robot.mouseMove(search.getX(), search.getY());
            robot.delay(20);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            TimeUnit.MILLISECONDS.sleep(random.nextInt(500, 1000));
            printWordWithRobot(words.get(random.nextInt(words.size())));
            TimeUnit.MILLISECONDS.sleep(random.nextInt(300, 500));
            robot.keyPress(KeyEvent.VK_ENTER);
            TimeUnit.MILLISECONDS.sleep(random.nextInt(2000, 3000));

            robot.mouseMove(10, 540);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            goBack();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void doNews() {
        doSwipe(960, 540, Direction.UP, 250);
        robot.delay(200);
        doSwipe(960, 540, Direction.DOWN, 50);

        Pixel pixelFound = findPixel(new Pixel.Color(195, 53, 1), 0, 0, 1920, 1080);

        if (pixelFound == null) throw new RuntimeException("Pixel not found");

        List<Pixel> controlPixels = new ArrayList<>();

        Pixel controlPixel = new Pixel(pixelFound.getX(),
                pixelFound.getY() + 20,
                new Pixel.Color(getPixelColor(pixelFound.getX(), pixelFound.getY() + 20).getColor()));

        controlPixels.add(controlPixel);

        robot.mouseMove(controlPixel.getX(), controlPixel.getY());
        robot.delay(200);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        robot.delay(5000);

        robot.delay(500);

        goBack();
        doSwipe(960, 540, Direction.UP, 250);
        doSwipe(960, 540, Direction.DOWN, 50);

//        for (int i = 0; i < 10; i++) {
//            robot.mouseMove(controlPixel.getX(), controlPixel.getY());
//            robot.delay(200);
//            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
//            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
//
//            goBack();
//            doSwipe(960, 540, Direction.UP, 250);
//            doSwipe(960, 540, Direction.DOWN, 50);
//            robot.delay(400);
//        }
    }

    public static void goBack() {
        BufferedImage capture = getImageFromPath("C:\\Users\\LFKom\\Downloads\\back1.png");
        Pixel pixelFound = findImageOnScreen(capture);
        if (pixelFound == null) {
            capture = getImageFromPath("C:\\Users\\LFKom\\Downloads\\back2.png");
            pixelFound = findImageOnScreen(capture);
        }
        if (pixelFound == null) {
            capture = getImageFromPath("C:\\Users\\LFKom\\Downloads\\home1.png");
            pixelFound = findImageOnScreen(capture);
        }
        if (pixelFound == null) {
            capture = getImageFromPath("C:\\Users\\LFKom\\Downloads\\home2.png");
            pixelFound = findImageOnScreen(capture);
        }

        if (pixelFound == null) throw new RuntimeException("Pixel not found");

        robot.mouseMove(pixelFound.getX(), pixelFound.getY());
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        robot.delay(3000);
    }

    public static Pixel findPixel(Pixel.Color color) {
        return findPixel(color, 0, 0, 1920, 1080);
    }

    public static Pixel findPixel(Pixel.Color color, int startX, int startY) {
        return findPixel(color, startX, startY, 1920, 1080);
    }

    public static Pixel findPixel(Pixel.Color color, int startX, int startY, int endX, int endY) {
        robot.delay(300);
        BufferedImage screenshot = robot.createScreenCapture(new Rectangle(startX, startY, endX, endY));
        robot.delay(300);

        Pixel pixelFound = null;

        for (int y = 0; y < screenshot.getHeight(); y++) {
            if (pixelFound != null) break;
            for (int x = 0; x < screenshot.getWidth(); x++) {
                if (pixelFound != null) break;

                int thisColor = screenshot.getRGB(x, y);
                int thisRed = Pixel.Color.getRed(thisColor);
                int thisGreen = Pixel.Color.getGreen(thisColor);
                int thisBlue = Pixel.Color.getBlue(thisColor);

                if (thisRed == color.getRed()
                        && thisGreen == color.getGreen()
                        && thisBlue == color.getBlue()) {
                    pixelFound = new Pixel(x, y, new Pixel.Color(thisColor));
                }
            }
        }

        return pixelFound;
    }

    public static Pixel.Color getPixelColor(int x, int y) {
        BufferedImage screenshot = robot.createScreenCapture(new Rectangle(x, y, 1, 1));
        robot.delay(300);
        return new Pixel.Color(screenshot.getRGB(0, 0));
    }

    public static BufferedImage getImageFromPath(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean compareImages(BufferedImage image1, BufferedImage image2) {
        if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight()) {
            return false;
        }

        for (int y = 0; y < image1.getHeight(); y++) {
            for (int x = 0; x < image1.getWidth(); x++) {
                if (image1.getRGB(x, y) != image2.getRGB(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static Pixel findImageOnScreen(BufferedImage image) {
        BufferedImage screenshot = robot.createScreenCapture(new Rectangle(0, 0, 1920, 1080));


        for (int y = 0; y < screenshot.getHeight(); y++) {
            for (int x = 0; x < screenshot.getWidth(); x++) {
                if (x + image.getWidth() > screenshot.getWidth() || y + image.getHeight() > screenshot.getHeight()) {
                    break;
                }


                BufferedImage subImage = screenshot.getSubimage(x, y, image.getWidth(), image.getHeight());
                if (compareImages(subImage, image)) {
                    return new Pixel(x + image.getWidth() / 2, y + image.getHeight() / 2, new Pixel.Color(screenshot.getRGB(x, y)));
                }
            }
        }

        return null;
    }

    public static void doSwipe(int startX, int startY, Direction direction, int amount) {
        robot.mouseMove(startX, startY);
        robot.delay(200);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        for (int i = 1; i <= amount; i++) {
            robot.delay(1);
            switch (direction) {
                case UP -> robot.mouseMove(startX, startY + i);
                case DOWN -> robot.mouseMove(startX, startY - i);
                case LEFT -> robot.mouseMove(startX - i, startY);
                case RIGHT -> robot.mouseMove(startX + i, startY);
            }
        }
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public static void doDailySites() {
        newTabWithRobot();
        printWordInstantlyWithRobot("https://rewards.bing.com/");

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

        printWordWithRobot("daily");
        robot.delay(300);

        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);

        robot.delay(1000);

        Pixel pixelFound = findPixel(new Pixel.Color(255, 150, 50), 52, 0);

        if (pixelFound == null) throw new RuntimeException("Pixel not found");
        int pixelX = pixelFound.getX() + 20;
        int pixelY = pixelFound.getY() + 150;

        robot.mouseMove(pixelX, pixelY);
        robot.delay(200);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        robot.delay(5000);
        closeTabWithRobot();

        pixelX += 920;

        robot.mouseMove(pixelX, pixelY);
        robot.delay(200);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(5000);
        closeTabWithRobot();

        pixelX += 450;

        robot.mouseMove(pixelX, pixelY);
        robot.delay(200);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(5000);
        closeTabWithRobot();
    }

    public static void newTabWithRobot() {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_T);
        robot.keyRelease(KeyEvent.VK_T);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    public static void closeTabWithRobot() {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_W);
        robot.keyRelease(KeyEvent.VK_W);
        robot.keyRelease(KeyEvent.VK_CONTROL);
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

    public static void printWordWithRobot(String word) {
        printWordWithRobot(word, 100, 300);
    }

    public static void printWordInstantlyWithRobot(String word) {
        printWordWithRobot(word, 0, 0);
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
}
