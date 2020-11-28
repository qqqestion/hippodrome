package ru.emoji.tashkent.database.entity;

public class Hippodrome {
    private int id;
    private String name;
    private String address;
    private int peopleCapacity;
    private String website;

    public Hippodrome(int id, String name, String address, int peopleCapacity, String website) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.peopleCapacity = peopleCapacity;
        this.website = website;
    }

    public Hippodrome(String name, String address, int peopleCapacity, String website) {
        this(-1, name, address, peopleCapacity, website);
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPeopleCapacity() {
        return peopleCapacity;
    }

    public void setPeopleCapacity(int peopleCapacity) {
        this.peopleCapacity = peopleCapacity;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String toString() {
        return "Hippodrome{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", peopleCapacity=" + peopleCapacity +
                ", website='" + website + '\'' +
                '}';
    }
}
