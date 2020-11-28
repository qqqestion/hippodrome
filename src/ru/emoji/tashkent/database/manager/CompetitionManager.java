package ru.emoji.tashkent.database.manager;

import ru.emoji.tashkent.database.entity.Competition;
import ru.emoji.tashkent.database.entity.Crew;
import ru.emoji.tashkent.utils.DateUtils;
import ru.emoji.tashkent.utils.MysqlDatabase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CompetitionManager extends Manager<Competition> {
    public CompetitionManager(MysqlDatabase database) {
        super(database);
    }

    @Override
    public void add(Competition object) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "INSERT INTO competitions (name, fund, date_start, " +
                    "date_end, hippodrome_id) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement s = conn.prepareStatement(sql,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            s.setString(1, object.getName());
            s.setInt(2, object.getFund());
            s.setObject(3, object.getDateStart().format(DateUtils.MYSQL_FORMATTER));
            s.setObject(4, object.getDateEnd().format(DateUtils.MYSQL_FORMATTER));
            s.setInt(5, object.getHippodromeId());
            s.executeUpdate();

            ResultSet keys = s.getGeneratedKeys();
            if (keys.next()) {
                object.setId(keys.getInt(1));
                return;
            }
            throw new SQLException("Competition not added");
        }
    }

    @Override
    public Competition getById(int id) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT * FROM competitions WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return getObjectFromResultSet(result);
            }
            return null;
        }
    }

    @Override
    public List<Competition> getAll() throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT * FROM competitions";
            Statement statement = conn.createStatement();

            ResultSet result = statement.executeQuery(sql);
            return processResultSet(result);
        }
    }

    public List<Competition> getFinishedCompetitions(LocalDateTime date) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT * FROM competitions where date_end < ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setObject(1, date);

            ResultSet result = statement.executeQuery();
            return processResultSet(result);
        }
    }

    public List<Competition> getUpcomingCompetitions(LocalDateTime date) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT * FROM competitions where date_start > ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setObject(1, date);

            ResultSet result = statement.executeQuery();
            return processResultSet(result);
        }
    }

    public List<Competition> getCurrentCompetitions(LocalDateTime date) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT * FROM competitions where date_start < ? AND date_end > ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setObject(1, date);
            statement.setObject(2, date);

            ResultSet result = statement.executeQuery();
            return processResultSet(result);
        }
    }

    private List<Competition> processResultSet(ResultSet result) throws SQLException {
        List<Competition> list = new ArrayList<>();
        while (result.next()) {
            list.add(getObjectFromResultSet(result));
        }
        return list;
    }

    private Competition getObjectFromResultSet(ResultSet result) throws SQLException {
        return new Competition(
                result.getInt("id"),
                result.getString("name"),
                result.getInt("fund"),
                result.getObject("date_start", LocalDateTime.class),
                result.getObject("date_end", LocalDateTime.class),
                result.getInt("hippodrome_id")
        );
    }

    @Override
    public int update(Competition object) throws SQLException {
        return 0;
    }

    @Override
    public int delete(Competition object) throws SQLException {
        return 0;
    }

    @Override
    String getTableName() {
        return null;
    }
}
