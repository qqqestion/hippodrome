package ru.emoji.tashkent.ui;

import ru.emoji.tashkent.enums.ActionEnum;
import ru.emoji.tashkent.Application;
import ru.emoji.tashkent.database.entity.Competition;
import ru.emoji.tashkent.database.entity.User;
import ru.emoji.tashkent.database.manager.CompetitionManager;
import ru.emoji.tashkent.database.manager.RaceManager;
import ru.emoji.tashkent.database.manager.UserManager;
import ru.emoji.tashkent.enums.DisplayTypeEnum;
import ru.emoji.tashkent.utils.DateUtils;
import ru.emoji.tashkent.utils.DisplayCompetitionRaces;
import ru.emoji.tashkent.utils.ModelViewForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class UserForm extends
        ModelViewForm<User> implements ActionListener, DisplayCompetitionRaces {
    private JPanel mainPanel;
    private JComboBox<ActionEnum> actionBox;
    private JComboBox<User> userBox;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField birthYearField;
    private JTextField emailField;
    private JTextPane statisticPane;
    private JComboBox<Competition> competitionBox;

    private CompetitionManager competitionManager;
    private RaceManager raceManager;


    public UserForm() {
        super(ActionEnum.SHOW_JOCKEYS,
                new UserManager(Application.getInstance().getDatabase()));
        competitionManager = new CompetitionManager(Application.getInstance().getDatabase());
        raceManager = new RaceManager(Application.getInstance().getDatabase());

        setContentPane(mainPanel);

        JTextField[] fields = new JTextField[]{firstNameField,
                lastNameField, birthYearField, emailField};
        setFields(fields);
        setSearchField(userBox);
        setCanBeEdited(false);
        initSearchFieldAndFields();

        initBoxes();

        setCompetitionsToTheBox();
        updateCompetitionInfo();

        setVisible(true);
    }

    private void initBoxes() {
        initMenu(actionBox);
        actionBox.addActionListener(this);
        userBox.addActionListener(this);
        competitionBox.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == actionBox) {
            ActionEnum newAction = (ActionEnum) actionBox.getSelectedItem();
            changeWindow(newAction);
        } else if (source == userBox) {
            updateFields();
            setCompetitionsToTheBox();
            updateCompetitionInfo();
        } else if (source == competitionBox) {
            updateCompetitionInfo();
        }
    }

    private void setCompetitionsToTheBox() {
        competitionBox.removeAllItems();
        User user = (User) userBox.getSelectedItem();
        if (user == null || user.getId() == -1) {
            System.out.println("There is no horse");
            return;
        }
        List<Competition> competitions;
        try {
            competitions = competitionManager.getCompetitionsThatUserEnteredIn(user);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return;
        }
//        System.out.println("UserForm::setCompetitionsToTheBox");
        for (Competition comp : competitions) {
//            System.out.println(comp);
            competitionBox.addItem(comp);
        }
    }

    private void updateCompetitionInfo() {
        Competition selectedComp = (Competition) competitionBox.getSelectedItem();
        System.out.println("Selected " + selectedComp);
        if (selectedComp == null) {
            statisticPane.setText("Соревнование не выбрано");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Имя: ").append(selectedComp.getName()).append('\n')
                .append("Призовой фонд: ").append(selectedComp.getFund()).append(" рублей\n")
                .append("Дата начала: ").append(selectedComp.getDateStart().format(DateUtils.MYSQL_FORMATTER)).append("\n")
                .append("Дата конца: ").append(selectedComp.getDateEnd().format(DateUtils.MYSQL_FORMATTER)).append("\n")
                .append("Забеги: \n");
        setParticipant(raceManager, selectedComp, statisticPane, sb, DisplayTypeEnum.DISPLAY_USER);
    }


    @Override
    protected List<String> getValuesFromModel(User element) {
        return List.of(element.getFirstName(), element.getLastName(),
                String.valueOf(element.getBirthYear()), element.getEmail());
    }
}
