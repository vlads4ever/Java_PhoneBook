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
            int checkData = checkQuantityOfData(userData);
            if (checkData == -1) {
                throw new IncorrectAmountOfDataException("You entered not enough data.");
            } else if (checkData == -2) {
                throw new IncorrectAmountOfDataException("You entered too much data.");
            }
            Person person = parsePersonData(userData);
            saveToFile(person);
        } catch (IncorrectAmountOfDataException |
                 IncorrectGenderException |
                 IncorrectPhoneNumberException |
                 IncorrectNameFormatException |
                 IncorrectDateException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String[] getPersonData() throws IncorrectAmountOfDataException {
        System.out.println("Please enter below person`s data separated by spaces " +
                            "(FIO, birthday, phone number, gender f/m):");
        String userInput = scanner.nextLine();
        String[] words = userInput.split(" ");
        return words;
    }

    public static int checkQuantityOfData(String[] userData) {
        int quantity = userData.length;
        if (quantity < 6) {
            return -1;
        } else if (quantity > 6) {
            return -2;
        }
        return quantity;
    }

    public static Person parsePersonData(String[] userData)
            throws IncorrectPhoneNumberException, IncorrectGenderException,
                    IncorrectNameFormatException, IncorrectDateException {
        String surname = userData[0];
        String name = userData[1];
        String patronymic = userData[2];
        checkName(surname, name, patronymic);

        String strBirthday = userData[3];
        checkBirthday(strBirthday);
        Date birthday = new Date();
        try {
            birthday = dateFormat.parse(strBirthday);
        } catch (ParseException e) {
            System.out.println("Wrong date format.");
        }

        String phoneNumber = userData[4];
        checkPhoneNumber(phoneNumber);

        String strGender = userData[5];
        Gender gender = Gender.f;
        checkGender(strGender);
        switch (strGender) {
            case "f" -> gender = Gender.f;
            case "m" -> gender = Gender.m;
        }

        return new Person(surname, name, patronymic, birthday, phoneNumber, gender);
    }

    public static boolean checkName(String surname, String name, String patronymic)
            throws IncorrectNameFormatException {
        if (!surname.matches("[А-Яа-яЁёa-zA-Z]+") |
                !name.matches("[А-Яа-яЁёa-zA-Z]+") |
                !patronymic.matches("[А-Яа-яЁёa-zA-Z]+"))
            throw new IncorrectNameFormatException("You entered wrong person`s name.");
        return true;
    }

    public static boolean checkBirthday(String birthday) throws IncorrectDateException {
        if (!birthday.matches("\\d{2}\\.\\d{2}.\\d{4}"))
            throw new IncorrectDateException("You entered wrong date format.");
        return true;
    }

    public static boolean checkPhoneNumber(String phoneNumber) throws IncorrectPhoneNumberException {
        if (!phoneNumber.matches("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$"))
            throw new IncorrectPhoneNumberException("You entered wrong phone number.");
        return true;
    }

    public static boolean checkGender(String strGender) throws IncorrectGenderException {
        if(!strGender.matches("[f|m]"))
            throw new IncorrectGenderException("You entered wrong gender format.");
        return true;
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
            e.printStackTrace();
        }
    }
}
