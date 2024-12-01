package com.example.timezoneconverter;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.threetenabp.AndroidThreeTen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

public class SearchCityActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchCityAdapter cityAdapter;
    private List<String> cityList;
    private List<String> filteredCityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_city);
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnClickListener(v -> {
            searchView.requestFocus();
            searchView.setIconified(false);
        });
        AndroidThreeTen.init(this);


        recyclerView = findViewById(R.id.searchRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cityList = new ArrayList<>();
        String[] zoneIdsArray = TimeZone.getAvailableIDs();
        Set<String> zoneIds = new HashSet<>(Arrays.asList(zoneIdsArray));

        cityList.addAll(zoneIds);

        filteredCityList = new ArrayList<>(cityList);
        cityAdapter = new SearchCityAdapter(this, filteredCityList);
        recyclerView.setAdapter(cityAdapter);

        searchView.setQueryHint("Search City...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterCities(newText);
                return true;
            }
        });
    }

    private void filterCities(String query) {
        List<String> filteredList = new ArrayList<>();

        if (query.isEmpty()) {
            filteredList.addAll(cityList);
        } else {
            for (String city : cityList) {
                if (city.toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(city);
                }
            }
        }
        cityAdapter.updateCities(filteredList);
    }
}
