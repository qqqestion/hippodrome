package ru.emoji.tashkent.database.entity;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

public class Competition {
    private int id;
    private String name;
    private int fund;
    private LocalDateTime dateStart;
    private LocalDateTime dateEnd;
    private int hippodromeId;


    public Competition(int id, String name, int fund,
                       LocalDateTime dateStart, LocalDateTime dateEnd, int hippodromeId) {
        this.id = id;
        this.name = name;
        this.fund = fund;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.hippodromeId = hippodromeId;
    }

    public Competition(String name, int fund,
                       LocalDateTime dateStart, LocalDateTime dateEnd, int hippodromeId) {
        this(-1, name, fund, dateStart, dateEnd, hippodromeId);
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

    public int getHippodromeId() {
        return hippodromeId;
    }

    public void setHippodromeId(int hippodromeId) {
        this.hippodromeId = hippodromeId;
    }

    @Override
    public String toString() {
        LocalDateTime today = LocalDateTime.now();
        String format = "d MMM yyyy H:mm";
        if (dateStart.isBefore(today) && today.isBefore(dateEnd)) {
            return "\"" + name + "\" закончится " + dateEnd.format(DateTimeFormatter.ofPattern(format));
        } else if (dateStart.isAfter(today)) { // Не начался
            return "\"" + name + "\" начнется " + dateStart.format(DateTimeFormatter.ofPattern(format));
        } else if (dateEnd.isBefore(today)) { // Прошел
            return "\"" + name + "\" завершился " + dateEnd.format(DateTimeFormatter.ofPattern(format));
        }
        throw new RuntimeException("wrong date");
    }
}
