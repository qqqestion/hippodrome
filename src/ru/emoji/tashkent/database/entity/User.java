package ru.emoji.tashkent.database.entity;

import java.util.regex.Pattern;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private int birthYear;
    private String email;
    private String password;
    private boolean isAdmin;

    public User() {}

    public User(int id, String firstName, String lastName, int birthYear, String email, String password, boolean isAdmin) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthYear = birthYear;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public User(String firstName, String lastName, int birthYear, String email, String password, boolean isAdmin) {
        this(-1, firstName, lastName, birthYear, email, password, isAdmin);
    }

    @Override
    public String toString() {
        return firstName + ' ' + lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public boolean isFirstNameValid() {
        return true;
    }
    public boolean isLastNameValid() {
        return true;
    }
    public boolean isEmailNameValid() {
        return Pattern.matches("[0-9A-Za-z._-]+@[0-9A-Za-z._-]+\\.[0-9A-Za-z._-]+", email);
    }
    public boolean isPasswordNameValid() {
        return true;
    }
    public boolean isBirthNameValid() {
        return birthYear >= 1920 && birthYear <= 2020;
    }

    public boolean isValid() {
        return isFirstNameValid() && isLastNameValid() && isEmailNameValid() && isPasswordNameValid() && isBirthNameValid();
    }
}
