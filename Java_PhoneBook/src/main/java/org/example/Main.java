package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);
    public static DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public static void main(String[] args) {

        try {
            String[] userData = getPersonData();
            Person person = parsePersonData(userData);
            System.out.println(person);
            saveToFile(person);
        } catch (IncorrectAmountOfDataException | IncorrectGenderException | IncorrectPhoneNumberException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String[] getPersonData() throws IncorrectAmountOfDataException {
        System.out.println("Please enter below person`s data separated by spaces (FIO, birthday, phone number, gender f/m):");
        String userInput = scanner.nextLine();
        String[] words = userInput.split(" ");

        if (words.length < 6) {
            throw new IncorrectAmountOfDataException("You entered not enough data.");
        } else if (words.length > 6) {
            throw new IncorrectAmountOfDataException("You entered too much data.");
        }
        return words;
    }

    public static Person parsePersonData(String[] userData)
            throws IncorrectPhoneNumberException, IncorrectGenderException {
        String surname = userData[0];
        String name = userData[1];
        String patronymic = userData[2];

        String strBirthday = userData[3];
        Date birthday = new Date();
        try {
            birthday = dateFormat.parse(strBirthday);
        } catch (ParseException e) {
            System.out.println("Wrong date format.");
        }

        String phoneNumber = userData[4];
        if (!phoneNumber.matches("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$"))
            throw new IncorrectPhoneNumberException("You entered wrong phone number.");

        String strGender = userData[5];
        Gender gender = Gender.f;
        if(!strGender.matches("[f|m]")) {
            throw new IncorrectGenderException("You entered wrong gender format.");
        } else if (strGender == "f") {
            gender = Gender.f;
        } else {
            gender = Gender.m;
        }
        return new Person(surname, name, patronymic, birthday, phoneNumber, gender);
    }

    public static void saveToFile(Person person) {
        String filePath = person.getSurname();
        String personLine = "<" + person.getSurname() + ">" +
                            "<" + person.getName() + ">" +
                            "<" + person.getPatronymic() + ">" +
                            "<" + dateFormat.format(person.getBirthday()) + ">" +
                            "<" + person.getPhoneNumber() + ">" +
                            "<" + person.getGender() + ">\n";

        Path path = Paths.get(filePath);
        boolean append = true;
        if (Files.notExists(path)) append = false;    // Дозапись в файл или создание нового

        try(BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(filePath, append))){
            bufferWriter.write(personLine);
            System.out.println("File was saved successfully.");
        } catch(IOException e) {
            System.out.println("File saving error.");
        }
    }
}
