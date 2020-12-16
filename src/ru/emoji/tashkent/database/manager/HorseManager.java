package ru.emoji.tashkent.database.manager;

import ru.emoji.tashkent.database.entity.Competition;
import ru.emoji.tashkent.database.entity.Crew;
import ru.emoji.tashkent.database.entity.Horse;
import ru.emoji.tashkent.database.entity.Race;
import ru.emoji.tashkent.utils.MysqlDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HorseManager extends Manager<Horse> {
    private CrewManager crewManager;

    public HorseManager(MysqlDatabase database) {
        super(database);
    }

    public void setCrewManager(CrewManager crewManager) {
        this.crewManager = crewManager;
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
                addOrUpdateCrews(horse);
                return;
            }
            throw new SQLException("Horse not added");
        }
    }

    private void addOrUpdateCrews(Horse horse) throws SQLException {
//        List<Crew> crews = horse.getCrews();
//        for (int i = 0; i < crews.size(); i++) {
//            Crew crew = crews.get(i);
//            if (crew.getId() == -1) {
//                crewManager.add(crew);
//            } else {
//                crewManager.update(crew);
//            }
//        }
    }

//    public Horse getByIdWithRelatedObjects(int id) throws SQLException {
//        try (Connection conn = database.getConnection()) {
//            String sql = "SELECT * FROM horses JOIN crews ON (horses.id = horse_id) WHERE horses.id = ?";
//            PreparedStatement statement = conn.prepareStatement(sql);
//            statement.setInt(1, id);
//
//            ResultSet result = statement.executeQuery();
//            Horse horse = null;
//            if (result.next()) {
//                horse = new Horse(
//                        result.getInt("horses.id"),
//                        result.getString("name"),
//                        result.getInt("birth_year"),
//                        result.getInt("experience"),
//                        result.getString("owner"),
//                        result.getInt("price")
//                );
//                List<Crew> crews = new ArrayList<>();
//                while (result.next()) {
//                    int crewId = result.getInt("crews.id");
//                    int number = result.getInt("number");
//                    int horseId = result.getInt("horse_id");
//                    int userId = result.getInt("user_id");
//                    System.out.println("Crew");
//                    System.out.println(crewId);
//                    System.out.println(number);
//                    System.out.println(horseId);
//                    System.out.println(userId);
//
//                }
//            }
//            return horse;
//        }
//    }

    public Horse getByName(String name) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT * FROM horses WHERE name=?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name);

            ResultSet result = statement.executeQuery();
            return getEntityFromResultSet(result);
        }
    }

    @Override
    public List<Horse> getAll() throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT * FROM horses ORDER BY name";
            Statement statement = conn.createStatement();

            ResultSet result = statement.executeQuery(sql);
            return getEntityListFromResultSet(result);
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
    public Horse createEntity() {
        return new Horse();
    }

    @Override
    public String getTableName() {
        return "horses";
    }

    public int deleteByName(String name) throws SQLException {
        try (Connection c = database.getConnection()) {
            String sql = "DELETE FROM horses WHERE name=?";

            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, name);

            return s.executeUpdate();
        }
    }

    @Override
    protected Horse getEntityFromResultSet(ResultSet result) throws SQLException {
        Horse horse = null;
        if (result.next()) {
            horse = new Horse(
                    result.getInt("id"),
                    result.getString("name"),
                    result.getInt("birth_year"),
                    result.getInt("experience"),
                    result.getString("owner"),
                    result.getInt("price")
            );
        }
        return horse;
    }

    public List<Horse> getAllHorsesThatAvailableInCompetition(Competition comp) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT DISTINCT horses.* " +
                    "FROM horses " +
                    "where horses.id not in (" +
                    "select distinct h.id from horses h " +
                    "join crews on h.id = crews.horse_id " +
                    "join races r on crews.id = r.crew_id " +
                    "join competitions comp on comp.id = r.competition_id " +
                    "where comp.id = ?" +
                    ")";
            PreparedStatement s = conn.prepareStatement(sql);
            s.setInt(1, comp.getId());

            ResultSet result = s.executeQuery();
            return getEntityListFromResultSet(result);
        }
    }
}
