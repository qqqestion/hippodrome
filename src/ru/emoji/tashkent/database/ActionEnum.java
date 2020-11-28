package ru.emoji.tashkent.database;

public enum ActionEnum {
    SHOW_PROFILE("Профиль"),
    SHOW_JOCKEYS("Жокеи"),
    SHOW_HORSES("Лошади"),
    SHOW_COMPETITIONS("Соревнования"),
    SHOW_HIPPODROMES("Ипподромы");

    private final String locale;

    ActionEnum(String locale) {
        this.locale = locale;
    }

    @Override
    public String toString() {
        return this.locale;
    }
}
