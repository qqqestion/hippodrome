package ru.emoji.tashkent.ui;

import ru.emoji.tashkent.Application;
import ru.emoji.tashkent.enums.ActionEnum;
import ru.emoji.tashkent.database.entity.Competition;
import ru.emoji.tashkent.database.entity.Crew;
import ru.emoji.tashkent.database.entity.Horse;
import ru.emoji.tashkent.database.entity.Race;
import ru.emoji.tashkent.database.manager.CompetitionManager;
import ru.emoji.tashkent.database.manager.CrewManager;
import ru.emoji.tashkent.database.manager.HorseManager;
import ru.emoji.tashkent.database.manager.RaceManager;
import ru.emoji.tashkent.enums.DisplayTypeEnum;
import ru.emoji.tashkent.utils.DateUtils;
import ru.emoji.tashkent.utils.DialogUtil;
import ru.emoji.tashkent.utils.DisplayCompetitionRaces;
import ru.emoji.tashkent.utils.ModelViewForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CompetitionForm
        extends ModelViewForm<Competition>
        implements ActionListener, DisplayCompetitionRaces {
    private JPanel mainPanel;
    private JComboBox actionBox;
    private JComboBox competitionBox;
    private JTextField nameField;
    private JTextField fundField;
    private JTextField dateStartField;
    private JTextField dateEndField;
    private JTextPane participantPane;
    private JButton deleteButton;
    private JButton enterCompButton;

    private RaceManager raceManager;
    private HorseManager horseManager;

    public CompetitionForm() {
        super(ActionEnum.SHOW_COMPETITIONS,
                new CompetitionManager(Application.getInstance().getDatabase()));
        raceManager = new RaceManager(Application.getInstance().getDatabase());
        horseManager = new HorseManager(Application.getInstance().getDatabase());

        setContentPane(mainPanel);
        setSearchField(competitionBox);
        initBoxes();
        JTextField[] fields = new JTextField[]{nameField,
                fundField, dateStartField, dateEndField};
        setFields(fields);
        setSearchField(competitionBox);
        initSearchFieldAndFields();

        updateParticipants();

//        LocalDateTime today = LocalDateTime.now();
//        try {
//            upcomingTextPane.setText(Utils.fromCollectionToString(
//                    competitionManager.getUpcomingCompetitions(today), "\n"));
//            currentTextPane.setText(Utils.fromCollectionToString(
//                    competitionManager.getCurrentCompetitions(today), "\n"));
//            pastTextPane.setText(Utils.fromCollectionToString(
//                    competitionManager.getFinishedCompetitions(today), "\n"));
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }

        initDeleteButton(deleteButton);
        initEnterButton();

        setVisible(true);
        pack();
    }

    private void initEnterButton() {
        enterCompButton.addActionListener(this);
        updateEnterButton();
    }

    private void updateEnterButton() {
        Competition selectedComp = (Competition) competitionBox.getSelectedItem();
        if (selectedComp == null) {
            return;
        }
        List<Competition> competitions;
        try {
            competitions = ((CompetitionManager) manager).getCompetitionsThatUserEnteredIn(user);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            showError(this);
            return;
        }
        boolean canBeEntered = !competitions.contains(selectedComp) && selectedComp.isStarted();
        if (!canBeEntered) {
            enterCompButton.setEnabled(false);
            enterCompButton.setVisible(false);
        } else {
            enterCompButton.setEnabled(true);
            enterCompButton.setVisible(true);
        }
    }

    private void updateParticipants() {
        Competition selectedComp = (Competition) competitionBox.getSelectedItem();
        if (selectedComp == null) {
            participantPane.setText("Соревнование не выбрано");
            return;
        }
        setParticipant(raceManager, selectedComp, participantPane, DisplayTypeEnum.DISPLAY_CREW);
    }

    private void initBoxes() {
        initMenu(actionBox);
        actionBox.addActionListener(this);
        competitionBox.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == actionBox) {
            ActionEnum newAction = (ActionEnum) actionBox.getSelectedItem();
            changeWindow(newAction);
        } else if (source == competitionBox) {
            updateFields();
            updateParticipants();
            updateEnterButton();
        } else if (source == enterCompButton) {
            enterCompetition();
        }
    }

    private void enterCompetition() {
        List<Horse> horses;
        Competition selectedComp = (Competition) competitionBox.getSelectedItem();
        try {
            horses = horseManager.getAllHorsesThatAvailableInCompetition(selectedComp);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return;
        }
        if (horses.isEmpty()) {
            DialogUtil.showInfo(this, "К сожалению, нет доступных лошадей");
            return;
        }
        Horse horse = (Horse) JOptionPane.showInputDialog(
                this,
                "Выберете лошадь",
                "Информация",
                JOptionPane.INFORMATION_MESSAGE,
                null,
                horses.toArray(),
                horses.get(0));
        if (horse == null) {
            return;
        }
        CrewManager crewManager = new CrewManager(Application.getInstance().getDatabase());
        int number;
        while (true) {
            String rawNumber = JOptionPane.showInputDialog(this, "Выберете номер", "");
            if (rawNumber == null) {
                return;
            }
            try {
                number = Integer.parseInt(rawNumber);
                break;
            } catch (NumberFormatException exp) {
                DialogUtil.showError(this, "Введено некоректное число");
            }
        }
        Crew crewToFind = new Crew(
                number,
                horse,
                user
        );
        try {
            if (!crewManager.isExist(crewToFind)) {
                crewManager.add(crewToFind);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            showError(this);
            return;
        }
        try {
            List<Race> races = raceManager.getByCompetitionId(selectedComp.getId());
            int lastTrack = 1;
            int lastRaceNumber = 1;
            for (Race race : races) {
                if (race.getTrack() == lastTrack &&
                        lastRaceNumber == race.getRaceNumber()) {
                    lastTrack++;
                    if (lastTrack == 7) {
                        lastTrack = 1;
                        lastRaceNumber++;
                    }
                }
            }
            Race newRace = new Race(lastTrack,
                    selectedComp, crewToFind, lastRaceNumber, 150, null, selectedComp.getDateStart());
            raceManager.add(newRace);
            DialogUtil.showInfo(this, "Вы успешно записаны на соревнование");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            showError(this);
            return;
        }
        updateParticipants();
        updateEnterButton();
    }

    @Override
    protected List<String> getValuesFromModel(Competition element) {
        if (element.getId() == -1) {
            return fillStringList();
        }
        return List.of(element.getName(),
                String.valueOf(element.getFund()),
                element.getDateStart().format(DateUtils.MYSQL_FORMATTER),
                element.getDateEnd().format(DateUtils.MYSQL_FORMATTER));
    }

    @Override
    public void onSave() {
        super.onSave();
        System.out.println("onSave");
        Competition competition = (Competition) competitionBox.getSelectedItem();
        try {
            competition.setName(nameField.getText());
            competition.setFund(Integer.valueOf(fundField.getText()));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            competition.setDateStart(LocalDateTime.parse(dateStartField.getText(), formatter));
            competition.setDateEnd(LocalDateTime.parse(dateEndField.getText(), formatter));
            String msg;
            if (competition.getId() == -1) {
                manager.add(competition);
                onItemAdded(competition.getId());
                msg = "Соревнование успешно добавлено";
            } else {
                manager.update(competition);
                msg = "Соревнование успешно обновлено";
            }
            DialogUtil.showInfo(this, msg);
        } catch (SQLException e) {
            e.printStackTrace();
            showError(this);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Введены неверные значения");
        }
    }
}
