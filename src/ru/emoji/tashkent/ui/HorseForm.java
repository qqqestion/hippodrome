package ru.emoji.tashkent.ui;

import ru.emoji.tashkent.enums.ActionEnum;
import ru.emoji.tashkent.Application;
import ru.emoji.tashkent.database.entity.*;
import ru.emoji.tashkent.database.manager.*;
import ru.emoji.tashkent.enums.DisplayTypeEnum;
import ru.emoji.tashkent.utils.DateUtils;
import ru.emoji.tashkent.utils.DialogUtil;
import ru.emoji.tashkent.utils.DisplayCompetitionRaces;
import ru.emoji.tashkent.utils.ModelViewForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class HorseForm extends ModelViewForm<Horse>
        implements ActionListener, DisplayCompetitionRaces {
    private JPanel mainPanel;
    private JComboBox<ActionEnum> actionBox;
    private JComboBox<Horse> horseBox;
    private JTextField nameField;
    private JTextField birthYearField;
    private JTextField experienceField;
    private JTextField ownerField;
    private JTextField priceField;
    private JButton deleteButton;
    private JTextPane statisticPane;
    private JComboBox<Competition> competitionBox;

    private CompetitionManager competitionManager;
    private RaceManager raceManager;


    public HorseForm() {
        super(ActionEnum.SHOW_HORSES,
                new HorseManager(Application.getInstance().getDatabase()));
        competitionManager = new CompetitionManager(Application.getInstance().getDatabase());
        raceManager = new RaceManager(Application.getInstance().getDatabase());

        setContentPane(mainPanel);
        setSearchField(horseBox);

        initBoxes();

        JTextField[] fields = new JTextField[]{nameField,
                birthYearField, experienceField, ownerField, priceField};
        setFields(fields);
        setSearchField(horseBox);
        initSearchFieldAndFields();

        initDeleteButton(deleteButton);

        setCompetitionsToTheBox();
        updateCompetitionInfo();

        setVisible(true);
    }

    private void initBoxes() {
        initMenu(actionBox);
        actionBox.addActionListener(this);
        horseBox.addActionListener(this);
        competitionBox.addActionListener(this);
    }

    @Override
    protected List<String> getValuesFromModel(Horse element) {
        if (element.getId() == -1) {
            return fillStringList();
        }
        return List.of(element.getName(),
                String.valueOf(element.getBirthYear()),
                String.valueOf(element.getExperience()),
                element.getOwner(),
                String.valueOf(element.getPrice()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == actionBox) {
            ActionEnum newAction = (ActionEnum) actionBox.getSelectedItem();
            changeWindow(newAction);
        } else if (source == horseBox) {
            updateFields();
            setCompetitionsToTheBox();
            updateCompetitionInfo();
        } else if (source == competitionBox) {
            updateCompetitionInfo();
        }
    }

    private void setCompetitionsToTheBox() {
        competitionBox.removeAllItems();
        Horse horse = (Horse) horseBox.getSelectedItem();
        if (horse == null || horse.getId() == -1) {
            System.out.println("There is no horse");
            return;
        }
        List<Competition> competitions;
        try {
            competitions = competitionManager.getCompetitionsThatHorseEnteredIn(horse);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return;
        }
//        System.out.println("HorseForm::setCompetitionsToTheBox");
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

        setParticipant(raceManager, selectedComp, statisticPane, sb, DisplayTypeEnum.DISPLAY_HORSE);
    }

    @Override
    public void onSave() {
        Horse horse = (Horse) horseBox.getSelectedItem();
        try {
            horse.setName(nameField.getText());
            horse.setBirthYear(Integer.parseInt(birthYearField.getText()));
            horse.setExperience(Integer.parseInt(experienceField.getText()));
            horse.setOwner(ownerField.getText());
            horse.setPrice(Integer.parseInt(priceField.getText()));
            String msg;
            if (horse.getId() == -1) {
                manager.add(horse);
                onItemAdded(horse.getId());
                msg = "Лошадь успешно добавлена";
            } else {
                manager.update(horse);
                msg = "Лошадь успешно обновлена";
            }
            DialogUtil.showInfo(this, msg);
            // TODO: сделать отображение успешного сохранения.
            //  Может быть подумать об иконке изменен/сохранен.
        } catch (SQLException exp) {
            exp.printStackTrace();
            showError(this);
        } catch (NumberFormatException exp) {
            exp.printStackTrace();
            JOptionPane.showMessageDialog(this, "Введены неверные значения");
        }
    }
}
