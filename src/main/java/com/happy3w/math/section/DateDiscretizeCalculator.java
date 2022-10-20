package com.happy3w.math.section;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateDiscretizeCalculator implements DiscretizeCalculator<Date> {
    private final ZoneId zoneId;

    public DateDiscretizeCalculator(ZoneId zoneId) {
        this.zoneId = zoneId;
    }

    @Override
    public Date plus(Date value, long delta) {
        if (value == null) {
            return null;
        }

        Instant newInstant = LocalDateTime.ofInstant(value.toInstant(), zoneId)
                .plusDays(delta)
                .atZone(zoneId)
                .toInstant();
        return Date.from(newInstant);
    }
}
