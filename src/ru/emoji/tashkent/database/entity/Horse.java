package ru.emoji.tashkent.database.entity;


public class Horse {
    private int id;
    private String name;
    private int birthYear;
    private int experience;
    private String owner;
    private int price;

    public Horse(int id, String name, int birthYear, int experience, String owner, int price) {
        this.id = id;
        this.name = name;
        this.birthYear = birthYear;
        this.experience = experience;
        this.owner = owner;
        this.price = price;
    }

    public Horse(String name, int birthYear, int experience, String owner, int price) {
        this(-1, name, birthYear, experience, owner, price);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Horse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthYear=" + birthYear +
                ", experience=" + experience +
                ", owner='" + owner + '\'' +
                ", price=" + price +
                '}';
    }
}
