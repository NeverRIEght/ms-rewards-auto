package dev.mkomarov.browser;

import dev.mkomarov.screen.Pixel;

import java.io.IOException;

public interface BrowserController {
    public void launchBrowser() throws IOException;

    public void closeBrowser() throws IOException;

    public void createNewTab();

    public void closeTab();

    public void navigateTo(String url);

    public Pixel searchOnPage(String text);

    public void doDailySites();

    public void doDailySearches(int amount);
}
