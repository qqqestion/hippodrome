package ru.emoji.tashkent.database.manager;

import ru.emoji.tashkent.database.entity.User;
import ru.emoji.tashkent.utils.MysqlDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private MysqlDatabase database;

    public UserManager(MysqlDatabase database) {
        this.database = database;
    }

    public void add(User user) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "INSERT INTO users (first_name, last_name, birth_year, email, password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setInt(3, user.getBirthYear());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPassword());
            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                user.setId(keys.getInt(1));
                return;
            }
            throw new SQLException("User not added");
        }
    }

    public User getById(int id) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT * FROM users WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new User(
                        id,
                        result.getString("first_name"),
                        result.getString("last_name"),
                        result.getInt("birth_year"),
                        result.getString("email"),
                        result.getString("password")
                );
            }
            return null;
        }
    }

    public User getByEmailAndPassword(String email, String password) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new User(
                        result.getInt("id"),
                        result.getString("first_name"),
                        result.getString("last_name"),
                        result.getInt("birth_year"),
                        result.getString("email"),
                        result.getString("password")
                );
            }
            return null;
        }
    }

    public List<User> getAll() throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT * FROM users";
            Statement statement = conn.createStatement();

            ResultSet result = statement.executeQuery(sql);
            List<User> userList = new ArrayList<>();
            while (result.next()) {
                userList.add(new User(
                        result.getInt("id"),
                        result.getString("first_name"),
                        result.getString("last_name"),
                        result.getInt("birth_year"),
                        result.getString("email"),
                        result.getString("password")
                ));
            }
            return userList;
        }
    }

    public int update(User user) throws SQLException {
        try (Connection c = database.getConnection()) {
            String sql = "UPDATE users SET first_name=?, last_name=?, birth_year=?, email=?, password=? WHERE id=?";

            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, user.getFirstName());
            s.setString(2, user.getLastName());
            s.setInt(3, user.getBirthYear());
            s.setString(4, user.getEmail());
            s.setString(5, user.getPassword());
            s.setInt(6, user.getId());

            return s.executeUpdate();
        }
    }

    public int deleteById(int id) throws SQLException {
        try (Connection c = database.getConnection()) {
            String sql = "DELETE FROM users WHERE id=?";

            PreparedStatement s = c.prepareStatement(sql);
            s.setInt(1, id);

            return s.executeUpdate();
        }
    }
}
