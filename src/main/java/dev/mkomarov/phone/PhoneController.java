package dev.mkomarov.phone;

import dev.mkomarov.screen.Pixel;

public interface PhoneController {
    static PhoneController getInstance() {
        return new WaydroidController();
    }

    void launchSession();

    void closeSession();

    void openApp(String appName);

    void doDaily();

    void collectDailyBonus();

    void doDailySearches(Pixel screenCenter, int amount);

    void doDailyNews();
}
