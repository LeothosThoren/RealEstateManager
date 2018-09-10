package com.openclassrooms.realestatemanager.utils;

public class HelperSingleton {

    private static final HelperSingleton ourInstance = new HelperSingleton();
    private boolean isVisible;
    private int mode;
    private int position;

    private HelperSingleton() {
    }

    public static HelperSingleton getInstance() {
        return ourInstance;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
