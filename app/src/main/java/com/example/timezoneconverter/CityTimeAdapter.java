package com.example.timezoneconverter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CityTimeAdapter extends RecyclerView.Adapter<CityTimeAdapter.CityTimeViewHolder> {

    private Context context;
    private List<CityTime> cityTimeList;

    public CityTimeAdapter(Context context, List<CityTime> cityTimeList) {
        this.context = context;
        this.cityTimeList = cityTimeList;
    }

    @Override
    public CityTimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_city_time, parent, false);
        return new CityTimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CityTimeViewHolder holder, int position) {
        CityTime cityTime = cityTimeList.get(position);
        holder.cityNameTextView.setText(cityTime.getCityName());
        holder.timeTextView.setText(cityTime.getCurrentTime()); // Display time
        holder.timeDifferenceTextView.setText(cityTime.getHoursDifference());


        if ("Current Location".equals(cityTime.getCityName())) {
            holder.removeButton.setVisibility(View.GONE);
        } else {
            holder.removeButton.setVisibility(View.VISIBLE);
            holder.removeButton.setOnClickListener(v -> {
                cityTimeList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, cityTimeList.size());
            });
        }
    }

    @Override
    public int getItemCount() {
        return cityTimeList.size();
    }

    public static class CityTimeViewHolder extends RecyclerView.ViewHolder {
        TextView cityNameTextView;
        TextView timeTextView;
        TextView timeDifferenceTextView;
        ImageButton removeButton;

        public CityTimeViewHolder(View itemView) {
            super(itemView);
            cityNameTextView = itemView.findViewById(R.id.cityName);
            timeTextView = itemView.findViewById(R.id.time);
            timeDifferenceTextView = itemView.findViewById(R.id.timezone);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
}
