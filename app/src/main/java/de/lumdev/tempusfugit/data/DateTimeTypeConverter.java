package de.lumdev.tempusfugit.data;

import org.threeten.bp.Duration;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

public class DateTimeTypeConverter {

    private static DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @TypeConverter
    public static OffsetDateTime toOffsetDateTime(String value){
        return formatter.parse(value, OffsetDateTime.FROM);
    }
    @TypeConverter
    public static String fromOffsetDateTime(OffsetDateTime value){
        return value.format(formatter);
    }

    @TypeConverter
    public static Duration toDuration(String value){
        return Duration.parse(value);
    }
    @TypeConverter
    public static String fromDuration(Duration value){
        return value.toString();
    }
}
