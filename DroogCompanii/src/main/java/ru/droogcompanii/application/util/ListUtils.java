package ru.droogcompanii.application.util;

import java.util.List;

import ru.droogcompanii.application.data.data_structure.PartnerPoint;

/**
 * Created by ls on 16.01.14.
 */
public class ListUtils {
    public static <T> void swap(List<T> list, int index1, int index2) {
        if (index1 == index2) {
            return;
        }
        T obj1 = list.get(index1);
        T obj2 = list.get(index2);
        list.set(index1, obj2);
        list.set(index2, obj1);
    }
}
