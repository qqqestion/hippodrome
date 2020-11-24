package ru.emoji.tashkent;

import ru.emoji.tashkent.ui.ModelListForm;
import ru.emoji.tashkent.utils.BaseForm;
import ru.emoji.tashkent.utils.MysqlDatabase;

import java.sql.Connection;
import java.sql.SQLException;

public class Application {
    private final MysqlDatabase database = new MysqlDatabase("localhost", "hippodrome", "newuser", "password");
    private static Application instance;

    private Application() {
        instance = this;

        initDatabase();

        new ModelListForm();
    }

    private void initDatabase() {
        try (Connection conn = database.getConnection()) {
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Application();
    }

    public static Application getInstance() {
        return instance;
    }

    private void initUi() {
        BaseForm.setBaseApplicationTitle("Медицинский центр ТРУБОЧИСТ");
    }

    public MysqlDatabase getDatabase() {
        return database;
    }
}
