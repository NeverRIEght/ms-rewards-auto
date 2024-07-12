package dev.mkomarov.phone;

import dev.mkomarov.screen.Pixel;

public interface PhoneController {
    public void launchSession();
    public void closeSession();
    public void openApp(String appName);
    public void doDaily();
    public void collectDailyBonus();
    public void doDailySearches(Pixel screenCenter, int amount);
    public void doDailyNews();
}
