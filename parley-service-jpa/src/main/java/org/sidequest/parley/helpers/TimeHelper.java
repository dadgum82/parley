package org.sidequest.parley.helpers;

import java.time.*;
import java.util.logging.Logger;

/**
 * A utility class that provides helper methods for managing time-related operations.
 */
public class TimeHelper {
    private static final Logger log = Logger.getLogger(TimeHelper.class.getName());
    /**
     * Converts a ZonedDateTime to an Instant representing the same point on the timeline in UTC.
     *
     * @param localDateTime The local date-time to convert.
     * @return The corresponding Instant in UTC.
     */
    public static Instant toUtc(ZonedDateTime localDateTime) {
        return localDateTime.toInstant();
    }

    /**
     * Converts an Instant in UTC to a ZonedDateTime in the specified timezone.
     *
     * @param utc    The UTC Instant to convert.
     * @param zoneId The timezone for the resulting ZonedDateTime.
     * @return The corresponding ZonedDateTime in the specified timezone.
     */
    public static ZonedDateTime fromUtc(Instant utc, ZoneId zoneId) {
        return utc.atZone(zoneId);
    }

    /**
     * Gets the current time as an Instant in UTC.
     *
     * @return The current time as an Instant in UTC.
     */
    public static Instant nowUtc() {
        return Instant.now();
    }

    /**
     * Gets the current time in the user's local timezone and converts it to an OffsetDateTime in UTC.
     *
     * @param userZoneId The timezone of the user.
     * @return The current time as an OffsetDateTime in UTC.
     */
    public static OffsetDateTime getOffsetDateTimeForUserInUtcTime(ZoneId userZoneId) {
        ZonedDateTime userLocalTime = ZonedDateTime.now(userZoneId);
        Instant utcTime = TimeHelper.toUtc(userLocalTime);

        return TimeHelper.toOffsetDateTimeUtc(utcTime);
    }

    /**
     * Converts a UTC Instant to an OffsetDateTime in the user's local timezone.
     *
     * @param utcInstant The UTC Instant to convert.
     * @param userZoneId The timezone of the user.
     * @return The corresponding OffsetDateTime in the user's local timezone.
     */
    public static OffsetDateTime getOffsetDateTimeForUserInUserLocalTime(Instant utcInstant, ZoneId userZoneId) {
        return utcInstant.atZone(userZoneId).toOffsetDateTime();
    }


    /**
     * Converts an Instant to an OffsetDateTime in UTC.
     *
     * @param instant The Instant to convert.
     * @return The corresponding OffsetDateTime in UTC.
     */
    public static OffsetDateTime toOffsetDateTimeUtc(Instant instant) {
        return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
    }

    /**
     * Converts an Instant to a LocalDateTime in the specified timezone.
     *
     * @param instant The Instant to convert.
     * @param zoneId  The timezone for the resulting LocalDateTime.
     * @return The corresponding LocalDateTime in the specified timezone.
     */
    public static LocalDateTime toLocalDateTime(Instant instant, ZoneId zoneId) {
        return LocalDateTime.ofInstant(instant, zoneId);
    }

    /**
     * Converts an Instant to a ZonedDateTime in the specified timezone.
     *
     * @param instant The Instant to convert.
     * @param zoneId  The timezone for the resulting ZonedDateTime.
     * @return The corresponding ZonedDateTime in the specified timezone.
     */
    public static ZonedDateTime toZonedDateTime(Instant instant, ZoneId zoneId) {
        return instant.atZone(zoneId);
    }

    /**
     * Gets the current time as a LocalDateTime in the specified timezone.
     *
     * @param zoneId The timezone for the resulting LocalDateTime.
     * @return The current time as a LocalDateTime in the specified timezone.
     */
    public static LocalDateTime nowLocal(ZoneId zoneId) {
        return LocalDateTime.now(zoneId);
    }

    /**
     * Gets the current time as a ZonedDateTime in the specified timezone.
     *
     * @param zoneId The timezone for the resulting ZonedDateTime.
     * @return The current time as a ZonedDateTime in the specified timezone.
     */
    public static ZonedDateTime nowZoned(ZoneId zoneId) {
        return ZonedDateTime.now(zoneId);
    }

}