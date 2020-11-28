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
        initMenu(actionComboBox);
        actionComboBox.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ActionEnum newAction = (ActionEnum) actionComboBox.getSelectedItem();
        changeWindow(newAction);
    }
}
