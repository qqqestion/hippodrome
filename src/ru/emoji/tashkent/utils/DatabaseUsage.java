package ru.emoji.tashkent.utils;

import javax.swing.*;
import java.awt.*;

public interface DatabaseUsage {
    default void showError(Component parent) {
        JOptionPane.showMessageDialog(parent,
                "Ошибка при подключении базы данных", "Ошибка!", JOptionPane.ERROR_MESSAGE);
    }
}
