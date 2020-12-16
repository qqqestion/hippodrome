package ru.emoji.tashkent.utils;

import ru.emoji.tashkent.enums.ActionEnum;
import ru.emoji.tashkent.ui.*;

import javax.swing.*;

public abstract class MainForm extends BaseForm {
    protected final ActionEnum action;

    public MainForm(ActionEnum action) {
        this.action = action;
    }

    protected void initMenu(JComboBox actionBox) {
        actionBox.addItem(ActionEnum.SHOW_PROFILE);
        actionBox.addItem(ActionEnum.SHOW_JOCKEYS);
        actionBox.addItem(ActionEnum.SHOW_HORSES);
        actionBox.addItem(ActionEnum.SHOW_COMPETITIONS);
//        if (Application.getInstance().getUser().isAdmin()) {
//            actionBox.addItem(ActionEnum.SHOW_ADMIN);
//        }

        actionBox.setSelectedItem(action);
    }

    @Override
    public int getFormWidth() {
        return 1200;
    }

    @Override
    public int getFormHeight() {
        return 700;
    }

    protected void changeWindow(ActionEnum newAction) {
        if (action.equals(newAction)) {
            return;
        }
        dispose();
        switch (newAction) {
            case SHOW_PROFILE:
                new ProfileForm();
                break;
            case SHOW_JOCKEYS:
                new UserForm();
                break;
            case SHOW_HORSES:
                new HorseForm();
                break;
            case SHOW_COMPETITIONS:
                new CompetitionForm();
                break;
//            case SHOW_ADMIN:
//                new AdminForm();
//                break;
        }
    }
}
