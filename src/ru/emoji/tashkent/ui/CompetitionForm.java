package ru.emoji.tashkent.ui;

import ru.emoji.tashkent.Application;
import ru.emoji.tashkent.ActionEnum;
import ru.emoji.tashkent.database.entity.User;
import ru.emoji.tashkent.database.manager.CompetitionManager;
import ru.emoji.tashkent.utils.MainForm;
import ru.emoji.tashkent.utils.Utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class CompetitionForm extends MainForm implements ActionListener {
    private JPanel mainPanel;
    private JLabel upcomingCompetitionLabel;
    private JLabel currentCompetitionLabel;
    private JLabel pastCompetitionLabel;
    private JTextPane upcomingTextPane;
    private JTextPane currentTextPane;
    private JTextPane pastTextPane;
    private JComboBox actionComboBox;

    CompetitionManager competitionManager = new CompetitionManager(Application.getInstance().getDatabase());

    public CompetitionForm() {
        super(ActionEnum.SHOW_COMPETITIONS);
        setContentPane(mainPanel);
        initComboBox();
        LocalDateTime today = LocalDateTime.now();
        try {
            upcomingTextPane.setText(Utils.fromCollectionToString(
                    competitionManager.getUpcomingCompetitions(today), "\n"));
            currentTextPane.setText(Utils.fromCollectionToString(
                    competitionManager.getCurrentCompetitions(today), "\n"));
            pastTextPane.setText(Utils.fromCollectionToString(
                    competitionManager.getFinishedCompetitions(today), "\n"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        setVisible(true);
        pack();
    }

    private void initComboBox() {
        actionComboBox.addItem(ActionEnum.SHOW_PROFILE);
        actionComboBox.addItem(ActionEnum.SHOW_JOCKEYS);
        actionComboBox.addItem(ActionEnum.SHOW_HORSES);
        actionComboBox.addItem(ActionEnum.SHOW_COMPETITIONS);
        actionComboBox.addItem(ActionEnum.SHOW_HIPPODROMES);

        actionComboBox.setSelectedItem(ActionEnum.SHOW_COMPETITIONS);
        actionComboBox.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ActionEnum action = (ActionEnum) actionComboBox.getSelectedItem();
        switch (action) {
            case SHOW_PROFILE:
                dispose();
                new ProfileForm();
                break;
            case SHOW_JOCKEYS:
                break;
            case SHOW_HORSES:
                break;
            case SHOW_COMPETITIONS:
                return;
            case SHOW_HIPPODROMES:
                break;
        }
    }
}
