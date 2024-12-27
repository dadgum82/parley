package org.sidequest.parley.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.zone.ZoneRulesException;
import java.util.logging.Logger;

/**
 * A utility class that provides helper methods for managing time-related operations.
 */
@Component
public class TimeHelper {
    private static final Logger log = Logger.getLogger(TimeHelper.class.getName());

    @Value("${default.timezone}")
    private String defaultTimezone;

    public ZoneId getZoneId(String timezone) {
        try {
            return timezone != null ? ZoneId.of(timezone) : null;
        } catch (Exception e) {
            log.warning("Invalid timezone: " + timezone + ". Using default: " + defaultTimezone);
            return null;
        }
    }

    public ZoneId getDefaultZoneId() {
        return ZoneId.of(defaultTimezone);
    }

    public boolean isTimezone(String timezone) {
        try {
            ZoneId.of(timezone);
            return true;
        } catch (ZoneRulesException e) {
            return false;
        }
    }


    /**
     * Converts a ZonedDateTime to UTC.
     *
     * @param localDateTime the OffsetDateTime to be converted to UTC
     * @return the OffsetDateTime in UTC
     */
    public static OffsetDateTime toUtc(OffsetDateTime localDateTime) {
        try {
            if (localDateTime == null) {
                return null;
            }
            return localDateTime.withOffsetSameInstant(ZoneOffset.UTC);
        } catch (Exception e) {
            log.severe("Error converting to UTC: " + e);
            return null;
        }
    }

    /**
     * Converts an OffsetDateTime to the local time of the specified ZoneId.
     *
     * @param odt     the OffsetDateTime to be converted
     * @param zoneId the ZoneId to convert to
     * @return the OffsetDateTime in the local time of the specified ZoneId
     */
    public static OffsetDateTime fromUtc(OffsetDateTime odt, ZoneId zoneId) {
        try {
            if (odt == null || zoneId == null) {
                return null;
            }
            return odt.atZoneSameInstant(zoneId).toOffsetDateTime();
        } catch (Exception e) {
            log.severe("Error converting from UTC: " + e);
            return null;
        }
    }

}