package ru.emoji.tashkent.ui;

import ru.emoji.tashkent.Application;
import ru.emoji.tashkent.database.entity.User;
import ru.emoji.tashkent.database.manager.UserManager;
import ru.emoji.tashkent.utils.ActionOnEnterListener;
import ru.emoji.tashkent.utils.BaseForm;
import ru.emoji.tashkent.utils.DatabaseUsage;
import ru.emoji.tashkent.utils.OnKeyClickListenerListener;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;

public class LoginForm extends BaseForm
        implements ItemListener, DatabaseUsage, OnKeyClickListenerListener {
    private JPanel mainPanel;

    private final UserManager manager = new UserManager(Application.getInstance().getDatabase());

    private JLabel passwordLabel;
    private JLabel emailLabel;

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    private JCheckBox showPasswordCheckBox;


    public LoginForm() {
        setContentPane(mainPanel);
        setVisible(true);
        showPasswordCheckBox.addItemListener(this);
        initButtons();
        initFields();
    }

    private void initFields() {
        ActionOnEnterListener keyListener = new ActionOnEnterListener(this);
        emailField.addKeyListener(keyListener);
        passwordField.addKeyListener(keyListener);
    }

    private void initButtons() {
        loginButton.addActionListener(e -> login());
        cancelButton.addActionListener(e -> cancel());
    }

    private void cancel() {
        dispose();
        new StartForm();
    }

    private void login() {
        User user = null;
        try {
//            String email = emailField.getText();
//            String password = new String(passwordField.getPassword());
            String email = "admin@email.com";
            String password = "Aa123456789";
            user = manager.getByEmailAndPassword(email, password);
        } catch (SQLException e) {
            e.printStackTrace();
            showError(this);
        }
        if (user != null) {
            Application.getInstance().setUser(user);
            dispose();
            new CompetitionForm();
        } else {
            JOptionPane.showMessageDialog(this, "Почта или пароль неправильны");
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

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            passwordField.setEchoChar((char) 0);
        } else {
            passwordField.setEchoChar('●');
        }
    }

    @Override
    public void onSave() {
        login();
    }
}
