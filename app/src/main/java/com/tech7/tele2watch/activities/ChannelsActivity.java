package com.tech7.tele2watch.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Filter;
import android.widget.Filterable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.tech7.tele2watch.R;
import com.tech7.tele2watch.adapter.DefaultChannelAdapter;
import com.tech7.tele2watch.object.Channel;
import com.tech7.tele2watch.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChannelsActivity extends AppCompatActivity implements DefaultChannelAdapter.RecyclerViewClickListener {

    RecyclerView rcvChannelsList;
    DefaultChannelAdapter adapter;
    List<Channel> channels;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private String titleBar = "Tous les Chaînes du Monde";

    private static String JSON_URL = "http://test.panzidrc.com/api/channels";   // json url from API to fetch all channels

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channels);

        initToolbar();

        //init views
        rcvChannelsList = findViewById(R.id.rcvChannelsList);
        channels = new ArrayList<>();

        //fetch data channels from json url
        extractChannels();
    }

    private void initToolbar() {
        //setting title bar
        titleBar = "Tous les Chaînes du Monde";
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(titleBar);
        Tools.setSystemBarColor(this, R.color.colorPrimary);
    }

    private void extractChannels() {
        // Display the progress bar.
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        //json
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject channelObject = response.getJSONObject(i);

                        Channel channel = new Channel();
                        channel.setGroup_title(channelObject.getString("Group_title").toString());
                        channel.setTvg_logo(channelObject.getString("Tvg_logo").toString());
                        channel.setChannelUrl(channelObject.getString("ChannelUrl").toString());
                        channels.add(channel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                setAdapter();
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
        adapter = new DefaultChannelAdapter(getApplication(), channels, this);
        rcvChannelsList.setLayoutManager(layoutManager);
        rcvChannelsList.setAdapter(adapter);

        //Hide progressbar
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
    }

    @Override
    public void onRowClicked(int position) {

    }

    @Override
    public void onViewClicked(View v, int position) {
        if (!channels.isEmpty()) {
            Log.d("ChannelsActivity-Click", channels.get(position).getGroup_title() + ":: " + channels.get(position).getChannelUrl());
            Intent intent = new Intent(ChannelsActivity.this, TvShowActivity2.class);
            intent.putExtra(TvShowActivity2.EXTRA_GROUP_TITLE, channels.get(position).getGroup_title());
            intent.putExtra(TvShowActivity2.EXTRA_TVG_LOGO, channels.get(position).getTvg_logo());
            intent.putExtra(TvShowActivity2.EXTRA_URL, channels.get(position).getChannelUrl());
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
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
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
