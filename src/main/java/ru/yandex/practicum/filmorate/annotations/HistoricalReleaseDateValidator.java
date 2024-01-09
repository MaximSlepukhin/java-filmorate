package ru.yandex.practicum.filmorate.annotations;


import java.lang.reflect.Field;
import java.time.LocalDate;

public class HistoricalReleaseDateValidator {
    public static void validate(Object obj) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(HistoricalReleaseDate.class) && field.getType() == LocalDate.class) {
                field.setAccessible(true);
                LocalDate dateValue = (LocalDate) field.get(obj);

                HistoricalReleaseDate annotation = field.getAnnotation(HistoricalReleaseDate.class);
                LocalDate historicalDate = LocalDate.parse(annotation.value());

                if (dateValue.isBefore(historicalDate)) {
                    throw new IllegalArgumentException("Release date must not be before " + annotation.value());
                }
            }
        }
    }
}