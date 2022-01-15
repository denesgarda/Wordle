package com.denesgarda.Wordle;

public class Config {
    public static int wordLength = 5;
    public static boolean repeatLetters = false;

    public static void reset() {
        wordLength = 5;
        repeatLetters = false;
    }

    public static int getTries() {
        if (wordLength == 3) {
            return 5;
        } else if (wordLength == 4) {
            return 5;
        } else if (wordLength == 5) {
            return 6;
        } else if (wordLength == 6) {
            return 8;
        } else if (wordLength == 7) {
            return 11;
        } else if (wordLength == 8) {
            return 15;
        } else if (wordLength == 9) {
            return 20;
        } else if (wordLength == 10) {
            return 26;
        } else {
            return 0;
        }
    }
}
