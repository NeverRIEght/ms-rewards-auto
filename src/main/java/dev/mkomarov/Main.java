package dev.mkomarov;

import dev.mkomarov.browser.BrowserController;
import dev.mkomarov.keyboard.KeyboardController;
import dev.mkomarov.mouse.Direction;
import dev.mkomarov.mouse.MouseController;
import dev.mkomarov.phone.PhoneController;
import dev.mkomarov.screen.Color;
import dev.mkomarov.screen.Pixel;
import dev.mkomarov.screen.ScreenController;
import dev.mkomarov.search.SearchController;
import dev.mkomarov.terminal.TerminalController;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
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

    public static final BrowserController browserController = BrowserController.getInstance();
    public static final KeyboardController keyboardController = KeyboardController.getInstance();
    public static final MouseController mouseController = MouseController.getInstance();
    public static final ScreenController screenController = ScreenController.getInstance();
    public static final PhoneController phoneController = PhoneController.getInstance();
    public static final SearchController searchController = SearchController.getInstance();

    public static void main(String[] args) throws InterruptedException {
        Thread daemon = TerminalController.startYdotoolDaemon();
        try {
            browserController.launchBrowser();
            TimeUnit.MILLISECONDS.sleep(2000);
            browserController.createNewTab();
            TimeUnit.MILLISECONDS.sleep(200);
            browserController.navigateTo("https://rewards.bing.com/");
            TimeUnit.MILLISECONDS.sleep(5000);

            browserController.doDailySites();
            browserController.doDailySearches(30);

            browserController.closeBrowser();

            phoneController.launchSession();

            phoneController.doDaily();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            phoneController.closeSession();
            daemon.interrupt();
            daemon.join();
        }
    }
}
