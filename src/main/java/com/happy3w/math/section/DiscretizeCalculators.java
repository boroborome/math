package com.happy3w.math.section;

import java.time.ZoneId;

public class DiscretizeCalculators {
    public static IntegerDiscretizeCalculator intCalculator = new IntegerDiscretizeCalculator();
    public static LongDiscretizeCalculator longCalculator = new LongDiscretizeCalculator();
    public static DateDiscretizeCalculator dateCalculator = new DateDiscretizeCalculator(ZoneId.systemDefault());
}
