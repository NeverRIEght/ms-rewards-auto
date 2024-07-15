package dev.mkomarov.browser;

import dev.mkomarov.screen.Pixel;

import java.io.IOException;

public interface BrowserController {
    static BrowserController getInstance() {
        return new FirefoxController();
    }

    void launchBrowser() throws IOException;

    void closeBrowser() throws IOException;

    void createNewTab();

    void closeTab();

    void navigateTo(String url);

    Pixel searchOnPage(String text);

    void doDailySites();

    void doDailySearches(int amount);
}
