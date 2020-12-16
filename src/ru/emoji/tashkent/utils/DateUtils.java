package ru.emoji.tashkent.utils;

import java.time.format.DateTimeFormatter;
import java.util.logging.SimpleFormatter;

public class DateUtils {
    public static DateTimeFormatter MYSQL_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
}
