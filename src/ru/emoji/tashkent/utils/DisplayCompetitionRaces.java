package ru.emoji.tashkent.utils;

import ru.emoji.tashkent.database.entity.Competition;
import ru.emoji.tashkent.database.entity.Race;
import ru.emoji.tashkent.database.manager.RaceManager;
import ru.emoji.tashkent.enums.DisplayTypeEnum;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface DisplayCompetitionRaces {
    default void setParticipant(
            RaceManager raceManager,
            Competition competition,
            JTextPane pane,
            DisplayTypeEnum displayParameter
    ) {
        setParticipant(raceManager, competition, pane, new StringBuilder(), displayParameter);
    }
    default void setParticipant(
            RaceManager raceManager,
            Competition competition,
            JTextPane pane,
            StringBuilder sb,
            DisplayTypeEnum displayParameter
    ) {
        List<Race> races;
        try {
            races = raceManager.getByCompetitionId(competition.getId());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return;
        }
        int lastRaceNumber = -1;
        boolean isFirst = true;
        LocalDateTime today = LocalDateTime.now();
        for (Race race : races) {
            boolean isCompleted = race.getDateOfRace().compareTo(today) < 0;
            if (race.getRaceNumber() != lastRaceNumber) {
                if (!isFirst) {
                    sb.append('\n');
                }
                isFirst = false;
                sb.append("Забег №").append(race.getRaceNumber()).append(" на ").append(race.getRaceLength()).append("м.");
                if (!isCompleted) {
                    sb.append(" пройдет ").append(race.getDateOfRace().format(DateUtils.MYSQL_FORMATTER));
                } else {
                    sb.append(" прошел ").append(race.getDateOfRace().format(DateUtils.MYSQL_FORMATTER));
                }
                sb.append('\n');
                lastRaceNumber = race.getRaceNumber();
            }
            sb.append("Дорожка ").append(race.getTrack()).append('\n');
            sb.append("Жокей: ").append(race.getCrew().getUser()).append('\n');
            sb.append("Лошадь: ").append(race.getCrew().getHorse()).append('\n');
            if (isCompleted) {
                long time = race.getTimeOfRace().getTime() / 1000;
                long minutes = time / 60;
                long seconds = time % 60;
                sb.append("Время забега:");
                if (minutes != 0) {
                    sb.append(' ').append(minutes).append(" мин.");
                }
                if (seconds != 0) {
                    sb.append(' ').append(seconds).append(" сек.");
                }
                sb.append('\n');
            }
            sb.append('\n');
        }
        pane.setText(sb.toString());
    }
}
