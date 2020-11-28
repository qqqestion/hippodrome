package ru.emoji.tashkent.ui;

import ru.emoji.tashkent.ActionEnum;
import ru.emoji.tashkent.Application;
import ru.emoji.tashkent.database.entity.Horse;
import ru.emoji.tashkent.database.manager.HorseManager;
import ru.emoji.tashkent.utils.MainForm;
import ru.emoji.tashkent.utils.ModelViewForm;

import javax.swing.*;
import java.util.List;

public class HorseForm extends ModelViewForm<Horse> {
    private JPanel mainPanel;

    public HorseForm() {
        super(ActionEnum.SHOW_HORSES,
                new HorseManager(Application.getInstance().getDatabase()));
        setContentPane(mainPanel);
        setVisible(true);
    }

    @Override
    protected List<String> getValuesFromModel(Horse element) {
        return null;
    }
}
