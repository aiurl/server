package io.theurl.framework.utility;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateTimeUtility {
    public static long toUnixTimestamp(long epochMilli) {
        return epochMilli / 1000;
    }

    public static long toUnixTimestamp(long epochMilli, ZoneId zoneId) {
        return epochMilli / 1000;
    }

    public static long toUnixTimestamp(java.time.LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    public static long toUnixTimestamp(java.time.LocalDateTime dateTime, ZoneId zoneId) {
        return dateTime.atZone(zoneId).toEpochSecond();
    }

    public static long toUnixTimestamp(java.time.ZonedDateTime dateTime) {
        return dateTime.toEpochSecond();
    }

    public static long toUnixTimestamp(java.time.ZonedDateTime dateTime, ZoneId zoneId) {
        return dateTime.withZoneSameInstant(zoneId).toEpochSecond();
    }

    public static long toUnixTimestamp(java.time.Instant instant) {
        return instant.getEpochSecond();
    }

    public static long toUnixTimestamp(java.time.Instant instant, ZoneId zoneId) {
        return instant.atZone(zoneId).toEpochSecond();
    }

    public static long toUnixTimestamp(java.util.Date date) {
        return date.getTime() / 1000;
    }

    public static Date toDate(long unixTimestamp) {
        return new Date(unixTimestamp * 1000);
    }

    public static Date toDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(LocalDateTime dateTime, ZoneId zoneId) {
        return Date.from(dateTime.atZone(zoneId).toInstant());
    }

    public static Date toDate(java.time.ZonedDateTime dateTime) {
        return Date.from(dateTime.toInstant());
    }

    public static Date toDate(java.time.Instant instant) {
        return Date.from(instant);
    }

    public static LocalDateTime toLocalDateTime(long unixTimestamp) {
        return LocalDateTime.ofEpochSecond(unixTimestamp, 0, ZoneId.systemDefault().getRules().getOffset(LocalDateTime.now()));
    }

    public static LocalDateTime toLocalDateTime(long unixTimestamp, ZoneId zoneId) {
        return LocalDateTime.ofEpochSecond(unixTimestamp, 0, zoneId.getRules().getOffset(LocalDateTime.now()));
    }

    public static LocalDateTime toLocalDateTime(java.util.Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime toLocalDateTime(java.util.Date date, ZoneId zoneId) {
        return date.toInstant().atZone(zoneId).toLocalDateTime();
    }
}
