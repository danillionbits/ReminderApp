package com.example.project.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.DateEntity;
import com.example.project.R;
import com.example.project.utils.CalendarUtils;

import com.example.project.activities.MainActivity;

import java.util.ArrayList;

/**
 * Provide implementation of RecyclerView interface and bind ViewHolder.
 * This class stores DateEntity
 *
 * @author Dat Pham
 * @version 0.0.1
 * @since 2022-02-28
 */
public class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder> {
    private final Context mContext;
    private final CalendarUtils mCalendarUtils;
    private final ArrayList<DateEntity> mDateEntities;

    /**
     * Provide a reference to the type of used views
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Button btnDate;

        public ViewHolder(View view) {
            super(view);
            btnDate = view.findViewById(R.id.btnDate);
        }
    }

    /**
     * Contructor for DateAdapter class
     *
     * @param context       Context
     * @param calendarUtils CalendarUtils
     * @param dateEntities  ArrayList<DateEntity> Initialize dataset of DateEntity
     */
    public DateAdapter(Context context, CalendarUtils calendarUtils, ArrayList<DateEntity> dateEntities) {
        mContext = context;
        mCalendarUtils = calendarUtils;
        mDateEntities = dateEntities;
    }

    /**
     * Create new views
     *
     * @param viewGroup ViewGroup
     * @param viewType  int
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.date_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    /**
     * Replace the contents of a view
     *
     * @param viewHolder ViewHolder
     * @param position   int
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        DateEntity entity = mDateEntities.get(position);

        /* Button text is grey if not in selected month. Otherwise, it is white */
        if (!entity.isInCurrentMonth) {
            viewHolder.btnDate.setTextColor(mContext.getResources().getColor(R.color.bg_grey));
        } else {
            viewHolder.btnDate.setTextColor(mContext.getResources().getColor(R.color.white));
        }

        /* Button is purple with white text if date is selected */
        if (entity.isSelected) {
            viewHolder.btnDate.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            viewHolder.btnDate.setTextColor(mContext.getResources().getColor(R.color.purple));
        }

        /* Button text is pink if selected date is today */
        if (entity.isToday) {
            viewHolder.btnDate.setTextColor(mContext.getResources().getColor(R.color.pink));
        }

        viewHolder.btnDate.setText(String.valueOf(entity.getDate()));


        viewHolder.btnDate.setOnClickListener(v -> {
            Log.d("testing", entity.getYear() + " " + entity.getMonth() + " " + entity.getDate());

            mCalendarUtils.populateDateList(entity.getYear(), entity.getMonth(), entity.getDate());
            if (mContext instanceof MainActivity) {
                ((MainActivity) mContext).updateUI();
            }
        });
    }

    /**
     * Return the size of dataset
     *
     * @return int size of mDateEntities
     */
    @Override
    public int getItemCount() {
        if (mDateEntities != null) {
            return mDateEntities.size();
        }
        return 0;
    }
}
