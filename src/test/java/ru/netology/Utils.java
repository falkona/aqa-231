package ru.netology;

import java.time.LocalDate;
import java.time.ZoneId;

public class Utils {
    private Utils() {}



    static void sleep(int sec) {
        try {
            Thread.sleep(sec*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
