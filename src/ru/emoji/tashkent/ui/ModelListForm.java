package ru.emoji.tashkent.ui;

import ru.emoji.tashkent.Application;
import ru.emoji.tashkent.database.entity.User;
import ru.emoji.tashkent.database.manager.UserManager;
import ru.emoji.tashkent.utils.BaseForm;
import ru.emoji.tashkent.utils.MysqlDatabase;

import javax.swing.*;
import java.sql.SQLException;


public class ModelListForm extends BaseForm {
    private JPanel mainPanel;
    private JTextArea textArea;
    private JButton newButton;
    private JButton updateButton;
    private JButton deleteButton;
    private MysqlDatabase database = Application.getInstance().getDatabase();

    public ModelListForm() {
        super();
        setContentPane(mainPanel);
        initButtons();
        try {
            updateForm();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        setVisible(true);
    }

    private void initButtons() {
        newButton.addActionListener(e -> newUser(UserForm.Action.CREATE));
        updateButton.addActionListener(e -> newUser(UserForm.Action.UPDATE));
        deleteButton.addActionListener(e -> newUser(UserForm.Action.DELETE));
    }

    private void newUser(UserForm.Action action) {
        new UserForm(this, action);
        freezeForm();
    }

    private void updateForm() throws SQLException {
        StringBuilder sb = new StringBuilder("");
        for (User user : new UserManager(database).getAll()) {
            sb.append(user.toString()).append('\n');
        }
        textArea.setText(sb.toString());
    }

    public void freezeForm() {
        setFocusable(false);
        setEnabled(false);
    }

    public void unfreezeForm() {
        setEnabled(true);
        setFocusable(true);
        try {
            updateForm();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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
