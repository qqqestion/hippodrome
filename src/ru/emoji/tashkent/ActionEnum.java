package ru.emoji.tashkent;

import ru.emoji.tashkent.database.entity.User;
import ru.emoji.tashkent.ui.*;
import ru.emoji.tashkent.utils.MainForm;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public enum ActionEnum {
    SHOW_PROFILE("Профиль"),
    SHOW_JOCKEYS("Жокеи"),
    SHOW_HORSES("Лошади"),
    SHOW_COMPETITIONS("Соревнования"),
    SHOW_HIPPODROMES("Ипподромы"),
    SHOW_ADMIN("Панель админа");

    private final String locale;

    ActionEnum(String locale) {
        this.locale = locale;
    }

    @Override
    public String toString() {
        return this.locale;
    }
}
