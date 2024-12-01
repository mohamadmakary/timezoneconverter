package com.example.timezoneconverter;

import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.ZoneId;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Handler;

import com.jakewharton.threetenabp.AndroidThreeTen;

import java.util.ArrayList;
import java.util.List;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CityTimeAdapter cityTimeAdapter;
    private List<CityTime> cityTimeList;
    private Button addButton;

    private static final int REQUEST_CODE_SEARCH_CITY = 1;
    private String previousTime = "";
    private Handler handler;
    private Runnable timeCheckRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(this);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        cityTimeList = new ArrayList<>();
        String currentTime = getCurrentTime();
        previousTime = currentTime;
        cityTimeList.add(new CityTime("Current Location", currentTime, ""));
        cityTimeList.add(getCityTime("America/New_York"));
        cityTimeList.add(getCityTime("Europe/London"));
        cityTimeList.add(getCityTime("Asia/Tokyo"));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cityTimeAdapter = new CityTimeAdapter(this, cityTimeList);
        recyclerView.setAdapter(cityTimeAdapter);
        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchCityActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SEARCH_CITY);
        });
        handler = new Handler();
        timeCheckRunnable = new Runnable() {
            @Override
            public void run() {
                String currentTime = getCurrentTime();
                if (!currentTime.equals(previousTime)) {
                    updateCityTimes(currentTime);
                    previousTime = currentTime;
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(timeCheckRunnable);
    }
    private String getCurrentTime() {
        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        return now.format(formatter);
    }
    private CityTime getCityTime(String zoneId) {
        ZonedDateTime localTime = ZonedDateTime.now();
        ZonedDateTime cityTime = ZonedDateTime.now(ZoneId.of(zoneId));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        String formattedCityTime = cityTime.format(formatter);

        long hoursDifference = cityTime.getOffset().getTotalSeconds() / 3600 - localTime.getOffset().getTotalSeconds() / 3600;

        String timeDifferenceString;
        if (hoursDifference == 0) {
            timeDifferenceString = "Same time as local time";
        } else if (hoursDifference > 0) {
            timeDifferenceString = hoursDifference + " hours ahead";
        } else {
            timeDifferenceString = Math.abs(hoursDifference) + " hours behind";
        }

        return new CityTime(zoneId, formattedCityTime, timeDifferenceString);
    }


    private void updateCityTimes(String currentTime) {
        cityTimeList.set(0, new CityTime("Current Location", currentTime, ""));
        cityTimeList.set(1, getCityTime("America/New_York"));
        cityTimeList.set(2, getCityTime("Europe/London"));
        cityTimeList.set(3, getCityTime("Asia/Tokyo"));
        cityTimeAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SEARCH_CITY && resultCode == RESULT_OK) {
            String selectedCity = data.getStringExtra("selected_city");

            if (selectedCity != null && !selectedCity.isEmpty()) {
                CityTime cityTime = getCityTime(selectedCity);
                cityTimeList.add(cityTime);
                cityTimeAdapter.notifyItemInserted(cityTimeList.size() - 1);
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (cityTimeList.isEmpty()) {
            String currentTime = getCurrentTime();
            previousTime = currentTime;

            cityTimeList.add(new CityTime("Current Location", currentTime, ""));
            cityTimeList.add(getCityTime("America/New_York"));
            cityTimeList.add(getCityTime("Europe/London"));
            cityTimeList.add(getCityTime("Asia/Tokyo"));

            cityTimeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(timeCheckRunnable);
    }
}
