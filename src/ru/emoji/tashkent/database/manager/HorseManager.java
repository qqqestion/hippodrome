package ru.emoji.tashkent.database.manager;

import ru.emoji.tashkent.database.entity.Horse;
import ru.emoji.tashkent.utils.MysqlDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HorseManager extends Manager<Horse> {
    public HorseManager(MysqlDatabase database) {
        super(database);
    }

    @Override
    public void add(Horse horse) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "INSERT INTO horses (name, birth_year, experience, " +
                    "owner, price) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, horse.getName());
            statement.setInt(2, horse.getBirthYear());
            statement.setInt(3, horse.getExperience());
            statement.setString(4, horse.getOwner());
            statement.setInt(5, horse.getPrice());
            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                horse.setId(keys.getInt(1));
                return;
            }
            throw new SQLException("Horse not added");
        }
    }

    @Override
    public Horse getById(int id) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT * FROM horses WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new Horse(
                        id,
                        result.getString("name"),
                        result.getInt("birth_year"),
                        result.getInt("experience"),
                        result.getString("owner"),
                        result.getInt("price")
                );
            }
            return null;
        }
    }

    public Horse getByName(String name) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT * FROM horses WHERE name=?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new Horse(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getInt("birth_year"),
                        result.getInt("experience"),
                        result.getString("owner"),
                        result.getInt("price")
                );
            }
            return null;
        }
    }

    @Override
    public List<Horse> getAll() throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT * FROM horses";
            Statement statement = conn.createStatement();

            ResultSet result = statement.executeQuery(sql);
            List<Horse> horseList = new ArrayList<>();
            while (result.next()) {
                horseList.add(new Horse(
                    result.getInt("id"),
                    result.getString("name"),
                    result.getInt("birth_year"),
                    result.getInt("experience"),
                    result.getString("owner"),
                    result.getInt("price")
                ));
            }
            return horseList;
        }
    }

    @Override
    public int update(Horse horse) throws SQLException {
        try (Connection c = database.getConnection()) {
            String sql = "UPDATE horses SET name=?, birth_year=?, " +
                    "experience=?, owner=?, price=? WHERE id=?";

            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, horse.getName());
            s.setInt(2, horse.getBirthYear());
            s.setInt(3, horse.getExperience());
            s.setString(4, horse.getOwner());
            s.setInt(5, horse.getPrice());
            s.setInt(6, horse.getId());

            return s.executeUpdate();
        }
    }

    @Override
    public int delete(Horse object) throws SQLException {
        if (object.getId() == -1) {
            return deleteById(object.getId());
        }
        return deleteByName(object.getName());
    }

    @Override
    public String getTableName() {
        return "horses";
    }

    public int deleteById(int id) throws SQLException {
        try (Connection c = database.getConnection()) {
            String sql = "DELETE FROM horses WHERE id=?";

            PreparedStatement s = c.prepareStatement(sql);
            s.setInt(1, id);

            return s.executeUpdate();
        }
    }

    public int deleteByName(String name) throws SQLException {
        try (Connection c = database.getConnection()) {
            String sql = "DELETE FROM horses WHERE name=?";

            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, name);

            return s.executeUpdate();
        }
    }
}
