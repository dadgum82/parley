package org.sidequest.parley.helpers;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.logging.Logger;

/**
 * A utility class that provides helper methods for managing time-related operations.
 */
public class TimeHelper {
    private static final Logger log = Logger.getLogger(TimeHelper.class.getName());

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