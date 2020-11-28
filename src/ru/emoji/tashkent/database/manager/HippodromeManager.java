package ru.emoji.tashkent.database.manager;

import ru.emoji.tashkent.database.entity.Hippodrome;
import ru.emoji.tashkent.database.entity.Horse;
import ru.emoji.tashkent.database.entity.User;
import ru.emoji.tashkent.utils.MysqlDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HippodromeManager extends Manager<Hippodrome> {
    public HippodromeManager(MysqlDatabase database) {
        super(database);
    }

    @Override
    public void add(Hippodrome object) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "INSERT INTO hippodromes (name, address, people_capacity, " +
                    "website) VALUES (?, ?, ?, ?)";

            PreparedStatement s = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            s.setString(1, object.getName());
            s.setString(2, object.getAddress());
            s.setInt(3, object.getPeopleCapacity());
            s.setString(4, object.getWebsite());
            s.executeUpdate();

            ResultSet keys = s.getGeneratedKeys();
            if (keys.next()) {
                object.setId(keys.getInt(1));
                return;
            }
            throw new SQLException("Hippodrome not added");
        }
    }

    @Override
    public Hippodrome getById(int id) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT * FROM hippodromes WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new Hippodrome(
                        id,
                        result.getString("name"),
                        result.getString("address"),
                        result.getInt("people_capacity"),
                        result.getString("website")
                );
            }
            return null;
        }
    }

    @Override
    public List<Hippodrome> getAll() throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT * FROM hippodromes";
            Statement statement = conn.createStatement();

            ResultSet result = statement.executeQuery(sql);
            List<Hippodrome> hippodromeList = new ArrayList<>();
            while (result.next()) {
                hippodromeList.add(new Hippodrome(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("address"),
                        result.getInt("people_capacity"),
                        result.getString("website")
                ));
            }
            return hippodromeList;
        }

    }

    @Override
    public int update(Hippodrome object) throws SQLException {
        try (Connection c = database.getConnection()) {
            String sql = "UPDATE hippodromes SET name=?, " +
                    "address=?, people_capacity=?, website=? WHERE id=?";

            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, object.getName());
            s.setString(2, object.getAddress());
            s.setInt(3, object.getPeopleCapacity());
            s.setString(4, object.getWebsite());
            s.setInt(5, object.getId());

            return s.executeUpdate();
        }
    }

    @Override
    public int delete(Hippodrome object) throws SQLException {
        if (object.getId() != -1) {
            return deleteById(object.getId());
        }
        return deleteByName(object.getName());
    }

    public int deleteById(int id) throws SQLException {
        try (Connection c = database.getConnection()) {
            String sql = "DELETE FROM hippodromes WHERE id=?";

            PreparedStatement s = c.prepareStatement(sql);
            s.setInt(1, id);

            return s.executeUpdate();
        }
    }
    public int deleteByName(String name) throws SQLException {
        try (Connection c = database.getConnection()) {
            String sql = "DELETE FROM hippodromes WHERE name=?";

            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, name);

            return s.executeUpdate();
        }
    }

    @Override
    public String getTableName() {
        return "hippodromes";
    }
}
