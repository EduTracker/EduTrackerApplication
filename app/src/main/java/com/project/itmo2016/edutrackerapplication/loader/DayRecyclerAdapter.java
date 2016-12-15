package com.project.itmo2016.edutrackerapplication.loader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.itmo2016.edutrackerapplication.R;
import com.project.itmo2016.edutrackerapplication.models.schedule.Day;
import com.project.itmo2016.edutrackerapplication.models.schedule.Lesson;


public class DayRecyclerAdapter extends RecyclerView.Adapter<DayRecyclerAdapter.DayViewHolder> {



    private final Context context;
    private final LayoutInflater layoutInflater;
    private String[] parity = new String[]{"Еженедельно", "Четная", "Нечетная"};

    private Day daySchedule = null;

    public DayRecyclerAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setDaySchedule(Day day) {
        Log.d("Day Adapter", "Create");
        daySchedule = day;
        for (Lesson lesson : daySchedule.lessons) {
            Log.d("\tlessons", lesson.subject);
        }
        notifyDataSetChanged();
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("\tView Holder", "Create");
        return DayViewHolder.newInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(DayViewHolder holder, int position) {
        Log.d("\tView Bind", String.valueOf(position));
        Lesson lessons = daySchedule.lessons.get(position);
        holder.parity.setText(parity[lessons.parity]);
        holder.subject.setText(String.valueOf(lessons.subject));
        holder.time_from.setText(String.valueOf(lessons.startTime));
        holder.time_to.setText(String.valueOf(lessons.endTime));
    }

    @Override
    public int getItemCount() {
        return daySchedule == null ? 0 : daySchedule.lessons.size();
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        final TextView time_from;
        final TextView time_to;
        final TextView subject;
        final TextView parity;

        DayViewHolder(View itemView) {
            super(itemView);
            this.subject = (TextView) itemView.findViewById(R.id.subject);
            this.time_from = (TextView) itemView.findViewById(R.id.time_from);
            this.time_to = (TextView) itemView.findViewById(R.id.time_to);
            this.parity = (TextView) itemView.findViewById(R.id.parity);
        }

        static DayViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.schedule_day_item, parent, false);
            return new DayViewHolder(view);
        }
    }
}
