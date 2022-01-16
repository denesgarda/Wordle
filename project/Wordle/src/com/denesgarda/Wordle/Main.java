package com.denesgarda.Wordle;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.*;

public class Main {
    public static double VERSION = 1.3;

    public static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    public static String breaker = "----------------------------------------";

    public static void main(String[] args) {
        System.out.println("Checking for update...");
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://raw.githubusercontent.com/DenDen747/Wordle/main/nv.txt").openConnection();
            connection.setConnectTimeout(5000);
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            String line = scanner.nextLine();
            double version = Double.parseDouble(line.split(",")[0]);
            boolean mandatory = Boolean.parseBoolean(line.split(",")[1]);
            String link = line.split(",")[2];
            if (VERSION < version) {
                if (mandatory) {
                    System.out.println("You are using an outdated version! This is a mandatory update.\n\n[ENTER] Download new version\n[~] Exit");
                    String choice = in.readLine();
                    if (!choice.equalsIgnoreCase("~")) {
                        try {
                            Desktop.getDesktop().browse(new URI(link));
                            System.exit(0);
                        } catch (Exception e) {
                            System.out.println("Failed to open link in default browser. Please go to the following link:\n" + link);
                        }
                    } else {
                        System.exit(0);
                    }
                } else {
                    System.out.println("You are using an outdated version!\n\n[ENTER] Download new version\n[~] Continue anyway (not recommended)");
                    String choice = in.readLine();
                    if (!choice.equalsIgnoreCase("~")) {
                        try {
                            Desktop.getDesktop().browse(new URI(link));
                            System.exit(0);
                        } catch (Exception e) {
                            System.out.println("Failed to open link in default browser. Please go to the following link:\n" + link);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to connect, ignoring...");
            printBreaker();
            System.out.println("For an improved experience, please connect to the internet.\n[ENTER] Dismiss / continue");
            try {
                in.readLine();
            } catch (Exception e2) {
                System.out.println("ERROR OCCURRED. PLEASE RELAUNCH!");
                System.exit(-1);
            }
        }
        System.out.println("Loading words...");
        int wordAmount = 0;
        ArrayList<String> words = new ArrayList<>();
        ArrayList<String> bank = new ArrayList<>();
        try {
            //https://raw.githubusercontent.com/first20hours/google-10000-english/master/20k.txt
            Scanner scanner = new Scanner(Main.class.getResourceAsStream("/data/en-us/common.txt"));
            scanner.useDelimiter("\\Z");
            while (scanner.hasNext()) {
                String word = scanner.nextLine();
                if (word.matches("[a-zA-Z]+")) {
                    words.add(word.toLowerCase());
                    bank.add(word.toLowerCase());
                    wordAmount++;
                }
            }
            scanner.close();
            //https://raw.githubusercontent.com/dwyl/english-words/master/words.txt
            Scanner scanner2 = new Scanner(Main.class.getResourceAsStream("/data/en-us/all.txt"));
            scanner2.useDelimiter("\\Z");
            while (scanner2.hasNext()) {
                String word = scanner2.nextLine();
                if (word.matches("[a-zA-Z]+")) {
                    bank.add(word.toLowerCase());
                    wordAmount++;
                }
            }
            scanner2.close();
        } catch (Exception e) {
            System.out.println("Failed to load words");
            System.exit(0);
        }
        System.out.println(wordAmount + " words successfully loaded");
        printBreaker();
        try {
            menu:
            while (true) {
                System.out.println("Wordle Main Menu\n[1] Play\n[2] How to play\n[3] Github\n[~] Quit");
                String menuInput = in.readLine();
                if (menuInput.equalsIgnoreCase("1")) {
                    printBreaker();
                    config:
                    while (true) {
                        System.out.println("Config:\n\nWord length: " + Config.wordLength + "\nRepeat letters: " + Config.repeatLetters + "\nHard mode: " + Config.hardMode + "\n\n[E] Edit config\n[ENTER] Start game");
                        String configInput = in.readLine();
                        if (configInput.equalsIgnoreCase("E")) {
                            property:
                            while (true) {
                                printBreaker();
                                System.out.println("Select a property to edit\n\n[1] Word length\n[2] Repeat letters\n[3] Hard mode\n[R] Reset all properties\n[ENTER] Cancel");
                                String propertyInput = in.readLine();
                                if (propertyInput.equalsIgnoreCase("1")) {
                                    printBreaker();
                                    value:
                                    while (true) {
                                        System.out.print("Enter new value (3 - 10)\n" + Config.wordLength + " -> ");
                                        String valueInput = in.readLine();
                                        try {
                                            int value = Integer.parseInt(valueInput);
                                            if (value >= 3 && value <= 10) {
                                                Config.wordLength = value;
                                                printBreaker();
                                                break property;
                                            } else {
                                                printlnColor("Invalid input", Color.ANSI_RED);
                                            }
                                        } catch (Exception e) {
                                            printlnColor("Invalid input", Color.ANSI_RED);
                                        }
                                    }
                                } else if (propertyInput.equalsIgnoreCase("2")) {
                                    printBreaker();
                                    value:
                                    while (true) {
                                        System.out.print("Enter new value (true / false)\n" + Config.repeatLetters + " -> ");
                                        String valueInput = in.readLine();
                                        if (valueInput.equalsIgnoreCase("true") || valueInput.equalsIgnoreCase("false")) {
                                            Config.repeatLetters = Boolean.parseBoolean(valueInput);
                                            printBreaker();
                                            break property;
                                        } else {
                                            printlnColor("Invalid input", Color.ANSI_RED);
                                        }
                                    }
                                } else if (propertyInput.equalsIgnoreCase("3")) {
                                    printBreaker();
                                    value:
                                    while (true) {
                                        System.out.print("Enter new value (true / false)\n" + Config.hardMode + " -> ");
                                        String valueInput = in.readLine();
                                        if (valueInput.equalsIgnoreCase("true") || valueInput.equalsIgnoreCase("false")) {
                                            Config.hardMode = Boolean.parseBoolean(valueInput);
                                            printBreaker();
                                            break property;
                                        } else {
                                            printlnColor("Invalid input", Color.ANSI_RED);
                                        }
                                    }
                                } else if (propertyInput.equalsIgnoreCase("R")) {
                                    printBreaker();
                                    Config.reset();
                                    System.out.println("Reset all properties");
                                    printBreaker();
                                    break property;
                                } else {
                                    printBreaker();
                                    break property;
                                }
                            }
                        } else {
                            printBreaker();
                            game:
                            while (true) {
                                System.out.print("Loading...");
                                ArrayList<String> applicable = new ArrayList<>();
                                if (Config.hardMode) {
                                    for (String word : bank) {
                                        if (Config.repeatLetters) {
                                            if (word.length() == Config.wordLength) {
                                                applicable.add(word);
                                            }
                                        } else {
                                            if (word.length() == Config.wordLength && !duplicates(word.toCharArray())) {
                                                applicable.add(word);
                                            }
                                        }
                                    }
                                } else {
                                    for (String word : words) {
                                        if (Config.repeatLetters) {
                                            if (word.length() == Config.wordLength) {
                                                applicable.add(word);
                                            }
                                        } else {
                                            if (word.length() == Config.wordLength && !duplicates(word.toCharArray())) {
                                                applicable.add(word);
                                            }
                                        }
                                    }
                                }
                                int t = 1;
                                int totalTries = Config.getTries();
                                String definition;
                                String word;
                                if (Config.hardMode) {
                                    word = applicable.get(new Random().nextInt(applicable.size()));
                                    definition = getDefinition(word);
                                } else {
                                    do {
                                        word = applicable.get(new Random().nextInt(applicable.size()));
                                        definition = getDefinition(word);
                                    } while (definition.equalsIgnoreCase("No definition available"));
                                }
                                System.out.print("\b\b\b\b\b\b\b\b\b\b");
                                System.out.println("Guess the " + Config.wordLength + " letter word. You have " + totalTries + " tries\n[~] Exit / Forfeit");
                                guess:
                                while (true) {
                                    if (t <= totalTries) {
                                        printTry(t);
                                        String guess;
                                        input:
                                        while (true) {
                                            guess = in.readLine();
                                            guess = guess.toLowerCase();
                                            if (guess.equalsIgnoreCase("~")) {
                                                t = totalTries;
                                                guess = "";
                                                for (int i = 0; i < Config.wordLength; i++) {
                                                    guess += " ";
                                                }
                                                break input;
                                            }
                                            if (guess.length() > word.length()) {
                                                printlnColor("Too long", Color.ANSI_RED);
                                            } else if (guess.length() < word.length()) {
                                                printlnColor("Too short", Color.ANSI_RED);
                                            } else if (!(guess.matches("[a-zA-Z]+") && (bank.contains(guess) || bank.contains(guess)))) {
                                                printlnColor("Not in word bank", Color.ANSI_RED);
                                            } else {
                                                break input;
                                            }
                                        }
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
                                            printlnColor("Top Definition: " + definition, Color.ANSI_WHITE);
                                            System.out.println("[ENTER] Continue");
                                            in.readLine();
                                            printBreaker();
                                            break config;
                                        }
                                        t++;
                                    } else {
                                        printBreaker();
                                        printlnColor("YOU LOSE!", Color.ANSI_RED);
                                        System.out.println("The word was: " + word);
                                        printlnColor("Top Definition: " + definition, Color.ANSI_WHITE);
                                        System.out.println("[ENTER] Continue");
                                        in.readLine();
                                        printBreaker();
                                        break config;
                                    }
                                }
                            }
                        }
                    }
                } else if (menuInput.equalsIgnoreCase("2")) {
                    printBreaker();
                    System.out.println("How to play Wordle\nYou have to try guessing a word using other words that are the same length with a set amount of tries.");
                    printlnColor("Yellow means the letter is in the word but not in the right spot.", Color.ANSI_YELLOW);
                    printlnColor("Green means the letter is in the word and in the right place.", Color.ANSI_GREEN);
                    System.out.println("[ENTER] Continue");
                    in.readLine();
                    printBreaker();
                } else if (menuInput.equalsIgnoreCase("3")) {
                    printBreaker();
                    try {
                        Desktop.getDesktop().browse(new URI("https://www.github.com/DenDen747/Wordle/"));
                        System.out.println("Opened Github page in default browser");
                    } catch (Exception e) {
                        System.out.println("Could not open Github in default browser");
                    }
                    printBreaker();
                } else if (menuInput.equalsIgnoreCase("~")) {
                    printBreaker();
                    System.out.println("Thank you for playing!");
                    printBreaker();
                    break menu;
                } else {
                    printlnColor("Invalid input", Color.ANSI_RED);
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

    public static void printlnColor(String s, String color) {
        System.out.println(color + s + Color.ANSI_RESET);
    }

    public static boolean duplicates(final char[] array) {
        Set<Character> lump = new HashSet<>();
        for (char i : array) {
            if (lump.contains(i)) return true;
            lump.add(i);
        }
        return false;
    }

    public static String getDefinition(String word) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://api.dictionaryapi.dev/api/v2/entries/en/" + word).openConnection();
            connection.setConnectTimeout(5000);
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            String line = scanner.nextLine();
            JSONArray all = (JSONArray) new JSONParser().parse(line);
            JSONArray meanings = (JSONArray) ((JSONObject) all.get(0)).get("meanings");
            JSONArray definitions = (JSONArray) ((JSONObject) meanings.get(0)).get("definitions");
            return (String) ((JSONObject) definitions.get(0)).get("definition");
        } catch (ParseException | FileNotFoundException e) {
            return "No definition available";
        } catch (Exception e) {
            return "Unable to get definition";
        }
    }
}
