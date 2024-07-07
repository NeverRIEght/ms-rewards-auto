package dev.mkomarov.phone;

public interface PhoneController {
    public void launchSession();
    public void closeSession();
    public void openApp(String appName);
    public void doDailyNews();
    public void doDailySearches(int amount);
}
