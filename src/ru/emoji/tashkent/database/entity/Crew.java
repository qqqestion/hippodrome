package ru.emoji.tashkent.database.entity;

public class Crew {
    private int id;
    private int number;
    private Horse horse;
    private User user;

    public Crew() {
        this(-1, -1, null, null);
    }

    public Crew(int id, int number, Horse horse, User user) {
        this.id = id;
        this.number = number;
        this.horse = horse;
        this.user = user;
    }

    public Crew(int number, Horse horse, User user) {
        this(-1, number, horse, user);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Horse getHorse() {
        return horse;
    }

    public void setHorse(Horse horse) {
        this.horse = horse;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return user.toString() + " и " + horse.toString();
    }
}
