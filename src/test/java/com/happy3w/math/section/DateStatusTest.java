package com.happy3w.math.section;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Date;

public class DateStatusTest {
    @Test
    void addDate() {
        DateStatus status = new DateStatus();
        Date d = Timestamp.valueOf("2020-02-02 00:00:00");
        status.addDate(d);

        String json = status.encodeTo();
        Assertions.assertEquals("[{\"from\":{\"include\":true,\"value\":1580572800000},\"to\":{\"include\":true,\"value\":1580572800000}}]", status.encodeTo());
        status.removeDate(d);
        status.decodeFrom(json);
        Assertions.assertEquals(json, status.encodeTo());
    }
}
