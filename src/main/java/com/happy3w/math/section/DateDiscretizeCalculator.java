package com.happy3w.math.section;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateDiscretizeCalculator implements DiscretizeCalculator<Date> {
    @Override
    public Date plus(Date value, long delta) {
        if (value == null) {
            return null;
        }
        Instant newInstant = LocalDate.from(value.toInstant())
                .plusDays(delta)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant();
        return Date.from(newInstant);
    }
}
