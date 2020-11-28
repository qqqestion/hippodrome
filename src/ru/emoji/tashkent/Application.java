package ru.emoji.tashkent;

import ru.emoji.tashkent.database.entity.User;
import ru.emoji.tashkent.database.manager.CompetitionManager;
import ru.emoji.tashkent.database.manager.UserManager;
import ru.emoji.tashkent.ui.LoginForm;
import ru.emoji.tashkent.ui.MainForm;
import ru.emoji.tashkent.ui.ProfileForm;
import ru.emoji.tashkent.ui.StartForm;
import ru.emoji.tashkent.utils.BaseForm;
import ru.emoji.tashkent.utils.MysqlDatabase;

import java.sql.Connection;
import java.sql.SQLException;

public class Application {
    private final MysqlDatabase database = new MysqlDatabase("localhost", "hippodrome", "newuser", "password");
    private static Application instance;

    private final UserManager userManager = new UserManager(database);

    private Application() {
        instance = this;

        initDatabase();
        initUi();

        String email = "admin@email.com";
        String password = "Aa123456789";
        User user = null;
        try {
            user = userManager.getByEmailAndPassword(email, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        new ProfileForm(user);
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
        BaseForm.setBaseApplicationTitle("Ипподром для пацанвы");
    }

    public MysqlDatabase getDatabase() {
        return database;
    }
}
