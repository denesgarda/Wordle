package com.denesgarda.Wordle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class Main {
    public static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    public static String breaker = "------------------------------";

    public static void main(String[] args) {
        System.out.println("Loading words...");
        ArrayList<String> words = new ArrayList<>();
        try {
            URLConnection connection = new URL("http://www-personal.umich.edu/~jlawler/wordlist").openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            while (scanner.hasNext()) {
                words.add(scanner.nextLine());
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Failed to access library...");
            System.exit(0);
        }
        System.out.println("Words successfully loaded");
        printBreaker();
        try {
            menu:
            while (true) {
                System.out.println("Wordle Main Menu\n[1] Play\n[2] Quit");
                String menuInput = in.readLine();
                if (menuInput.equalsIgnoreCase("1")) {
                    printBreaker();
                    game:
                    while (true) {
                        ArrayList<String> applicable = new ArrayList<>();
                        for (String word : words) {
                            if (word.length() == 5) {
                                applicable.add(word);
                            }
                        }
                        int t = 1;
                        String word = applicable.get(new Random().nextInt(applicable.size()));
                        System.out.println("Start guessing!");
                        guess:
                        while (true) {
                            printBreaker();
                            if (t <= 6) {
                                String guess = null;
                                input:
                                while (true) {
                                    guess = in.readLine();
                                    if (guess.length() == word.length() && guess.matches("[a-zA-Z]+") && words.contains(guess)) {
                                        break input;
                                    } else {
                                        invalid();
                                    }
                                }
                                guess = guess.toLowerCase();
                                String[] colors = new String[word.length()];
                                for (int i = 0; i < word.length(); i++) {
                                    if (word.contains(String.valueOf(guess.toCharArray()[i]))) {
                                        colors[i] = Color.ANSI_YELLOW;
                                    }
                                }
                                for (int i = 0; i < word.length(); i++) {
                                    if (word.toCharArray()[i] == guess.toCharArray()[i]) {
                                        colors[i] = Color.ANSI_GREEN;
                                    }
                                }
                                for (int i = 0; i < colors.length; i++) {
                                    if (colors[i] == null) {
                                        colors[i] = Color.ANSI_RESET;
                                    }
                                }
                                String output = "";
                                for (int i = 0; i < colors.length; i++) {
                                    output += colors[i];
                                    output += guess.toCharArray()[i];
                                }
                                output += Color.ANSI_RESET;
                                System.out.println(output);
                                if (guess.equalsIgnoreCase(word)) {
                                    printBreaker();
                                    printlnColor("YOU WIN!", Color.ANSI_CYAN);
                                    printBreaker();
                                    break game;
                                }
                                t++;
                            } else {
                                printlnColor("YOU LOSE!", Color.ANSI_RED);
                                System.out.println("The word was: " + word);
                                printBreaker();
                                break game;
                            }
                        }
                    }
                } else if (menuInput.equalsIgnoreCase("2")) {
                    printBreaker();
                    System.out.println("Thank you for playing!");
                    break menu;
                } else {
                    invalid();
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            e.printStackTrace();
        }
    }

    public static void printBreaker() {
        System.out.println(breaker);
    }

    public static void invalid() {
        System.out.println(Color.ANSI_RED + "Invalid Input" + Color.ANSI_RESET);
    }

    public static void printlnColor(String s, String color) {
        System.out.println(color + s + Color.ANSI_RESET);
    }
}
