package dev.mkomarov;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import static dev.mkomarov.RobotController.*;
import static dev.mkomarov.ScreenController.findPixel;

public class FirefoxController implements BrowserController {
    public static final String LAUNCH_FIREFOX_COMMAND;
    private static final String OS = System.getProperty("os.name").toLowerCase();

    private static final Pixel.Color SEARCH_SELECTION_COLOR = new Pixel.Color(255, 150, 50);

    static {
        if (OS.contains("win")) {
            LAUNCH_FIREFOX_COMMAND = "cmd /c start firefox";
        } else if (OS.contains("nix") || OS.contains("nux") || OS.contains("mac")) {
            LAUNCH_FIREFOX_COMMAND = "firefox";
        } else {
            throw new RuntimeException("Unsupported operating system");
        }
    }

    @Override
    public void launchBrowser() throws IOException {
        Runtime.getRuntime().exec(LAUNCH_FIREFOX_COMMAND);
    }

    @Override
    public void closeBrowser() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void createNewTab() {
        getRobot().keyPress(KeyEvent.VK_CONTROL);
        getRobot().keyPress(KeyEvent.VK_T);
        getRobot().keyRelease(KeyEvent.VK_T);
        getRobot().keyRelease(KeyEvent.VK_CONTROL);
    }

    @Override
    public void closeTab() {
        getRobot().keyPress(KeyEvent.VK_CONTROL);
        getRobot().keyPress(KeyEvent.VK_W);
        getRobot().keyRelease(KeyEvent.VK_W);
        getRobot().keyRelease(KeyEvent.VK_CONTROL);
    }

    @Override
    public void navigateTo(String url) {
        printWordInstantlyWithRobot(url);
    }

    @Override
    public Pixel searchOnPage(String text) {
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

        Pixel pixelFound = findPixel(SEARCH_SELECTION_COLOR, 52, 0);

        if (pixelFound == null) throw new RuntimeException("Pixel not found");
        return pixelFound;
    }

    @Override
    public void doDailySites() {

    }

    @Override
    public void doDailySearches(int amount) {

    }
}
