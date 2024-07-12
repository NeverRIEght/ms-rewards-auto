package dev.mkomarov.phone;

public interface PhoneController {
    public void launchSession();
    public void closeSession();
    public void openApp(String appName);
    public void doDaily();
    public void collectDailyBonus();
    public void doDailySearches(int amount);
    public void doDailyNews();
}
