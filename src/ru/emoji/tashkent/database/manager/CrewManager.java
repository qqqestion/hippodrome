package ru.emoji.tashkent.database.manager;

import ru.emoji.tashkent.database.entity.Crew;
import ru.emoji.tashkent.database.entity.Horse;
import ru.emoji.tashkent.database.entity.User;
import ru.emoji.tashkent.utils.MysqlDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CrewManager extends Manager<Crew> {
    private HorseManager horseManager;
    private UserManager userManager;

    public CrewManager(MysqlDatabase database) {
        super(database);
        setHorseManager(new HorseManager(database));
        setUserManager(new UserManager(database));
    }

    public void setHorseManager(HorseManager horseManager) {
        this.horseManager = horseManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public void add(Crew object) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "INSERT INTO crews (number, horse_id, user_id) VALUES (?, ?, ?)";

            PreparedStatement s = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            s.setInt(1, object.getNumber());
            s.setInt(2, object.getHorse().getId());
            s.setInt(3, object.getUser().getId());
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
    protected Crew getEntityFromResultSet(ResultSet result) throws SQLException {
        Crew entity = null;
        if (result.next()) {
            Horse horse = horseManager.getById(result.getInt("horse_id"));
            User user = userManager.getById(result.getInt("user_id"));
            entity = new Crew(
                    result.getInt("id"),
                    result.getInt("number"),
                    horse,
                    user
            );
        }
        return entity;
    }

    public List<Crew> getByHorseId(int horseId) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT * FROM crews where horse_id=?";
            PreparedStatement s = conn.prepareStatement(sql);
            s.setInt(1, horseId);

            ResultSet result = s.executeQuery();
            return getEntityListFromResultSet(result);
        }
    }

    public List<Crew> getByUserId(int userId) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT * FROM crews where user_id=?";
            PreparedStatement s = conn.prepareStatement(sql);

            s.setInt(1, userId);
            ResultSet result = s.executeQuery();
            return getEntityListFromResultSet(result);
        }
    }

    @Override
    public int update(Crew object) throws SQLException {
        try (Connection c = database.getConnection()) {
            String sql = "UPDATE crews SET number=?, " +
                    "horse_id=?, user_id=? WHERE id=?";

            PreparedStatement s = c.prepareStatement(sql);
            s.setInt(1, object.getNumber());
            s.setInt(2, object.getHorse().getId());
            s.setInt(3, object.getUser().getId());
            s.setInt(4, object.getId());

            return s.executeUpdate();
        }
    }

    @Override
    public int delete(Crew object) throws SQLException {
        if (object.getId() != -1) {
            return deleteById(object.getId());
        }
        throw new RuntimeException("Deleting is not implemented");
//        return 0;
    }

    public boolean isExist(Crew crew) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT id FROM crews WHERE number=? AND horse_id=? AND user_id=?";
            PreparedStatement s = conn.prepareStatement(sql);

            s.setInt(1, crew.getNumber());
            s.setInt(2, crew.getHorse().getId());
            s.setInt(3, crew.getUser().getId());
            ResultSet result = s.executeQuery();
            if (result.next()) {
                crew.setId(result.getInt("id"));
                return true;
            }
            return false;
        }
    }

    @Override
    public Crew createEntity() {
        return new Crew();
    }

    @Override
    String getTableName() {
        return "crews";
    }
}
