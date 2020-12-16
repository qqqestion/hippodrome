package ru.emoji.tashkent.database.manager;

import ru.emoji.tashkent.database.entity.Competition;
import ru.emoji.tashkent.database.entity.Horse;
import ru.emoji.tashkent.database.entity.User;
import ru.emoji.tashkent.utils.MysqlDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserManager extends Manager<User> {

    public UserManager(MysqlDatabase database) {
        super(database);
    }

    @Override
    public void add(User user) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "INSERT INTO users (first_name, last_name, " +
                    "birth_year, email, password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql,
                    PreparedStatement.RETURN_GENERATED_KEYS);
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

    public User getByEmailAndPassword(String email, String password) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet result = statement.executeQuery();
            return getEntityFromResultSet(result);
        }
    }

    @Override
    public List<User> getAll() throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT * FROM users order by first_name, last_name";
            Statement statement = conn.createStatement();

            ResultSet result = statement.executeQuery(sql);
            return getEntityListFromResultSet(result);
        }
    }

    @Override
    public int update(User user) throws SQLException {
        try (Connection c = database.getConnection()) {
            String sql = "UPDATE users SET first_name=?, last_name=?, " +
                    "birth_year=?, email=?, password=? WHERE id=?";

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

    @Override
    public int delete(User object) throws SQLException {
        if (object.getId() == -1) {
            return deleteById(object.getId());
        }
        return deleteByEmail(object.getEmail());
    }

    @Override
    public User createEntity() {
        return new User();
    }

    @Override
    protected User getEntityFromResultSet(ResultSet result) throws SQLException {
        User user = null;
        if (result.next()) {
            user = new User(
                    result.getInt("id"),
                    result.getString("first_name"),
                    result.getString("last_name"),
                    result.getInt("birth_year"),
                    result.getString("email"),
                    result.getString("password"),
                    result.getBoolean("is_admin")
            );
        }
        return user;
    }

    @Override
    public String getTableName() {
        return "users";
    }

    public int deleteByEmail(String email) throws SQLException {
        try (Connection c = database.getConnection()) {
            String sql = "DELETE FROM users WHERE email=?";

            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, email);

            return s.executeUpdate();
        }
    }
}
