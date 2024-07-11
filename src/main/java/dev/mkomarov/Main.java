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


//            keyboardController.keyClick(2);

            String str = "esc escape 1\n" +
                    "1 2\n" +
                    "2 3\n" +
                    "3 4\n" +
                    "4 5\n" +
                    "5 6\n" +
                    "6 7\n" +
                    "7 8\n" +
                    "8 9\n" +
                    "9 10\n" +
                    "10 11\n" +
                    "minus - 12\n" +
                    "equal = 13\n" +
                    "backspace erase 14\n" +
                    "tab 15\n" +
                    "q 16\n" +
                    "w 17\n" +
                    "e 18\n" +
                    "r 19\n" +
                    "KEY_T 20\n" +
                    "KEY_Y 21\n" +
                    "KEY_U 22\n" +
                    "KEY_I 23\n" +
                    "KEY_O 24\n" +
                    "KEY_P 25\n" +
                    "KEY_LEFTBRACE 26\n" +
                    "KEY_RIGHTBRACE 27\n" +
                    "KEY_ENTER 28\n" +
                    "KEY_LEFTCTRL 29\n" +
                    "KEY_A 30\n" +
                    "KEY_S 31\n" +
                    "KEY_D 32\n" +
                    "KEY_F 33\n" +
                    "KEY_G 34\n" +
                    "KEY_H 35\n" +
                    "KEY_J 36\n" +
                    "KEY_K 37\n" +
                    "KEY_L 38\n" +
                    "KEY_SEMICOLON 39\n" +
                    "KEY_APOSTROPHE 40\n" +
                    "KEY_GRAVE 41\n" +
                    "KEY_LEFTSHIFT 42\n" +
                    "KEY_BACKSLASH 43\n" +
                    "KEY_Z 44\n" +
                    "KEY_X 45\n" +
                    "KEY_C 46\n" +
                    "KEY_V 47\n" +
                    "KEY_B 48\n" +
                    "KEY_N 49\n" +
                    "KEY_M 50\n" +
                    "KEY_COMMA 51\n" +
                    "KEY_DOT 52\n" +
                    "KEY_SLASH 53\n" +
                    "KEY_RIGHTSHIFT 54\n" +
                    "KEY_KPASTERISK 55\n" +
                    "KEY_LEFTALT 56\n" +
                    "KEY_SPACE 57\n" +
                    "KEY_CAPSLOCK 58\n" +
                    "KEY_F1 59\n" +
                    "KEY_F2 60\n" +
                    "KEY_F3 61\n" +
                    "KEY_F4 62\n" +
                    "KEY_F5 63\n" +
                    "KEY_F6 64\n" +
                    "KEY_F7 65\n" +
                    "KEY_F8 66\n" +
                    "KEY_F9 67\n" +
                    "KEY_F10 68\n" +
                    "KEY_NUMLOCK 69\n" +
                    "KEY_SCROLLLOCK 70\n" +
                    "KEY_KP7 71\n" +
                    "KEY_KP8 72\n" +
                    "KEY_KP9 73\n" +
                    "KEY_KPMINUS 74\n" +
                    "KEY_KP4 75\n" +
                    "KEY_KP5 76\n" +
                    "KEY_KP6 77\n" +
                    "KEY_KPPLUS 78\n" +
                    "KEY_KP1 79\n" +
                    "KEY_KP2 80\n" +
                    "KEY_KP3 81\n" +
                    "KEY_KP0 82\n" +
                    "KEY_KPDOT 83\n" +
                    "\n" +
                    "KEY_ZENKAKUHANKAKU 85\n" +
                    "KEY_102ND 86\n" +
                    "KEY_F11 87\n" +
                    "KEY_F12 88\n" +
                    "KEY_RO 89\n" +
                    "KEY_KATAKANA 90\n" +
                    "KEY_HIRAGANA 91\n" +
                    "KEY_HENKAN 92\n" +
                    "KEY_KATAKANAHIRAGANA 93\n" +
                    "KEY_MUHENKAN 94\n" +
                    "KEY_KPJPCOMMA 95\n" +
                    "KEY_KPENTER 96\n" +
                    "KEY_RIGHTCTRL 97\n" +
                    "KEY_KPSLASH 98\n" +
                    "KEY_SYSRQ 99\n" +
                    "KEY_RIGHTALT 100\n" +
                    "KEY_LINEFEED 101\n" +
                    "KEY_HOME 102\n" +
                    "KEY_UP 103\n" +
                    "KEY_PAGEUP 104\n" +
                    "KEY_LEFT 105\n" +
                    "KEY_RIGHT 106\n" +
                    "KEY_END 107\n" +
                    "KEY_DOWN 108\n" +
                    "KEY_PAGEDOWN 109\n" +
                    "KEY_INSERT 110\n" +
                    "KEY_DELETE 111\n" +
                    "KEY_MACRO 112\n" +
                    "KEY_MUTE 113\n" +
                    "KEY_VOLUMEDOWN 114\n" +
                    "KEY_VOLUMEUP 115\n" +
                    "KEY_POWER 116 /* SC System Power Down */\n" +
                    "KEY_KPEQUAL 117\n" +
                    "KEY_KPPLUSMINUS 118\n" +
                    "KEY_PAUSE 119\n" +
                    "KEY_SCALE 120 /* AL Compiz Scale (Expose) */\n" +
                    "\n" +
                    "KEY_KPCOMMA 121\n" +
                    "KEY_HANGEUL 122\n" +
                    "KEY_HANGUEL KEY_HANGEUL\n" +
                    "KEY_HANJA 123\n" +
                    "KEY_YEN 124\n" +
                    "KEY_LEFTMETA 125\n" +
                    "KEY_RIGHTMETA 126\n" +
                    "KEY_COMPOSE 127";

            System.out.println(str);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            phoneController.closeSession();
            daemon.interrupt();
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
