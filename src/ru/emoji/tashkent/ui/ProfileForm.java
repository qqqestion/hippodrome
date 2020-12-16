package ru.emoji.tashkent.ui;

import ru.emoji.tashkent.Application;
import ru.emoji.tashkent.enums.ActionEnum;
import ru.emoji.tashkent.database.entity.User;
import ru.emoji.tashkent.database.manager.UserManager;
import ru.emoji.tashkent.utils.MainForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ProfileForm extends MainForm implements ActionListener {
    private JPanel mainPanel;
    private JTextField firstNameField;
    private JTextField birthYearField;
    private JTextField emailField;
    private JButton changePasswordButton;
    private JButton saveButton;
    private JComboBox actionComboBox;
    private JTextField lastNameField;

    private final User user = Application.getInstance().getUser();
    private final UserManager manager = new UserManager(Application.getInstance().getDatabase());

    public ProfileForm() {
        super(ActionEnum.SHOW_PROFILE);
        setContentPane(mainPanel);
        setVisible(true);
        initBoxes();
        initFields();
        initButtons();
    }

    private void initBoxes() {
        initMenu(actionComboBox);
        actionComboBox.addActionListener(this);
    }

    private void initFields() {
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        birthYearField.setText(String.valueOf(user.getBirthYear()));
        emailField.setText(user.getEmail());
    }

    private void initButtons() {
        changePasswordButton.addActionListener(this);
        saveButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == actionComboBox) {
            ActionEnum newAction = (ActionEnum) actionComboBox.getSelectedItem();
            changeWindow(newAction);
        } else if (source == saveButton) {
            System.out.println("Save");
            try {
                save();
            } catch (SQLException | NumberFormatException exception) {
                System.out.println(exception.getMessage());
                JOptionPane.showMessageDialog(this,
                        "Введенные значения не корректны",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else if (source == changePasswordButton) {
            System.out.println("Change");
            changePassword();
        } else {
            throw new RuntimeException("unknown event");
        }
    }

    private void changePassword() {
        String currentPassword = (String) JOptionPane.showInputDialog(
                this,
                "Введите текущий пароль",
                "Введите текущий пароль",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "");
        if (currentPassword == null) {
            return;
        }
        if (!user.getPassword().equals(currentPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Неверный пароль", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String newPassword = (String) JOptionPane.showInputDialog(
                this,
                "Введите новый пароль",
                "Введите новый пароль",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "");
        user.setPassword(newPassword);
        try {
            manager.update(user);
            JOptionPane.showMessageDialog(this,
                    "Вы успешно обновили пароль!", "Успешно", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Ошибка", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void save() throws SQLException, NumberFormatException {
        user.setEmail(emailField.getText());
        user.setBirthYear(Integer.parseInt(birthYearField.getText()));
        user.setFirstName(firstNameField.getText());
        user.setLastName(lastNameField.getText());
        if (user.isValid()) {
            manager.update(user);
            JOptionPane.showMessageDialog(this,
                    "Вы успешно изменили профиль",
                    "Успешное изменение",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            throw new SQLException("user is not valid");
        }
    }
}
