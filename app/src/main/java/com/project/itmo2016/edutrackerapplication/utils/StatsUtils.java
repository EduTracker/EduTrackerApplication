package com.project.itmo2016.edutrackerapplication.utils;

import com.project.itmo2016.edutrackerapplication.R;

/**
 * Created by Aleksandr Tukallo on 08.12.16.
 */

public class StatsUtils {

    private static class Pair {
        float yValue;
        int correspondingColor;

        Pair(float yValue, int correspondingColor) {
            this.yValue = yValue;
            this.correspondingColor = correspondingColor;
        }
    }

    private static final Pair[] colorsForValues = new Pair[]{
            new Pair(0f, R.color.attendance_grade_1),
            new Pair(0.166f, R.color.attendance_grade_2),
            new Pair(0.333f, R.color.attendance_grade_3),
            new Pair(0.493f, R.color.attendance_grade_4),
            new Pair(0.665f, R.color.attendance_grade_5),
            new Pair(0.831f, R.color.attendance_grade_6),
            new Pair(1.0f, R.color.attendance_grade_7)};

    //method goes through all the array with colors and yValues in O(n) time and chooses the closest yValue
    private static Pair getClosestPairToYValue(float yValue) {
        for (int i = 1; i < colorsForValues.length; i++) {
            if (colorsForValues[i - 1].yValue <= yValue && colorsForValues[i].yValue >= yValue) {
                if (Math.abs(colorsForValues[i - 1].yValue - yValue) < Math.abs(colorsForValues[i].yValue - yValue))
                    return colorsForValues[i - 1];
                else return colorsForValues[i];
            }
        }
        return new Pair(yValue, R.color.black); //it must never be returned. black just not to cause NullPtrException
    }

    //returns color for yValue
    public static int getColorForYValue(float yValue, float shift) {
        return getClosestPairToYValue(yValue - shift).correspondingColor;
    }
}
