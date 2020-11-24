package ru.emoji.tashkent.utils;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class MysqlDatabase {
    private final String address;
    private final int port;
    private final String db;
    private final String user;
    private final String password;

    private MysqlDataSource source;

    public MysqlDatabase(String address, int port, String db, String user, String password) {
        this.address = address;
        this.port = port;
        this.db = db;
        this.user = user;
        this.password = password;
    }

    public MysqlDatabase(String address, String db, String user, String password) {
        this(address, 3306, db, user, password);
    }

    public Connection getConnection() throws SQLException {
        if (source == null) {
            source = new MysqlDataSource();

            source.setServerName(address);
            source.setPort(port);
            source.setDatabaseName(db);
            source.setUser(user);
            source.setPassword(password);

            source.setCharacterEncoding("UTF-8");
            source.setServerTimezone("UTC");
        }
        return source.getConnection();
    }
}
