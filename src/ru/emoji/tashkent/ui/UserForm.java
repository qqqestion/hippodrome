package ru.emoji.tashkent.ui;

import ru.emoji.tashkent.Application;
import ru.emoji.tashkent.database.entity.User;
import ru.emoji.tashkent.database.manager.UserManager;
import ru.emoji.tashkent.utils.BaseForm;
import ru.emoji.tashkent.utils.MysqlDatabase;

import javax.swing.*;
import java.sql.SQLException;

public class UserForm extends BaseForm {
    private final ModelListForm mainForm;
    private final Action action;
    private JPanel mainPanel;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField birthYearField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton executeButton;
    private JButton cancelButton;
    private JTextField idField;
    private JLabel firstNameLabel;
    private JLabel idLabel;
    private JLabel lastNameLabel;
    private JLabel birthYearLabel;
    private JLabel emailLabel;
    private JLabel passwordLabel;

    private MysqlDatabase database = Application.getInstance().getDatabase();

    public UserForm(ModelListForm mainForm, Action action) {
        this.mainForm = mainForm;
        this.action = action;
        setContentPane(mainPanel);
        setVisible(true);

        initButtons();
    }

    private void initButtons() {
        executeButton.addActionListener(e -> execute());
        cancelButton.addActionListener(e -> cancel());
    }

    private void cancel() {
        dispose();
//        JOptionPane.showMessageDialog(mainForm, "Eggs are not supposed to be green.");
        mainForm.unfreezeForm();
    }

    private void execute() {
        try {
            switch (action) {
                case CREATE -> create();
                case UPDATE -> update();
                case DELETE -> delete();
            }
            cancel();
        } catch (SQLException |NumberFormatException exp) {
            exp.printStackTrace();
        }
    }

    private void create() throws SQLException, NumberFormatException {
        User user = new User(
                firstNameField.getText(),
                lastNameField.getText(),
                Integer.parseInt(birthYearField.getText()),
                emailField.getText(),
                String.valueOf(passwordField.getPassword())
        );
        if (user.isValid()) {
            UserManager manager = new UserManager(database);
            manager.add(user);
        }
    }

    private void update() throws SQLException, NumberFormatException {
        User user = new User(
                Integer.parseInt(idField.getText()),
                firstNameField.getText(),
                lastNameField.getText(),
                Integer.parseInt(birthYearField.getText()),
                emailField.getText(),
                String.valueOf(passwordField.getPassword())
        );
        if (user.isValid()) {
            UserManager manager = new UserManager(database);
            manager.update(user);
        }
    }

    private void delete() throws SQLException, NumberFormatException {
        UserManager manager = new UserManager(database);
        manager.deleteById(Integer.parseInt(idField.getText()));
    }

    @Override
    public int getFormWidth() {
        return 500;
    }

    @Override
    public int getFormHeight() {
        return 300;
    }

    enum Action {
        CREATE,
        UPDATE,
        DELETE
    }
}
