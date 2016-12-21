package com.project.itmo2016.edutrackerapplication.utils;

import com.project.itmo2016.edutrackerapplication.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Aleksandr Tukallo on 08.12.16.
 */

/**
 * Class with utils for working with Stats for barChart in StatsActivity
 */
public final class StatsUtils {

    private final static class Pair {
        final float yValue;
        final int correspondingColor;

        Pair(float yValue, int correspondingColor) {
            this.yValue = yValue;
            this.correspondingColor = correspondingColor;
        }
    }

    /**
     * Array for storing yValues and corresponding colors
     */
    private static final Pair[] colorsForValues = new Pair[]{
            new Pair(0f, R.color.attendance_grade_1),
            new Pair(0.166f, R.color.attendance_grade_2),
            new Pair(0.333f, R.color.attendance_grade_3),
            new Pair(0.493f, R.color.attendance_grade_4),
            new Pair(0.665f, R.color.attendance_grade_5),
            new Pair(0.831f, R.color.attendance_grade_6),
            new Pair(1.0f, R.color.attendance_grade_7)};

    /**
     * Method looks for, closest to yValue, Pair.yValue in array.
     * It is done in O(n) time, can be optimized with search tree.
     *
     * @return Pair with closest yValue to parameter is returned.
     */
    private static Pair getClosestPairToYValue(float yValue) {

        for (int i = 1; i < colorsForValues.length; i++) {
            if (colorsForValues[i - 1].yValue <= yValue && colorsForValues[i].yValue >= yValue) {
                if (Math.abs(colorsForValues[i - 1].yValue - yValue) < Math.abs(colorsForValues[i].yValue - yValue))
                    return colorsForValues[i - 1];
                else return colorsForValues[i];
            }
        }

        //if yValue is not in range (it must never happen), still closest is returned
        if (yValue < colorsForValues[0].yValue)
            return colorsForValues[0];
        else return colorsForValues[colorsForValues.length - 1];
    }

    /**
     * Method returns a color for bar with given height
     *
     * @param yValue is a height of a bar
     * @param shift  is a shift to show bars with zero attendance. It is needed, because methods in this
     *               class suppose, that yValue is in [0;1] segment
     * @return color is returned
     */
    public static int getColorForYValue(float yValue, float shift) {
        return getClosestPairToYValue(yValue - shift).correspondingColor;
    }

    public final static class GeneratedData<T> {
        public final boolean noData;
        public final T chartDataSet;

        public GeneratedData(T chartDataSet, boolean noData) {
            this.noData = noData;
            this.chartDataSet = chartDataSet;
        }
    }

    /**
     * Method makes date Saturday if it's Sunday else it isn't changed
     */
    public static GregorianCalendar ensureNotSunday(GregorianCalendar date) {
        if (date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
            date.add(Calendar.DATE, -1);
        return date;
    }

    /**
     * Method moves date to first day of the week (Monday)
     */
    public static void moveToMonday(GregorianCalendar date) {
        while (date.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY)
            date.add(Calendar.DAY_OF_WEEK, -1);
    }

    /**
     * Method coverts weekDay numeration from (Mon = 2, Sat = 7) as used in GregorianCalendar to Mon = 0, Sat = 5
     */
    public static int convertWeekDayNumeration(int weekDay) {
        if (weekDay == 1) return 6; //sunday
        return weekDay - 2;
    }

    /**
     * Copy of argument is returned as new GregorianCalendar object
     */
    public static GregorianCalendar copyCalendarConstructor(GregorianCalendar other) {
        return new GregorianCalendar(other.get(Calendar.YEAR), other.get(Calendar.MONTH), other.get(Calendar.DATE));
    }

}
