package ru.emoji.tashkent.ui;


import ru.emoji.tashkent.utils.BaseForm;

import javax.swing.*;

public class StartForm extends BaseForm {
    private JPanel mainPanel;
    private JButton loginButton;
    private JButton registerButton;

    public StartForm() {
        setContentPane(mainPanel);

        loginButton.addActionListener(e -> {
            dispose();
            new LoginForm();
        });

        registerButton.addActionListener(e -> {
            dispose();
            new RegisterForm();
        });

        setVisible(true);
    }

    @Override
    public int getFormWidth() {
        return 300;
    }

    @Override
    public int getFormHeight() {
        return 300;
    }
}
