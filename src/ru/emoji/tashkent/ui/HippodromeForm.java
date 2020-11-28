package ru.emoji.tashkent.ui;

import ru.emoji.tashkent.ActionEnum;
import ru.emoji.tashkent.utils.MainForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HippodromeForm extends MainForm implements ActionListener {
    private JPanel mainPanel;
    private JComboBox actionBox;

    public HippodromeForm() {
        super(ActionEnum.SHOW_HIPPODROMES);
        setContentPane(mainPanel);
        setVisible(true);

        initBoxes();
    }

    private void initBoxes() {
        initMenu(actionBox);
        actionBox.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ActionEnum newAction = (ActionEnum) actionBox.getSelectedItem();
        changeWindow(newAction);
    }
}
