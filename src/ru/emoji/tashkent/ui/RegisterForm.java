package ru.emoji.tashkent.ui;

import ru.emoji.tashkent.Application;
import ru.emoji.tashkent.database.entity.User;
import ru.emoji.tashkent.database.manager.UserManager;
import ru.emoji.tashkent.utils.BaseForm;
import ru.emoji.tashkent.utils.MysqlDatabase;

import javax.swing.*;
import java.sql.SQLException;

public class RegisterForm extends BaseForm {
    private JPanel mainPanel;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField birthYearField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton cancelButton;
    private JLabel firstNameLabel;
    private JLabel lastNameLabel;
    private JLabel birthYearLabel;
    private JLabel emailLabel;
    private JLabel passwordLabel;

    private MysqlDatabase database = Application.getInstance().getDatabase();

    public RegisterForm() {
        setContentPane(mainPanel);
        initButtons();
        setVisible(true);
    }

    private void initButtons() {
        registerButton.addActionListener(e -> register());
        cancelButton.addActionListener(e -> cancel());
    }

    private void cancel() {
        dispose();
        new StartForm();
    }

    private void register() {
        User user = new User(
                firstNameField.getText(),
                lastNameField.getText(),
                Integer.parseInt(birthYearField.getText()),
                emailField.getText(),
                String.valueOf(passwordField.getPassword()),
                true
        );
        if (user.isValid()) {
            UserManager manager = new UserManager(database);
            try {
                manager.add(user);
                new MainForm(user);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Введенные значения не корректны",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public int getFormWidth() {
        return 500;
    }

    @Override
    public int getFormHeight() {
        return 300;
    }

}
