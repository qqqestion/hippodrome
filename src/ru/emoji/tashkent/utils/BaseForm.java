package ru.emoji.tashkent.utils;

import javax.swing.*;
import java.awt.*;

public abstract class BaseForm extends JFrame {
    private static String baseApplicationTitle;

    public BaseForm() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(getFormWidth(), getFormHeight()));
        setTitle(baseApplicationTitle == null ? "Application" : baseApplicationTitle);
        setLocation(
                Toolkit.getDefaultToolkit().getScreenSize().width / 2 - getFormWidth() / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - getFormHeight() / 2
        );
    }

    public abstract int getFormWidth();

    public abstract int getFormHeight();

    public static String getBaseApplicationTitle() {
        return baseApplicationTitle;
    }

    public static void setBaseApplicationTitle(String baseApplicationTitle) {
        BaseForm.baseApplicationTitle = baseApplicationTitle;
    }
}
