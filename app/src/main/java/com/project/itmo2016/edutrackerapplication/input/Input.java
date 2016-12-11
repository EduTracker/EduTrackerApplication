package com.project.itmo2016.edutrackerapplication.input;

import com.project.itmo2016.edutrackerapplication.models.Schedule.GlobalSchedule;
import com.project.itmo2016.edutrackerapplication.models.Schedule.LocalSchedule;

/**
 * Created by Aleksandr Tukallo on 04.12.16.
 */
public class Input {

    /**
     * Checks if there is a schedule for such groupName (english letters only) in globalSchedule
     * If there isn't, returns null
     * else
     * schedule object is significantly reduced -- only schedule for this group is left
     * and LocalSchedule is returned
     */
    public static LocalSchedule processGroupName(GlobalSchedule schedule, String groupName) {
        for (int i = 0; i < schedule.faculties.size(); i++) {
            for (int j = 0; j < schedule.faculties.get(i).groups.size(); j++) {
                if (schedule.faculties.get(i).groups.get(j).groupName.equals(groupName)) {
                    return reduceSchedule(i, j, schedule);
                }
            }
        }
        return null;
    }

    private static LocalSchedule reduceSchedule(int i, int j, GlobalSchedule globalSchedule) {
        return new LocalSchedule(globalSchedule.faculties.get(i).facultyName,
                globalSchedule.faculties.get(i).groups.get(j).groupName,
                globalSchedule.faculties.get(i).groups.get(j).days);
    }
}
