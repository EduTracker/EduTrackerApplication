package com.project.itmo2016.edutrackerapplication.loader;

import android.util.Log;

import com.project.itmo2016.edutrackerapplication.ChooseGroupActivity;
import com.project.itmo2016.edutrackerapplication.models.Auditory;
import com.project.itmo2016.edutrackerapplication.models.Day;
import com.project.itmo2016.edutrackerapplication.models.Faculty;
import com.project.itmo2016.edutrackerapplication.models.Group;
import com.project.itmo2016.edutrackerapplication.models.Lesson;
import com.project.itmo2016.edutrackerapplication.models.Schedule;
import com.project.itmo2016.edutrackerapplication.models.Time;
import com.project.itmo2016.edutrackerapplication.utils.IOUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Aleksandr Tukallo on 04.12.16.
 */
public class ParserJSON {

    public static Schedule parseResponse(InputStream in) {
        try {
            final JSONObject jsonObj = new JSONObject(IOUtils.readToString(in, "UTF-8"));
            return parseJson(jsonObj);
        } catch (Exception e) {
            Log.d(ChooseGroupActivity.TAG, "Error while reading json");
            return null;
        }
    }

    private static Schedule parseJson(JSONObject jsonObject) throws Exception {
        Schedule schedule = new Schedule(new ArrayList<Faculty>());

        JSONArray faculties = jsonObject.optJSONArray("faculties");
        for (int i = 0; i < faculties.length(); i++) {
            JSONObject faculty = faculties.optJSONObject(i);
            schedule.faculties.add(new Faculty(new ArrayList<Group>(), faculty.optString("faculty_name")));

            JSONArray groups = faculty.optJSONArray("groups");
            for (int j = 0; j < groups.length(); j++) {
                JSONObject group = groups.optJSONObject(j);
                schedule.faculties.get(i)
                        .groups.add(new Group(new ArrayList<Day>(), group.optString("group_name")));

                JSONArray days = group.optJSONArray("days");
                for (int k = 0; k < days.length(); k++) {
                    JSONObject day = days.optJSONObject(k);
                    schedule.faculties.get(i)
                            .groups.get(j)
                            .days.add(new Day(new ArrayList<Lesson>(), day.optInt("weekday")));

                    JSONArray lessons = day.optJSONArray("lessons");
                    for (int l = 0; l < lessons.length(); l++) {
                        JSONObject lesson = lessons.optJSONObject(l);
                        schedule.faculties.get(i)
                                .groups.get(j)
                                .days.get(k)
                                .lessons.add(new Lesson(lesson.optString("subject"),
                                new Time(lesson.optString("time_start")),
                                new Time(lesson.optString("time_end")),
                                lesson.optInt("parity"),
                                new ArrayList<Auditory>()));

                        JSONArray auditories = lesson.optJSONArray("auditories");
                        for (int m = 0; m < auditories.length(); m++) {
                            JSONObject auditory = auditories.optJSONObject(m);
                            schedule.faculties.get(i)
                                    .groups.get(j)
                                    .days.get(k)
                                    .lessons.get(l)
                                    .auditories.add(new Auditory(auditory.optString("auditory_name"),
                                    auditory.optString("auditory_address")));
                        }
                    }
                }
            }
        }
        return schedule;
    }
}
