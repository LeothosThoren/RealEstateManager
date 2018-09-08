package com.openclassrooms.realestatemanager.utils;

public class HelperSingleton {

    private static final HelperSingleton ourInstance = new HelperSingleton();
    private int viewVisibility;
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

    public int getViewVisibility() {
        return viewVisibility;
    }

    public void setViewVisibility(int viewVisibility) {
        this.viewVisibility = viewVisibility;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
