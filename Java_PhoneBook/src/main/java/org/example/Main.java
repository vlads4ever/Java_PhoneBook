package org.example;

import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            getPersonData(scanner);
        } catch (IncorrectAmountOfData e) {
            System.out.println(e.getMessage());
        }
    }

    public static void getPersonData(Scanner scanner) throws IncorrectAmountOfData {
        System.out.println("Please enter below person`s data separated by spaces (FIO, birthday, phone number, gender f/m):");
        String userInput = scanner.nextLine();
        String[] words = userInput.split(" ");

        if (words.length < 6) {
            throw new IncorrectAmountOfData("You entered not enough data.");
        } else if (words.length > 6) {
            throw new IncorrectAmountOfData("You entered too much information.");
        }


    }
}
