package com.marcelorodrigo.apidifference.util;

import com.marcelorodrigo.apidifference.model.Difference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StringProcessor {

    public static List<Difference> getDifferences(final String left, final String right) {
        List<Difference> diffList = new ArrayList<>();
        Integer diffStart = null;
        int diffLength = 0;

        for (int position = 0; position <= left.length(); position++) {
            if (position < left.length() && left.charAt(position) != right.charAt(position)) {
                diffStart = Objects.isNull(diffStart) ? position : diffStart;
                diffLength++;
            } else if (Objects.nonNull(diffStart)) {
                diffList.add(new Difference(diffStart, diffLength));
                diffStart = null;
                diffLength = 0;
            }
        }

        return diffList;
    }

}
