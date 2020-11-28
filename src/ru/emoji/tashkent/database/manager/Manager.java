package ru.emoji.tashkent.database.manager;

import ru.emoji.tashkent.utils.MysqlDatabase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class Manager<E> {
    protected MysqlDatabase database;

    public Manager(MysqlDatabase database) {
        this.database = database;
    }

    abstract void add(E object) throws SQLException;

    abstract E getById(int id) throws SQLException;

    abstract public List<E> getAll() throws SQLException;

    abstract int update(E object) throws SQLException;

    abstract int delete(E object) throws SQLException;

    abstract String getTableName();
}
