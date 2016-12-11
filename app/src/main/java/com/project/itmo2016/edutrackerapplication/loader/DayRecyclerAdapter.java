package com.project.itmo2016.edutrackerapplication.loader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.itmo2016.edutrackerapplication.R;
import com.project.itmo2016.edutrackerapplication.models.Day;
import com.project.itmo2016.edutrackerapplication.models.Lesson;


public class DayRecyclerAdapter extends RecyclerView.Adapter<DayRecyclerAdapter.DayViewHolder> {

    private final Context context;
    private final LayoutInflater layoutInflater;

    private Day daySchedule = null;

    public DayRecyclerAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setDaySchedule(Day day) {
        daySchedule = day;
        notifyDataSetChanged();
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return DayViewHolder.newInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(DayViewHolder holder, int position) {
        Lesson lessons = daySchedule.lessons.get(position);
        holder.parity.setText(lessons.parity);
        holder.subject.setText(lessons.subject);
        holder.time.setText("from " + lessons.startTime + " to " + lessons.endTime);
    }

    @Override
    public int getItemCount() {
        return daySchedule == null ? 0 : daySchedule.lessons.size();
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        public final TextView time;
        final TextView subject;
        final TextView parity;

        DayViewHolder(View itemView) {
            super(itemView);
            this.subject = (TextView) itemView.findViewById(R.id.subject);
            this.time = (TextView) itemView.findViewById(R.id.time);
            this.parity = (TextView) itemView.findViewById(R.id.parity);
        }

        static DayViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.schedule_day_item, parent, false);
            return new DayViewHolder(view);
        }
    }
}
