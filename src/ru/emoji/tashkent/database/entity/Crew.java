package ru.emoji.tashkent.database.entity;

public class Crew {
    private int id;
    private int number;
    private int horseId;
    private int userId;

    public Crew(int id, int number, int horseId, int userId) {
        this.id = id;
        this.number = number;
        this.horseId = horseId;
        this.userId = userId;
    }

    public Crew(int number, int horseId, int userId) {
        this(-1, number, horseId, userId);
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

    public int getHorseId() {
        return horseId;
    }

    public void setHorseId(int horseId) {
        this.horseId = horseId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Crew{" +
                "id=" + id +
                ", number=" + number +
                ", horseId=" + horseId +
                ", userId=" + userId +
                '}';
    }
}
