package ru.emoji.tashkent.database.manager;

import ru.emoji.tashkent.database.entity.*;
import ru.emoji.tashkent.utils.DateUtils;
import ru.emoji.tashkent.utils.MysqlDatabase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RaceManager extends Manager<Race> {
    private final CompetitionManager competitionManager;
    private final CrewManager crewManager;

    public RaceManager(MysqlDatabase database) {
        super(database);
        competitionManager = new CompetitionManager(database);
        crewManager = new CrewManager(database);
    }

    @Override
    public void add(Race object) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "INSERT INTO races (track, competition_id, " +
                    "crew_id, race_number, time_of_race, date_of_race, " +
                    "race_length) " +
                    "VALUES" +
                    "(?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement s = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            s.setInt(1, object.getTrack());
            s.setInt(2, object.getCompetition().getId());
            s.setInt(3, object.getCrew().getId());
            s.setInt(4, object.getRaceNumber());
            s.setTimestamp(5, object.getTimeOfRace());
            s.setObject(6, object.getDateOfRace().format(DateUtils.MYSQL_FORMATTER));
            s.setInt(7, object.getRaceLength());
            s.executeUpdate();

            ResultSet keys = s.getGeneratedKeys();
            if (keys.next()) {
                object.setId(keys.getInt(1));
                return;
            }
            throw new SQLException("crew not added");
        }
    }
    @Override
    protected Race getEntityFromResultSet(ResultSet result) throws SQLException {
        Race entity = null;
        if (result.next()) {
            Competition comp = competitionManager.getById(result.getInt("competition_id"));
            Crew crew = crewManager.getById(result.getInt("crew_id"));
            entity = new Race(
                    result.getInt("id"),
                    result.getInt("track"),
                    comp,
                    crew,
                    result.getInt("race_number"),
                    result.getInt("race_length"),
                    result.getTimestamp("time_of_race"),
                    result.getObject("date_of_race", LocalDateTime.class)
            );
        }
        return entity;
    }

    public List<Race> getByCrewId(int crewId) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT * FROM races WHERE crew_id=?";
            PreparedStatement s = conn.prepareStatement(sql);
            s.setInt(1, crewId);

            ResultSet result = s.executeQuery();
            return getEntityListFromResultSet(result);
        }
    }

    public List<Race> getByCompetitionId(int competitionId) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT * FROM races WHERE competition_id=? order by race_number, track, time_of_race";
            PreparedStatement s = conn.prepareStatement(sql);
            s.setInt(1, competitionId);

            ResultSet result = s.executeQuery();
            return getEntityListFromResultSet(result);
        }
    }

    @Override
    public int update(Race object) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "UPDATE races SET track=?, competition_id=?," +
                    "crew_id=?, race_number=?, race_length=?, " +
                    "time_of_race=?, date_of_race=? " +
                    "WHERE id=?";
            PreparedStatement s = conn.prepareStatement(sql);
            s.setInt(1, object.getTrack());
            s.setInt(2, object.getCompetition().getId());
            s.setInt(3, object.getCrew().getId());
            s.setInt(4, object.getRaceNumber());
            s.setTimestamp(5, object.getTimeOfRace());
            s.setObject(6, object.getDateOfRace().format(DateUtils.MYSQL_FORMATTER));
            s.setInt(7, object.getRaceLength());
            s.setInt(8, object.getId());

            return s.executeUpdate();
        }
    }

    @Override
    public int delete(Race object) throws SQLException {
        if (object.getId() != -1) {
            return deleteById(object.getId());
        }
        throw new RuntimeException("Deleting is not implemented");
    }

    @Override
    public Race createEntity() {
        return new Race();
    }

    @Override
    String getTableName() {
        return "races";
    }
}
