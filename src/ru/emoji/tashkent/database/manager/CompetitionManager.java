package ru.emoji.tashkent.database.manager;

import ru.emoji.tashkent.database.entity.Competition;
import ru.emoji.tashkent.database.entity.Horse;
import ru.emoji.tashkent.database.entity.Race;
import ru.emoji.tashkent.database.entity.User;
import ru.emoji.tashkent.utils.DateUtils;
import ru.emoji.tashkent.utils.MysqlDatabase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CompetitionManager extends Manager<Competition> {
    private RaceManager raceManager;

    public CompetitionManager(MysqlDatabase database) {
        super(database);
//        raceManager = new RaceManager(database);
    }

    @Override
    public void add(Competition object) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "INSERT INTO competitions (name, fund, date_start, " +
                    "date_end) VALUES (?, ?, ?, ?)";

            PreparedStatement s = conn.prepareStatement(sql,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            s.setString(1, object.getName());
            s.setInt(2, object.getFund());
            s.setObject(3, object.getDateStart().format(DateUtils.MYSQL_FORMATTER));
            s.setObject(4, object.getDateEnd().format(DateUtils.MYSQL_FORMATTER));
            s.executeUpdate();

            ResultSet keys = s.getGeneratedKeys();
            if (keys.next()) {
                object.setId(keys.getInt(1));
                return;
            }
            throw new SQLException("Competition not added");
        }
    }

//    private void addOrUpdateRaces(Competition competition) throws SQLException {
//        for (int i = 0; i < competition.getRaces().size(); i++) {
//            Race race = competition.getRace(i);
//            if (race.getId() == -1) {
//                raceManager.add(race);
//            } else {
//                raceManager.update(race);
//            }
//        }
//    }

    @Override
    public List<Competition> getAll() throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT * FROM competitions order by date_start";
            Statement statement = conn.createStatement();

            ResultSet result = statement.executeQuery(sql);
            return getEntityListFromResultSet(result);
        }
    }

    public List<Competition> getFinishedCompetitions(LocalDateTime date) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT * FROM competitions where date_end < ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setObject(1, date);

            ResultSet result = statement.executeQuery();
            return getEntityListFromResultSet(result);
        }
    }

    public List<Competition> getUpcomingCompetitions(LocalDateTime date) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT * FROM competitions where date_start > ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setObject(1, date);

            ResultSet result = statement.executeQuery();
            return getEntityListFromResultSet(result);
        }
    }

    public List<Competition> getCurrentCompetitions(LocalDateTime date) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT * FROM competitions where date_start < ? AND date_end > ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setObject(1, date);
            statement.setObject(2, date);

            ResultSet result = statement.executeQuery();
            return getEntityListFromResultSet(result);
        }
    }

    @Override
    protected Competition getEntityFromResultSet(ResultSet result) throws SQLException {
        Competition competition = null;
        if (result.next()) {
             competition = new Competition(
                    result.getInt("id"),
                    result.getString("name"),
                    result.getInt("fund"),
                    result.getObject("date_start", LocalDateTime.class),
                    result.getObject("date_end", LocalDateTime.class)
            );
//            List<Race> races = raceManager.getByCompetitionId(competition.getId());
//            competition.setRaces(races);
        }
        return competition;
    }

    public List<Competition> getCompetitionsThatHorseEnteredIn(Horse horse) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT DISTINCT competitions.* " +
                    "FROM competitions " +
                    "join races r on competitions.id = r.competition_id " +
                    "join crews c on r.crew_id = c.id " +
                    "join horses h on c.horse_id = h.id " +
                    "where h.id = ?";
            PreparedStatement s = conn.prepareStatement(sql);
            s.setInt(1, horse.getId());

            ResultSet result = s.executeQuery();
            return getEntityListFromResultSet(result);
        }
    }

    public List<Competition> getCompetitionsThatUserEnteredIn(User user) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT DISTINCT competitions.* " +
                    "FROM competitions " +
                    "join races r on competitions.id = r.competition_id " +
                    "join crews c on r.crew_id = c.id " +
                    "join users u on c.user_id = u.id " +
                    "where u.id = ?";
            PreparedStatement s = conn.prepareStatement(sql);
            s.setInt(1, user.getId());

            ResultSet result = s.executeQuery();
            return getEntityListFromResultSet(result);
        }
    }

    @Override
    public int update(Competition object) throws SQLException {
        try (Connection c = database.getConnection()) {
            String sql = "UPDATE competitions SET name=?, " +
                    "fund=?, date_start=?, date_end=? WHERE id=?";

            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, object.getName());
            s.setInt(2, object.getFund());
            s.setObject(3, object.getDateStart());
            s.setObject(4, object.getDateEnd());
            s.setInt(5, object.getId());

            return s.executeUpdate();
        }

    }

    @Override
    public int delete(Competition object) throws SQLException {
        System.out.println(object.getId() + " " + object.getName());
        if (object.getId() != -1) {
            return deleteById(object.getId());
        }
        throw new RuntimeException("delete not implementing");
    }

    @Override
    public Competition createEntity() {
        return new Competition();
    }

    @Override
    String getTableName() {
        return "competitions";
    }
}
