package logic;

import gui.WindowMain;

public class Main {

    private static final boolean LOG = true;

    public static void printLog(String text) {
        if(LOG)
            System.out.println(text);
    }

    public static void main(String[] args) {
        ApplicationManager.start();
    }
}
