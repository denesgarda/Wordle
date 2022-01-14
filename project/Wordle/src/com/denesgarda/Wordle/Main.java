package com.denesgarda.Wordle;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class Main {
    public static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    public static String breaker = "----------------------------------------";

    public static void main(String[] args) {
        System.out.println("Loading words...");
        int wordAmount = 0;
        ArrayList<String> words = new ArrayList<>();
        try {
            URLConnection connection = new URL("https://raw.githubusercontent.com/first20hours/google-10000-english/master/20k.txt").openConnection();
            //URLConnection connection = new URL("https://raw.githubusercontent.com/dwyl/english-words/master/words.txt").openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            while (scanner.hasNext()) {
                String word = scanner.nextLine();
                if (word.matches("[a-zA-Z]+")) {
                    words.add(word.toLowerCase());
                    wordAmount++;
                }
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Failed to access library...");
            System.exit(0);
        }
        System.out.println(wordAmount + " words successfully loaded");
        printBreaker();
        try {
            menu:
            while (true) {
                System.out.println("Wordle Main Menu\n[1] Play\n[~] Quit");
                String menuInput = in.readLine();
                if (menuInput.equalsIgnoreCase("1")) {
                    printBreaker();
                    game:
                    while (true) {
                        ArrayList<String> applicable = new ArrayList<>();
                        for (String word : words) {
                            if (word.length() == 5 && !duplicates(word.toCharArray())) {
                                applicable.add(word);
                            }
                        }
                        int t = 1;
                        int totalTries = 6;
                        String word = applicable.get(new Random().nextInt(applicable.size()));
                        System.out.println("Start guessing. You have " + totalTries + " tries\n[~] Exit / Forfeit");
                        guess:
                        while (true) {
                            if (t <= totalTries) {
                                printTry(t);
                                String guess;
                                input:
                                while (true) {
                                    guess = in.readLine();
                                    if (guess.equalsIgnoreCase("~")) {
                                        t = totalTries;
                                        guess = "     ";
                                        break input;
                                    }
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
                                    printlnColor("Top Definition: " + getDefinition(word), Color.ANSI_WHITE);
                                    printBreaker();
                                    break game;
                                }
                                t++;
                            } else {
                                printBreaker();
                                printlnColor("YOU LOSE!", Color.ANSI_RED);
                                System.out.println("The word was: " + word);
                                printlnColor("Top Definition: " + getDefinition(word), Color.ANSI_WHITE);
                                printBreaker();
                                break game;
                            }
                        }
                    }
                } else if (menuInput.equalsIgnoreCase("~")) {
                    printBreaker();
                    System.out.println("Thank you for playing!");
                    printBreaker();
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

    public static void printTry(int t) {
        String s = "Try " + t;
        System.out.println(breaker.substring(0, breaker.length() - s.length()) + s);
    }

    public static void invalid() {
        System.out.println(Color.ANSI_RED + "Invalid Input" + Color.ANSI_RESET);
    }

    public static void printlnColor(String s, String color) {
        System.out.println(color + s + Color.ANSI_RESET);
    }

    public static boolean duplicates(final char[] array) {
        Set<Character> lump = new HashSet<Character>();
        for (char i : array) {
            if (lump.contains(i)) return true;
            lump.add(i);
        }
        return false;
    }

    public static String getDefinition(String word) {
        try {
            URLConnection connection = new URL("https://api.dictionaryapi.dev/api/v2/entries/en/" + word).openConnection();
            connection.setConnectTimeout(5000);
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            String line = scanner.nextLine();
            JSONArray all = (JSONArray) new JSONParser().parse(line);
            JSONArray meanings = (JSONArray) ((JSONObject) all.get(0)).get("meanings");
            JSONArray definitions = (JSONArray) ((JSONObject) meanings.get(0)).get("definitions");
            return (String) ((JSONObject) definitions.get(0)).get("definition");
        } catch (Exception e) {
            return "Could not get definition";
        }
    }
}
