package com.example.project.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.activities.DetailActivity;
import com.example.project.activities.MainActivity;
import com.example.project.database.AppDatabase;
import com.example.project.database.Event;
import com.example.project.utils.CalendarUtils;

import java.time.LocalDate;
import java.util.List;

/**
 * Provide implementation of RecyclerView interface and bind ViewHolder.
 * This class stores Event
 *
 * @author Dat Pham
 * @version 0.0.1
 * @since 2022-02-28
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private final Context mContext;
    private final AppDatabase mDatabase;
    private final CalendarUtils mCalendarUtils;
    private final List<Event> mEvents;

    /**
     * Provide a reference to the type of used views
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cvEvent;
        private final ImageView ivColor;
        private final TextView txtTitle;
        private final TextView txtTimeframe;

        public ViewHolder(View view) {
            super(view);
            cvEvent = view.findViewById(R.id.cvEvent);
            ivColor = view.findViewById(R.id.ivColor);
            txtTitle = view.findViewById(R.id.txtTitle);
            txtTimeframe = view.findViewById(R.id.txtTimeframe);
        }
    }

    /**
     * Contructor for DateAdapter class
     *
     * @param context       Context
     * @param calendarUtils CalendarUtils
     * @param database      AppDatabase
     */
    public EventAdapter(Context context, AppDatabase database, CalendarUtils calendarUtils) {
        mContext = context;
        mDatabase = database;
        mCalendarUtils = calendarUtils;
        mEvents = mDatabase.eventDao().getAll(mCalendarUtils.getCurrentDateTime());
    }

    /**
     * Create new views
     *
     * @param viewGroup ViewGroup
     * @param viewType  int
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.event_list_item, viewGroup, false);

        return new ViewHolder(view);
    }

    /**
     * Replace the contents of a view
     *
     * @param viewHolder ViewHolder
     * @param position   int
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Event event = mEvents.get(position);

        viewHolder.ivColor.setColorFilter(Color.parseColor(event.getColor()));

        viewHolder.txtTitle.setText(mEvents.get(position).getTitle());

        viewHolder.txtTimeframe.setText(getTimeframe(event));

        viewHolder.cvEvent.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra(MainActivity.EVENT_ID, event.getEventId());
            mContext.startActivity(intent);
        });
    }

    /**
     * Convert a String value to sentence textStyle
     *
     * @param value String
     * @return String textInSentenceStyle
     */
    private String convertToSentence(String value) {
        return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
    }

    /**
     * Get a customized text view from Event
     *
     * @param event Event
     * @return String customized text
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getTimeframe(Event event) {
        String timeframe;
        LocalDate beginDateTime = LocalDate.parse(event.getBeginDate());

        String beginMonth = String.valueOf(beginDateTime.getMonth());

        timeframe = convertToSentence(beginMonth) + " " + beginDateTime.getDayOfMonth();
        if (beginDateTime.getYear() != mCalendarUtils.getTodayYear()) {
            timeframe += " " + beginDateTime.getYear();
        }

        timeframe += ", " + event.getBeginTime() + " - ";

        if (event.getBeginDate().equals(event.getEndDate())) {
            timeframe += event.getEndTime();
        }
        return timeframe;
    }

    /**
     * Return the size of dataset
     *
     * @return int size of mDateEntities
     */
    @Override
    public int getItemCount() {
        if (mEvents != null) {
            return mEvents.size();
        }
        return 0;
    }
}
