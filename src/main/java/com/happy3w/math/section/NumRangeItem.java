package com.happy3w.math.section;

import com.happy3w.java.ext.StringUtils;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class NumRangeItem {
    private Long start;
    private Long end;

    public NumRangeItem(int start, int end) {
        this.start = Long.valueOf(start);
        this.end = Long.valueOf(end);
    }

    public static long gap(Long a, Long b, boolean nullIsMin) {
        if (a == b) {
            return 0;
        }

        if (a == null) {
            return nullIsMin ? -10 : 10;
        }

        if (b == null) {
            return nullIsMin ? 10 : -10;
        }

        return a - b;
    }

    public void output(StringBuilder buf) {
        if (start == null) {
            buf.append("(*");
        } else {
            buf.append('[')
                    .append(start);
        }
        buf.append(',');
        if (end == null) {
            buf.append("*)");
        } else {
            buf.append(end)
                    .append(']');
        }
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        output(buf);
        return buf.toString();
    }

    public static NumRangeItem parse(String text) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }

        String pureText = text.replaceAll(" ", "");
        String[] nums = pureText.split(",");

        String startText, endText;
        if (nums.length < 2) {
            startText =pureText.substring(0, pureText.length() - 1);
            endText = pureText.substring(1);
        } else {
            startText = nums[0];
            endText = nums[1];
        }
        NumRangeItem rangeItem = new NumRangeItem(
                parseStart(startText),
                parseEnd(endText));
        return rangeItem.isValid() ? rangeItem : null;
    }

    private static Long parseEnd(String endText) {
        int bracketIndex = endText.length() - 1;
        String numText = endText.substring(0, bracketIndex);
        if (numText.isEmpty() || "*".equals(numText)) {
            return null;
        }

        long num = Long.parseLong(numText);
        char bracket = endText.charAt(bracketIndex);
        if (bracket == ')') {
            num--;
        }
        return Long.valueOf(num);
    }

    private static Long parseStart(String startText) {
        String numText = startText.substring(1);
        if (numText.isEmpty() || "*".equals(numText)) {
            return null;
        }

        long num = Long.parseLong(numText);
        char bracket = startText.charAt(0);
        if (bracket == '(') {
            num++;
        }
        return Long.valueOf(num);
    }

    private static Long parseNum(String text) {
        return "*".equals(text) || text.isEmpty() ? null : Long.parseLong(text);
    }

    public boolean isValid() {
        return start == null
                || end == null
                || start <= end;
    }
}
