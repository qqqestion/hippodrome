package ru.emoji.tashkent.ui;

import ru.emoji.tashkent.Application;
import ru.emoji.tashkent.database.ActionEnum;
import ru.emoji.tashkent.database.entity.User;
import ru.emoji.tashkent.database.manager.UserManager;
import ru.emoji.tashkent.utils.BaseForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ProfileForm extends BaseForm implements ActionListener {
    private JPanel mainPanel;
    private JTextField firstNameField;
    private JTextField birthYearField;
    private JTextField emailField;
    private JButton changePasswordButton;
    private JButton saveButton;
    private JComboBox actionComboBox;
    private JTextField lastNameField;

    private final User authUser;
    private final UserManager manager = new UserManager(Application.getInstance().getDatabase());

    public ProfileForm(User authUser) {
        this.authUser = authUser;
        setContentPane(mainPanel);
        setVisible(true);
        initBoxes();
        initFields();
        initButtons();
    }

    private void initBoxes() {
        actionComboBox.addItem(ActionEnum.SHOW_PROFILE);
        actionComboBox.addItem(ActionEnum.SHOW_JOCKEYS);
        actionComboBox.addItem(ActionEnum.SHOW_HORSES);
        actionComboBox.addItem(ActionEnum.SHOW_COMPETITIONS);
        actionComboBox.addItem(ActionEnum.SHOW_HIPPODROMES);

        actionComboBox.setSelectedItem(ActionEnum.SHOW_PROFILE);
        actionComboBox.addActionListener(this);
    }

    private void initFields() {
        firstNameField.setText(authUser.getFirstName());
        lastNameField.setText(authUser.getLastName());
        birthYearField.setText(String.valueOf(authUser.getBirthYear()));
        emailField.setText(authUser.getEmail());
    }

    private void initButtons() {
        changePasswordButton.addActionListener(this);
        saveButton.addActionListener(this);
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
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == actionComboBox) {
            ActionEnum action = (ActionEnum) actionComboBox.getSelectedItem();
            switch (action) {
                case SHOW_PROFILE:
                    return;
                case SHOW_JOCKEYS:
                    break;
                case SHOW_HORSES:
                    break;
                case SHOW_COMPETITIONS:
                    dispose();
                    new MainForm(authUser);
                case SHOW_HIPPODROMES:
                    break;
            }
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

    private void resetPassword() {

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
        if (!authUser.getPassword().equals(currentPassword)) {
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
        authUser.setPassword(newPassword);
        try {
            manager.update(authUser);
            JOptionPane.showMessageDialog(this,
                    "Вы успешно обновили пароль!", "Успешно", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Ошибка", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void save() throws SQLException, NumberFormatException {
        authUser.setEmail(emailField.getText());
        authUser.setBirthYear(Integer.parseInt(birthYearField.getText()));
        authUser.setFirstName(firstNameField.getText());
        authUser.setLastName(lastNameField.getText());
        if (authUser.isValid()) {
            manager.update(authUser);
            JOptionPane.showMessageDialog(this,
                    "Вы успешно изменили профиль",
                    "Успешное изменение",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            throw new SQLException("user is not valid");
        }
    }
}
