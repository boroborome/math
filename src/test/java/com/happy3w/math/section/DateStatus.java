package com.happy3w.math.section;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateStatus {
    private DiscreteSection<Date> dateSection = new DiscreteSection<>(DiscretizeCalculators.dateCalculator);

    public void addDate(Date d) {
        dateSection.unionValue(d);
    }

    public void addRange(Date start, Date end) {
        dateSection.unionItem(SectionItem.ofValue(start, true, end, false));
    }

    public void removeDate(Date d) {
        dateSection.subtractValue(d);
    }

    public void removeRange(Date start, Date end) {
        dateSection.subtractItem(SectionItem.ofValue(start, true, end, false));
    }

    public String encodeTo() {
        return JSON.toJSONString(dateSection.getItems());
    }

    public void decodeFrom(String encodeStr) {
        List<Object> items = JSON.parseArray(encodeStr, new Type[]{SectionItem.class, Date.class});
        List<SectionItem<Date>> newItems = new ArrayList<>();
        for (Object obj : items) {
            if (obj != null) {
                newItems.add((SectionItem<Date>) obj);
            }
        }
        dateSection.setItems(newItems);
    }
}
