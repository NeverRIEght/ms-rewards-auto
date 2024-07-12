package dev.mkomarov;

import dev.mkomarov.browser.BrowserController;
import dev.mkomarov.browser.FirefoxController;
import dev.mkomarov.keyboard.KeyboardController;
import dev.mkomarov.keyboard.KeyboardControllerWayland;
import dev.mkomarov.mouse.Direction;
import dev.mkomarov.mouse.MouseController;
import dev.mkomarov.mouse.MouseControllerWayland;
import dev.mkomarov.phone.PhoneController;
import dev.mkomarov.phone.WaydroidController;
import dev.mkomarov.screen.Color;
import dev.mkomarov.screen.Pixel;
import dev.mkomarov.screen.ScreenController;
import dev.mkomarov.screen.ScreenControllerWayland;
import dev.mkomarov.search.SearchController;
import dev.mkomarov.search.SearchControllerImpl;
import dev.mkomarov.terminal.TerminalController;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static dev.mkomarov.robot.RobotController.*;
import static dev.mkomarov.screen.ScreenControllerRobot.*;

public class Main {
    //dependencies:
    //ydotool - mouse and keyboard automation
    //ydotoold - backend for ydotool
    //gnome-screenshot - screenshot tool

    public static final String SEP = File.separator;

    public static final String ROOT_PASSWORD = "farout";

    public static final BrowserController browserController = new FirefoxController();
    public static final KeyboardController keyboardController = new KeyboardControllerWayland();
    public static final MouseController mouseController = new MouseControllerWayland();
    public static final ScreenController screenController = new ScreenControllerWayland();
    public static final PhoneController phoneController = new WaydroidController();
    public static final SearchController searchController = new SearchControllerImpl();

    public static void main(String[] args) throws IOException, InterruptedException {
        Thread daemon = TerminalController.startYdotoolDaemon();
        try {
//        browserController.launchBrowser();
//        TimeUnit.MILLISECONDS.sleep(2000);
//        browserController.createNewTab();
//        TimeUnit.MILLISECONDS.sleep(200);
//        browserController.navigateTo("https://rewards.bing.com/");
//        TimeUnit.MILLISECONDS.sleep(5000);
//
//        browserController.doDailySites();
//        browserController.doDailySearches(30);
//
//            phoneController.launchSession();
//
//            Thread.sleep(1600);
//            Color black = new Color(0, 0, 0);
//            Pixel bottomRightCorner = screenController.findLastPixel(black);
//
//            Pixel[] corners = screenController.findBorder(bottomRightCorner, black);
//            Pixel middle = screenController.findMiddlePixel(corners[0], corners[1]);
//
//            Thread.sleep(10000);
//            phoneController.openApp("com.microsoft.bing");
//            Thread.sleep(3000);
//
//            mouseController.mouseMove(middle.getX(), middle.getY() - 150);
//            Thread.sleep(500);
//            mouseController.mouseClick();
//            Thread.sleep(1500);
//
//            Random random = new Random();
//            try {
//                for (int i = 0; i < 20; i++) {
//                    String currentWord = searchController.getRandomWord();
//                    TimeUnit.MILLISECONDS.sleep(random.nextInt(1000, 2000));
//                    keyboardController.print(currentWord, 150, 400);
//                    TimeUnit.MILLISECONDS.sleep(random.nextInt(300, 500));
//                    keyboardController.keyClick("enter");
//                    TimeUnit.MILLISECONDS.sleep(random.nextInt(2000, 3000));
//                    keyboardController.keyClick("esc");
//                    mouseController.mouseMove(middle.getX(), middle.getY() - 150);
//                    Thread.sleep(500);
//                    mouseController.mouseClick();
//                    Thread.sleep(1500);
//                }
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }

            // Scroll to news from top
//            Thread.sleep(1000);
//            mouseController.mouseScroll(Direction.DOWN, 2);
//            Thread.sleep(3000);

            browserController.launchBrowser();
//            Thread.sleep(500);
//            browserController.createNewTab();
//            Thread.sleep(500);
//            browserController.navigateTo("https://rewards.bing.com/");
            Thread.sleep(5000);
//            browserController.closeTab();
            browserController.closeBrowser();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            phoneController.closeSession();
            daemon.interrupt();
            daemon.join();
        }
    }

    public static void doPC() {
        List<String> words = getLinesFromTxt(WORDS_TXT_PATH);
        Random random = new Random();

        try {
            TimeUnit.MILLISECONDS.sleep(500);
            doDailySites();

            for (int i = 0; i < 30; i++) {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(1000, 2000));
                browserController.createNewTab();
                TimeUnit.MILLISECONDS.sleep(random.nextInt(300, 500));
                printWordWithRobot(words.get(random.nextInt(words.size())));
                TimeUnit.MILLISECONDS.sleep(random.nextInt(300, 500));
                robot.keyPress(KeyEvent.VK_ENTER);
                TimeUnit.MILLISECONDS.sleep(random.nextInt(2000, 3000));
                browserController.closeTab();
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

        Pixel pixelFound = findPixel(new Color(195, 53, 1), 0, 0, 1920, 1080);

        if (pixelFound == null) throw new RuntimeException("Pixel not found");

        List<Pixel> controlPixels = new ArrayList<>();

        Pixel controlPixel = new Pixel(pixelFound.getX(),
                pixelFound.getY() + 20,
                new Color(getPixelColor(pixelFound.getX(), pixelFound.getY() + 20).getColor()));

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
        browserController.createNewTab();
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

        Pixel pixelFound = findPixel(new Color(255, 150, 50), 52, 0);

        if (pixelFound == null) throw new RuntimeException("Pixel not found");
        int pixelX = pixelFound.getX() + 20;
        int pixelY = pixelFound.getY() + 150;

        robot.mouseMove(pixelX, pixelY);
        robot.delay(200);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        robot.delay(5000);
        browserController.closeTab();

        pixelX += 920;

        robot.mouseMove(pixelX, pixelY);
        robot.delay(200);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(5000);
        browserController.closeTab();

        pixelX += 450;

        robot.mouseMove(pixelX, pixelY);
        robot.delay(200);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(5000);
        browserController.closeTab();
    }
}
