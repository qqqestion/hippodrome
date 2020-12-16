package ru.emoji.tashkent.utils;

import javax.swing.*;
import java.awt.*;

public class DialogUtil {
    public static void showError(Component parent, String text) {
        JOptionPane.showMessageDialog(parent, text, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    public static void showError(String text) {
        showError(null, text);
    }


    public static void showWarn(Component parent, String text) {
        JOptionPane.showMessageDialog(parent, text, "Предупреждение", JOptionPane.WARNING_MESSAGE);
    }

    public static void showWarn(String text) {
        showWarn(null, text);
    }

    public static void showInfo(Component parent, String text) {
        JOptionPane.showMessageDialog(parent, text, "Информация", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showInfo(String text) {
        showInfo(null, text);
    }

    public static boolean showConfirm(Component parent, String text) {
        return JOptionPane.showOptionDialog(parent,
                text,
                "Подтверждение",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"Подтверждение", "Отмена"},
                "Подтверждение") == JOptionPane.YES_OPTION;
    }

    public static boolean showConfirm(String text) {
        return showConfirm(null, text);
    }
}
