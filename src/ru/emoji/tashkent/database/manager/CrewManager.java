package ru.emoji.tashkent.database.manager;

import ru.emoji.tashkent.database.entity.Crew;
import ru.emoji.tashkent.database.entity.Hippodrome;
import ru.emoji.tashkent.utils.MysqlDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CrewManager extends Manager<Crew> {
    public CrewManager(MysqlDatabase database) {
        super(database);
    }

    @Override
    public void add(Crew object) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "INSERT INTO crews (number, horse_id, user_id) VALUES (?, ?, ?)";

            PreparedStatement s = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            s.setInt(1, object.getNumber());
            s.setInt(2, object.getHorseId());
            s.setInt(3, object.getUserId());

            ResultSet keys = s.getGeneratedKeys();
            if (keys.next()) {
                object.setId(keys.getInt(1));
                return;
            }
            throw new SQLException("Hippodrome not added");
        }
    }

    @Override
    public Crew getById(int id) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT * FROM crews WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new Crew(
                        id,
                        result.getInt("number"),
                        result.getInt("horse_id"),
                        result.getInt("user_id")
                );
            }
            return null;
        }
    }

    private List<Crew> processResultSet(ResultSet result) throws SQLException {
        List<Crew> list = new ArrayList<>();
        while (result.next()) {
            list.add(new Crew(
                    result.getInt("id"),
                    result.getInt("number"),
                    result.getInt("horse_id"),
                    result.getInt("user_id")
            ));
        }
        return list;
    }

    List<Crew> getByHorseId(int horseId) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT * FROM crews where horse_id=?";
            PreparedStatement s = conn.prepareStatement(sql);

            s.setInt(1, horseId);
            ResultSet result = s.executeQuery(sql);
            return processResultSet(result);
        }
    }

    List<Crew> getByUserId(int userId) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT * FROM crews where user_id=?";
            PreparedStatement s = conn.prepareStatement(sql);

            s.setInt(1, userId);
            ResultSet result = s.executeQuery(sql);
            return processResultSet(result);
        }
    }

    @Override
    public List<Crew> getAll() throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT * FROM crews";
            Statement statement = conn.createStatement();

            ResultSet result = statement.executeQuery(sql);
            return processResultSet(result);
        }
    }

    @Override
    int update(Crew object) throws SQLException {
        try (Connection c = database.getConnection()) {
            String sql = "UPDATE crews SET number=?, " +
                    "horse_id=?, user_id=? WHERE id=?";

            PreparedStatement s = c.prepareStatement(sql);
            s.setInt(1, object.getNumber());
            s.setInt(2, object.getHorseId());
            s.setInt(3, object.getUserId());
            s.setInt(4, object.getId());

            return s.executeUpdate();
        }
    }

    @Override
    int delete(Crew object) throws SQLException {
        if (object.getId() != -1) {
            return deleteById(object.getId());
        }
        throw new RuntimeException("Deleting is not implemented");
//        return 0;
    }

    public int deleteById(int id) throws SQLException {
        try (Connection c = database.getConnection()) {
            String sql = "DELETE FROM crews WHERE id=?";

            PreparedStatement s = c.prepareStatement(sql);
            s.setInt(1, id);

            return s.executeUpdate();
        }
    }

    @Override
    String getTableName() {
        return "crews";
    }
}
