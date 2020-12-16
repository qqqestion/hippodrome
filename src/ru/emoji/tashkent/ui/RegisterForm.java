package ru.emoji.tashkent.ui;

import ru.emoji.tashkent.Application;
import ru.emoji.tashkent.database.entity.User;
import ru.emoji.tashkent.database.manager.UserManager;
import ru.emoji.tashkent.utils.*;

import javax.swing.*;
import java.sql.SQLException;

public class RegisterForm extends BaseForm
        implements DatabaseUsage, OnKeyClickListenerListener {
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
        initFields();
        setVisible(true);
    }

    private void initFields() {
        ActionOnEnterListener keyListener = new ActionOnEnterListener(this);

        firstNameField.addKeyListener(keyListener);
        lastNameField.addKeyListener(keyListener);
        birthYearField.addKeyListener(keyListener);
        emailField.addKeyListener(keyListener);
        passwordField.addKeyListener(keyListener);
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
        User user;
        try {
            user = new User(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    Integer.parseInt(birthYearField.getText()),
                    emailField.getText(),
                    String.valueOf(passwordField.getPassword()),
                    false
            );
            if (user.isValid()) {
                UserManager manager = new UserManager(database);
                manager.add(user);
            }
        } catch (NumberFormatException | SQLException exp) {
            exp.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Введенные значения не корректны",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            return;

        }
        Application.getInstance().setUser(user);
        new CompetitionForm();
    }

    @Override
    public int getFormWidth() {
        return 500;
    }

    @Override
    public int getFormHeight() {
        return 300;
    }

    @Override
    public void onSave() {
        register();
    }
}
