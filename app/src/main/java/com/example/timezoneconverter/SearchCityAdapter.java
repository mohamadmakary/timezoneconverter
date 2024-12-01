package com.example.timezoneconverter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchCityAdapter extends RecyclerView.Adapter<SearchCityAdapter.CityViewHolder> {

    private List<String> cityList;
    private Activity activity;

    public SearchCityAdapter(Activity activity, List<String> cityList) {
        this.activity = activity;
        this.cityList = cityList;
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_city, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        String city = cityList.get(position);
        holder.cityTextView.setText(city);
        holder.itemView.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selected_city", city);
            activity.setResult(Activity.RESULT_OK, resultIntent);
            activity.finish();
        });
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }
    public static class CityViewHolder extends RecyclerView.ViewHolder {
        TextView cityTextView;

        public CityViewHolder(View itemView) {
            super(itemView);
            cityTextView = itemView.findViewById(R.id.cityName);
        }
    }
    public void updateCities(List<String> newCityList) {
        cityList.clear();
        cityList.addAll(newCityList);
        notifyDataSetChanged();
    }
}
