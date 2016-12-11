package com.project.itmo2016.edutrackerapplication.loader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.itmo2016.edutrackerapplication.R;
import com.project.itmo2016.edutrackerapplication.models.schedule.Day;
import com.project.itmo2016.edutrackerapplication.models.schedule.LocalSchedule;


/**
 * Created by Lenovo on 11.12.2016.
 */

public class WeekRecycleAdapter extends RecyclerView.Adapter<WeekRecycleAdapter.WeekViewHolder> {
    private final Context context;
    private final LayoutInflater layoutInflater;

    private LocalSchedule week = null;


    public WeekRecycleAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setSchedule(LocalSchedule week) {
        this.week = week;
        notifyDataSetChanged();
    }

    @Override
    public WeekViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return WeekViewHolder.newInstance(layoutInflater, parent, context);
    }

    @Override
    public void onBindViewHolder(WeekViewHolder holder, int position) {
        Day day = week.days.get(position);
        holder.dayName.setText(day.dayOfTheWeek);
        holder.day.setDaySchedule(day);
    }

    @Override
    public int getItemCount() {
        return week == null ? 0 : week.days.size();
    }

    static class WeekViewHolder extends RecyclerView.ViewHolder {
        final TextView dayName;
        final DayRecyclerAdapter day;
        private final RecyclerView daysRecView;

        WeekViewHolder(View itemView, Context context) {
            super(itemView);
            this.dayName = (TextView) itemView.findViewById(R.id.day_name);
            this.daysRecView = (RecyclerView) itemView.findViewById(R.id.single_day);
            day = new DayRecyclerAdapter(context);
            daysRecView.setAdapter(day);
        }

        static WeekViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent, Context context) {
            final View view = layoutInflater.inflate(R.layout.schedule_item, parent, false);
            return new WeekViewHolder(view, context);
        }
    }

}
