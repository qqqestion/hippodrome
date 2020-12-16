package ru.emoji.tashkent.database.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Race {
    private int id;
    private int track;
    private Competition competition;
    private Crew crew;
    private int raceNumber;
    private int raceLength;
    private Timestamp timeOfRace;
    private LocalDateTime dateOfRace;

    public Race() {
        this(-1, -1, null,
                null, -1, -1,
                new Timestamp(1), LocalDateTime.now());
    }

    public Race(int id, int track, Competition competition,
                Crew crew, int raceNumber, int raceLength,
                Timestamp timeOfRace, LocalDateTime dateOfRace) {
        this.id = id;
        this.track = track;
        this.competition = competition;
        this.crew = crew;
        this.raceNumber = raceNumber;
        this.raceLength = raceLength;
        this.timeOfRace = timeOfRace;
        this.dateOfRace = dateOfRace;
    }

    public Race(int track, Competition competition,
                Crew crew, int raceNumber, int raceLength,
                Timestamp timeOfRace, LocalDateTime dateOfRace) {
        this(-1, track, competition, crew, raceNumber,
                raceLength, timeOfRace, dateOfRace);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTrack() {
        return track;
    }

    public void setTrack(int track) {
        this.track = track;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public Crew getCrew() {
        return crew;
    }

    public void setCrew(Crew crew) {
        this.crew = crew;
    }

    public int getRaceNumber() {
        return raceNumber;
    }

    public void setRaceNumber(int raceNumber) {
        this.raceNumber = raceNumber;
    }

    public int getRaceLength() {
        return raceLength;
    }

    public void setRaceLength(int raceLength) {
        this.raceLength = raceLength;
    }

    public Timestamp getTimeOfRace() {
        return timeOfRace;
    }

    public void setTimeOfRace(Timestamp timeOfRace) {
        this.timeOfRace = timeOfRace;
    }

    public LocalDateTime getDateOfRace() {
        return dateOfRace;
    }

    public void setDateOfRace(LocalDateTime dateOfRace) {
        this.dateOfRace = dateOfRace;
    }

    @Override
    public String toString() {
        return "Race{" +
                "id=" + id +
                ", track=" + track +
                ", competition=" + competition +
                ", crew=" + crew +
                ", raceNumber=" + raceNumber +
                ", raceLength=" + raceLength +
                ", timeOfRace=" + timeOfRace +
                ", dateOfRace=" + dateOfRace +
                '}';
    }
}
