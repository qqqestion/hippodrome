package ru.emoji.tashkent.utils;

import java.util.Arrays;
import java.util.Collection;

public class Utils {
    public static <E> String fromCollectionToString(Collection<E> collection) {
        return fromCollectionToString(collection, ", ");
    }

    public static <E> String fromCollectionToString(Collection<E> collection, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (E obj : collection) {
            sb.append(obj.toString()).append(delimiter);
        }
        return sb.toString();
    }
}
