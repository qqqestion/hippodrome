package ru.emoji.tashkent.ui;

import ru.emoji.tashkent.ActionEnum;
import ru.emoji.tashkent.Application;
import ru.emoji.tashkent.database.entity.User;
import ru.emoji.tashkent.database.manager.UserManager;
import ru.emoji.tashkent.utils.ModelViewForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class JockeyForm extends
        ModelViewForm<User> implements ActionListener {
    private JPanel mainPanel;
    private JComboBox actionBox;
    private JComboBox<User> jockeyBox;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField birthYearField;
    private JTextField emailField;


    public JockeyForm() {
        super(ActionEnum.SHOW_JOCKEYS,
                new UserManager(Application.getInstance().getDatabase()));

        JTextField[] fields = new JTextField[]{firstNameField,
                lastNameField, birthYearField, emailField};
        setFields(fields);
        setSearchField(jockeyBox);
        setCanBeEdited(false);
        initSearchFieldAndFields();

        initBoxes();
        setContentPane(mainPanel);
        setVisible(true);
    }

    private void initBoxes() {
        initMenu(actionBox);
        actionBox.addActionListener(this);
        jockeyBox.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == actionBox) {
            ActionEnum newAction = (ActionEnum) actionBox.getSelectedItem();
            changeWindow(newAction);
        } else if (source == jockeyBox) {
//            initFields();
            updateFields();
            System.out.println(jockeyBox.getSelectedItem());
        }
    }

    @Override
    protected List<String> getValuesFromModel(User element) {
        return List.of(element.getFirstName(), element.getLastName(),
                String.valueOf(element.getBirthYear()), element.getEmail());
    }
}
