package ru.droogcompanii.application.util;

import java.util.ArrayList;
import java.util.List;

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

    public static <T> void moveElementAtFirstPosition(T element, List<T> list) {
        int index = list.indexOf(element);
        if (index == -1) {
            throw new IllegalArgumentException("Cannot find element <" + element + "> in list");
        }
        swap(list, 0, index);
    }


    public static <T> List<T> combineLists(List<T>... listsToCombine) {
        List<T> combined = new ArrayList<T>(totalSizeOf(listsToCombine));
        for (List<T> each : listsToCombine) {
            combined.addAll(each);
        }
        return combined;
    }

    public static int totalSizeOf(List<?>... lists) {
        int totalSize = 0;
        for (List<?> each : lists) {
            totalSize += each.size();
        }
        return totalSize;
    }

    public static <T> List<T> copyOf(List<T> listToCopy) {
        return new ArrayList<T>(listToCopy);
    }
}
