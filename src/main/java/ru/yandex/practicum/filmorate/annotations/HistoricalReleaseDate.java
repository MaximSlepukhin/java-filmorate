package ru.yandex.practicum.filmorate.annotations;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.time.LocalDate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface HistoricalReleaseDate {
    String value() default "1895-12-28";
}
