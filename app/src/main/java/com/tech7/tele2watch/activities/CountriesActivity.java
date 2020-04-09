package com.tech7.tele2watch.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.tech7.tele2watch.R;
import com.tech7.tele2watch.adapter.CountriesAdapter;
import com.tech7.tele2watch.object.Country;
import com.tech7.tele2watch.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CountriesActivity extends AppCompatActivity implements CountriesAdapter.RecyclerViewClickListenerCountry{
    RecyclerView rcvCountries;
    CountriesAdapter adapter;
    List<Country> countries;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private String titleBar = "Choix du Pays";

    private static String JSON_URL = "http://test.panzidrc.com/api/countries";   // json url from API to fetch all countries


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries);

        initToolbar();

        //init views
        rcvCountries = findViewById(R.id.rcvCountries);
        countries = new ArrayList<>();

        //fetch data channels from json url
        extractCountries();
    }

    private void initToolbar() {
        //setting title bar
        titleBar = "Choix du Pays";
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(titleBar);
        Tools.setSystemBarColor(this, R.color.colorPrimary);
    }

    private void extractCountries() {
        // Display the progress bar.
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        //json
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response.toString() != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject channelObject = response.getJSONObject(i);

                            Country country = new Country();
                            country.setCountryCode(channelObject.getString("Iso").toString());
                            country.setCountry(channelObject.getString("Name").toString());
                            countries.add(country);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    setAdapter();
                }
                else{
                    Toast.makeText(CountriesActivity.this, "Echec de chargement. 1ere tentative de ressai", Toast.LENGTH_LONG);
                    extractCountries();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tagNews", "onErrorResponse: " + error.getMessage());

                //Hide progressbar
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            }
        });

        queue.add(jsonArrayRequest);
    }

    private void setAdapter() {

        //adapter
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new CountriesAdapter(getApplication(), countries, this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        rcvCountries.addItemDecoration(dividerItemDecoration);
        rcvCountries.setLayoutManager(layoutManager);
        rcvCountries.setAdapter(adapter);

        //Hide progressbar
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
    }

    @Override
    public void onCountryRowClicked(int position) {

    }

    @Override
    public void onCountryViewClicked(View v, int position) {
        if (!countries.isEmpty()) {
            Log.d("CountriesActivity-Click", countries.get(position).getCountry());
            Intent intent = new Intent(CountriesActivity.this, ChannelsByCountryActivity.class);
            intent.putExtra(ChannelsByCountryActivity.EXTRA_COUNTRYCODE, countries.get(position).getCountryCode());
            intent.putExtra(ChannelsByCountryActivity.EXTRA_COUNTRYNAME, countries.get(position).getCountry());
            //start tv activity
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}
