package ru.emoji.tashkent.utils;

import ru.emoji.tashkent.ActionEnum;
import ru.emoji.tashkent.database.entity.User;
import ru.emoji.tashkent.ui.CompetitionForm;

import javax.swing.*;

public abstract class MainForm extends BaseForm {
    protected final ActionEnum action;

    public MainForm(ActionEnum action) {
        this.action = action;
    }

    @Override
    public int getFormWidth() {
        return 500;
    }

    @Override
    public int getFormHeight() {
        return 300;
    }

//    protected void processAction(ActionEnum newAction) {
//        if (action.equals(newAction)) {
//            return;
//        }
//        switch (action) {
//            case SHOW_PROFILE:
//                return;
//            case SHOW_JOCKEYS:
//                break;
//            case SHOW_HORSES:
//                break;
//            case SHOW_COMPETITIONS:
//                dispose();
//                new CompetitionForm(authUser);
//            case SHOW_HIPPODROMES:
//                break;
//        }
//    }
}
