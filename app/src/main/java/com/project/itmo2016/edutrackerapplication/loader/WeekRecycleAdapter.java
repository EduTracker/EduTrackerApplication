package com.project.itmo2016.edutrackerapplication.loader;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.itmo2016.edutrackerapplication.R;
import com.project.itmo2016.edutrackerapplication.models.schedule.Day;
import com.project.itmo2016.edutrackerapplication.models.schedule.Lesson;
import com.project.itmo2016.edutrackerapplication.models.schedule.LocalSchedule;

import java.util.ArrayList;


public class WeekRecycleAdapter extends RecyclerView.Adapter<WeekRecycleAdapter.WeekViewHolder> {
    private static final String[] WEEKDAY = new String[]{"Понедельник", "Вторник",
    "Среда",
    "Четверг",
    "Пятница",
    "Суббота",
    "Воскресенье"
    };
    private final Context context;
    private final LayoutInflater layoutInflater;

    private LocalSchedule week = null;
    private ArrayList<ArrayList<Boolean>> checkedPerDays;

    public WeekRecycleAdapter(Context context, ArrayList<ArrayList<Boolean>> checkedPerDays) {
        this.context = context;
        this.checkedPerDays = checkedPerDays;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setSchedule(LocalSchedule week) {
        this.week = week;
        Log.d("Adapter set schedule", week.facultyName + " " + week.groupName + " " + week.days.size());
        notifyDataSetChanged();
    }

    @Override
    public WeekViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("View Create", "Launched");
        return WeekViewHolder.newInstance(layoutInflater, parent, context, checkedPerDays);
    }

    @Override
    public void onBindViewHolder(WeekViewHolder holder, int position) {
        Day day = week.days.get(position);
        holder.dayName.setText(WEEKDAY[day.dayOfTheWeek - 1]);
        holder.day.setDaySchedule(day, day.dayOfTheWeek - 1);

    }

    @Override
    public int getItemCount() {
        return week == null ? 0 : week.days.size();
    }

    static class WeekViewHolder extends RecyclerView.ViewHolder {
        final TextView dayName;
        final DayRecyclerAdapter day;
        final private RecyclerView daysRecView;

        WeekViewHolder(View itemView, Context context, ArrayList<ArrayList<Boolean>> checked) {
            super(itemView);
            this.dayName = (TextView) itemView.findViewById(R.id.day_name);
            this.daysRecView = (RecyclerView) itemView.findViewById(R.id.single_day);
            day = new DayRecyclerAdapter(context, checked);
            daysRecView.setAdapter(day);
            daysRecView.setLayoutManager(new LinearLayoutManager(context));
        }

        static WeekViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent, Context context, ArrayList<ArrayList<Boolean>> checked) {
            final View view = layoutInflater.inflate(R.layout.schedule_item, parent, false);
            return new WeekViewHolder(view, context, checked);
        }
    }

}
