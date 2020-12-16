package ru.emoji.tashkent.database.entity;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

public class Competition {
    private int id;
    private String name;
    private int fund;
    private LocalDateTime dateStart;
    private LocalDateTime dateEnd;
    private List<Race> races = new ArrayList<>();

    public Competition() {
        this(-1, "", 0, null, null);
    }

    public Competition(int id, String name, int fund,
                       LocalDateTime dateStart, LocalDateTime dateEnd) {
        this.id = id;
        this.name = name;
        this.fund = fund;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    public Competition(String name, int fund,
                       LocalDateTime dateStart, LocalDateTime dateEnd) {
        this(-1, name, fund, dateStart, dateEnd);
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

    public int getFund() {
        return fund;
    }

    public void setFund(int fund) {
        this.fund = fund;
    }

    public LocalDateTime getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDateTime dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDateTime getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDateTime dateEnd) {
        this.dateEnd = dateEnd;
    }

    public void addRace(Race race) {
        races.add(race);
        if (id != -1) {
            race.setCompetition(this);
        }
    }

    public Race getRace(int i) {
        return races.get(i);
    }

    public List<Race> getRaces() {
        return races;
    }

    public void setRaces(List<Race> races) {
        this.races = races;
    }

    @Override
    public String toString() {
        LocalDateTime today = LocalDateTime.now();
        String format = "d MMM yyyy H:mm";
//        System.out.println(dateStart + " " + dateEnd);
//        if (dateStart.isBefore(today) && today.isBefore(dateEnd)) {
//            return "\"" + name + "\" закончится " + dateEnd.format(DateTimeFormatter.ofPattern(format));
//        } else if (dateStart.isAfter(today)) { // Не начался
//            return "\"" + name + "\" начнется " + dateStart.format(DateTimeFormatter.ofPattern(format));
//        } else if (dateEnd.isBefore(today)) { // Прошел
//            return "\"" + name + "\" завершился " + dateEnd.format(DateTimeFormatter.ofPattern(format));
//        }
        if (id == -1) {
            return "Добавьте соревнование...";
        }
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Competition) {
            return name.equals(((Competition) obj).name);
        }
        return super.equals(obj);
    }

    public boolean isStarted() {
        if (dateStart == null) {
            return false;
        }
        int res = dateStart.compareTo(LocalDateTime.now());
        return res > 0;
    }
}
