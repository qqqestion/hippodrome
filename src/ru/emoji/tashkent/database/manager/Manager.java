package ru.emoji.tashkent.database.manager;

import ru.emoji.tashkent.database.entity.User;
import ru.emoji.tashkent.utils.MysqlDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Manager<E> {
    protected MysqlDatabase database;

    public Manager(MysqlDatabase database) {
        this.database = database;
    }

    public abstract void add(E object) throws SQLException;

    public E getById(int id) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT * FROM " + getTableName() + " WHERE id = ?";
            PreparedStatement s = conn.prepareStatement(sql);
            s.setInt(1, id);
            ResultSet result = s.executeQuery();
            return getEntityFromResultSet(result);
        }
    }

    public List<E> getAll() throws SQLException {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT * FROM " + getTableName();
            Statement s = conn.createStatement();

            ResultSet result = s.executeQuery(sql);
            return getEntityListFromResultSet(result);
        }
    }

    public abstract int update(E object) throws SQLException;

    public abstract int delete(E object) throws SQLException;

    public int deleteById(int id) throws SQLException {
        try (Connection c = database.getConnection()) {
            String sql = "DELETE FROM " + getTableName() + " WHERE id=?";

            PreparedStatement s = c.prepareStatement(sql);
            s.setInt(1, id);

            return s.executeUpdate();
        }
    }

    public abstract E createEntity();

    protected abstract E getEntityFromResultSet(ResultSet result) throws SQLException;

    protected List<E> getEntityListFromResultSet(ResultSet result) throws SQLException {
        List<E> list = new ArrayList<>();
        E buff;
        while ((buff = getEntityFromResultSet(result)) != null) {
            list.add(buff);
        }
        return list;
    }

    abstract String getTableName();
}
