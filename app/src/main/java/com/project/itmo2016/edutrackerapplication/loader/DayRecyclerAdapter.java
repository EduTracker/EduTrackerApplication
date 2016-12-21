package com.project.itmo2016.edutrackerapplication.loader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.project.itmo2016.edutrackerapplication.R;
import com.project.itmo2016.edutrackerapplication.cache.CheckboxCache;
import com.project.itmo2016.edutrackerapplication.models.schedule.Day;
import com.project.itmo2016.edutrackerapplication.models.schedule.Lesson;


class DayRecyclerAdapter extends RecyclerView.Adapter<DayRecyclerAdapter.DayViewHolder> {

    private final Context context;
    private final LayoutInflater layoutInflater;
    private String[] parity = new String[]{"Еженедельно", "Четная", "Нечетная"};

    private Day daySchedule = null;
    private int weekOffset;


    DayRecyclerAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    void setDaySchedule(Day day, int weekOffset) {
        daySchedule = day;
        this.weekOffset = weekOffset;
        notifyDataSetChanged();
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return DayViewHolder.newInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(DayViewHolder holder, int position) {
        Lesson lessons = daySchedule.lessons.get(position);
        holder.parity.setText(parity[lessons.parity]);
        holder.subject.setText(String.valueOf(lessons.subject));
        holder.time_from.setText(String.valueOf(lessons.startTime));
        holder.time_to.setText(String.valueOf(lessons.endTime));
//        holder.checkbox.setChecked(false);

        holder.checkbox.setChecked(new CheckboxCache(context).get(weekOffset, position) == 1);
        holder.checkbox.setOnClickListener(new Listener(context, position, holder));
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
        final CheckBox checkbox;

        DayViewHolder(View itemView) {
            super(itemView);
            this.subject = (TextView) itemView.findViewById(R.id.subject);
            this.time_from = (TextView) itemView.findViewById(R.id.time_from);
            this.time_to = (TextView) itemView.findViewById(R.id.time_to);
            this.parity = (TextView) itemView.findViewById(R.id.parity);
            this.checkbox = (CheckBox) itemView.findViewById(R.id.pair_stats_checkbox);
        }

        static DayViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.schedule_day_item, parent, false);
            return new DayViewHolder(view);
        }
    }

    private class Listener implements View.OnClickListener {
        final Context context;
        int pos;
        DayViewHolder holder;

        Listener(Context context, int pos, DayViewHolder holder) {
            this.context = context;
            this.pos = pos;
            this.holder = holder;
        }

        @Override
        public void onClick(View view) {
            if (view == holder.checkbox) {
                Log.d("Checkbox click", "Day " + weekOffset + " pos " + pos + "=" + holder.checkbox.isChecked());
                new CheckboxCache(context).put(weekOffset, pos, holder.checkbox.isChecked() ? 1 : 0);
            }
        }
    }
}
