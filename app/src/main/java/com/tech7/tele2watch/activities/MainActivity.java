package com.tech7.tele2watch.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.tech7.tele2watch.R;
import com.tech7.tele2watch.adapter.ChannelAdapterDivertissement;
import com.tech7.tele2watch.adapter.ChannelAdapterNews;
import com.tech7.tele2watch.adapter.ChannelAdapterDocumentary;
import com.tech7.tele2watch.adapter.ChannelAdapterFilm;
import com.tech7.tele2watch.adapter.ChannelAdapterSport;
import com.tech7.tele2watch.object.Channel;
import com.tech7.tele2watch.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ChannelAdapterNews.RecyclerViewClickListenerNews,
        ChannelAdapterFilm.RecyclerViewClickListenerFilm, ChannelAdapterSport.RecyclerViewClickListenerSport,
        ChannelAdapterDocumentary.RecyclerViewClickListenerDocumentary, ChannelAdapterDivertissement.RecyclerViewClickListenerDivertissement{

    RecyclerView rcvFilmList, rcvNewsList, rcvDocumentaryList, rcvSportList, rcvDiversList;

    ChannelAdapterNews adapterNews;
    ChannelAdapterFilm adapterFilm;
    ChannelAdapterSport adapterSport;
    ChannelAdapterDocumentary adapterDocumentary;
    ChannelAdapterDivertissement adapterDivertissement;
    List<Channel> channelsFilm, channelsNews, channelsSport, channelsDocumentary, channelsDivers;

    //private static String JSON_URL = "http://test.panzidrc.com/api/channels";   // json url from API to fetch all channels
    private static String JSON_URL_NEWS = "http://test.panzidrc.com/api/channels/news";   // json url from API to fetch news channels
    private static String JSON_URL_FILMS = "http://test.panzidrc.com/api/channels/film";   // json url from API to fetch films channels
    private static String JSON_URL_SPORT = "http://test.panzidrc.com/api/channels/sport";   // json url from API to fetch sport channels
    private static String JSON_URL_DOCUMENTARY = "http://test.panzidrc.com/api/channels/documentary";   // json url from API to fetch documentary channels
    private static String JSON_URL_Divers = "http://test.panzidrc.com/api/channels/category?categoryname=divertissement";   // json url from API to fetch music channels

    private Toolbar toolbar;
    private ActionBar actionBar;
    private String titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initNavigationMenu();

        rcvFilmList = findViewById(R.id.rcvFilmList);
        rcvNewsList = findViewById(R.id.rcvNewsList);
        rcvSportList = findViewById(R.id.rcvSportList);
        rcvDocumentaryList = findViewById(R.id.rcvDocumentaryList);
        rcvDiversList = findViewById(R.id.rcvDiversList);

        channelsNews = new ArrayList<>();
        channelsFilm = new ArrayList<>();
        channelsSport = new ArrayList<>();
        channelsDocumentary = new ArrayList<>();
        channelsDivers = new ArrayList<>();

        // Display the progress bar.
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);

        extractFilmChannels();
        extractNewsChannels();
        extractSportChannels();
        extractDocumentaryChannels();
        extractDiversChannels();

        //Hide progressbar
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
    }

    private void initToolbar() {
        //setting title bar
        titleBar = "Live TV";
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(titleBar);
        Tools.setSystemBarColor(this, R.color.colorPrimary);
    }

    private void extractNewsChannels() {
        //json
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL_NEWS, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject channelObject = response.getJSONObject(i);

                        Channel channel = new Channel();
                        channel.setGroup_title(channelObject.getString("Group_title").toString());
                        channel.setTvg_logo(channelObject.getString("Tvg_logo").toString());
                        channel.setChannelUrl(channelObject.getString("ChannelUrl").toString());
                        channelsNews.add(channel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                setNewsAdapter();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tagNews", "onErrorResponse: " + error.getMessage());
            }
        });

        queue.add(jsonArrayRequest);
    }

    private void extractFilmChannels() {
        //json
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL_FILMS, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject channelObject = response.getJSONObject(i);

                        Channel channel = new Channel();
                        channel.setGroup_title(channelObject.getString("Group_title").toString());
                        channel.setTvg_logo(channelObject.getString("Tvg_logo").toString());
                        channel.setChannelUrl(channelObject.getString("ChannelUrl").toString());
                        channelsFilm.add(channel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                setFilmAdapter();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tagFilm", "onErrorResponse: " + error.getMessage());
            }
        });

        queue.add(jsonArrayRequest);
    }

    private void extractSportChannels() {
        //json
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL_SPORT, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject channelObject = response.getJSONObject(i);

                        Channel channel = new Channel();
                        channel.setGroup_title(channelObject.getString("Group_title").toString());
                        channel.setTvg_logo(channelObject.getString("Tvg_logo").toString());
                        channel.setChannelUrl(channelObject.getString("ChannelUrl").toString());
                        channelsSport.add(channel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                setSportAdapter();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tagSport", "onErrorResponse: " + error.getMessage());
            }
        });

        queue.add(jsonArrayRequest);
    }

    private void extractDocumentaryChannels() {
        //json
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL_DOCUMENTARY, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject channelObject = response.getJSONObject(i);

                        Channel channel = new Channel();
                        channel.setGroup_title(channelObject.getString("Group_title").toString());
                        channel.setTvg_logo(channelObject.getString("Tvg_logo").toString());
                        channel.setChannelUrl(channelObject.getString("ChannelUrl").toString());
                        channelsDocumentary.add(channel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                setDocumentaryAdapter();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tagDocumentary", "onErrorResponse: " + error.getMessage());
            }
        });

        queue.add(jsonArrayRequest);
    }

    private void extractDiversChannels() {
        //json
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL_Divers, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject channelObject = response.getJSONObject(i);

                        Channel channel = new Channel();
                        channel.setGroup_title(channelObject.getString("Group_title").toString());
                        channel.setTvg_logo(channelObject.getString("Tvg_logo").toString());
                        channel.setChannelUrl(channelObject.getString("ChannelUrl").toString());
                        channelsDivers.add(channel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                setDiversAdapter();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tagDocumentary", "onErrorResponse: " + error.getMessage());
            }
        });

        queue.add(jsonArrayRequest);
    }

    private void setNewsAdapter() {

        //News adapter
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        adapterNews = new ChannelAdapterNews(getApplication(), channelsNews, this);
        rcvNewsList.setLayoutManager(layoutManager);
        rcvNewsList.setAdapter(adapterNews);
    }

    private void setFilmAdapter() {

        //Film adapter
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        adapterFilm = new ChannelAdapterFilm(getApplication(), channelsFilm, this);
        rcvFilmList.setLayoutManager(layoutManager);
        rcvFilmList.setAdapter(adapterFilm);
    }

    private void setSportAdapter() {

        //Sport adapter
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        adapterSport = new ChannelAdapterSport(getApplication(), channelsSport, this);
        rcvSportList.setLayoutManager(layoutManager);
        rcvSportList.setLayoutManager(layoutManager);
        rcvSportList.setAdapter(adapterSport);
    }

    private void setDocumentaryAdapter() {

        //Documentary adapter
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        adapterDocumentary = new ChannelAdapterDocumentary(getApplication(), channelsDocumentary, this);
        rcvDocumentaryList.setLayoutManager(layoutManager);
        rcvDocumentaryList.setAdapter(adapterDocumentary);
    }

    private void setDiversAdapter() {

        //Divertissement adapter
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        adapterDivertissement = new ChannelAdapterDivertissement(getApplication(),channelsDivers,this);
        rcvDiversList.setLayoutManager(layoutManager);
        rcvDiversList.setAdapter(adapterDivertissement);
    }

    //handle on item recyclerview click
    @Override
    public void onDocumRowClicked(int position) {

    }

    @Override
    public void onDocumViewClicked(View v, int position) {
        if (!channelsDocumentary.isEmpty()) {
            Log.d("MainActivity-ViewClick", channelsDocumentary.get(position).getGroup_title() + ":: " + channelsDocumentary.get(position).getChannelUrl());
            Intent intent = new Intent(MainActivity.this, TvShowActivity.class);
            intent.putExtra(TvShowActivity.EXTRA_GROUP_TITLE, channelsDocumentary.get(position).getGroup_title());
            intent.putExtra(TvShowActivity.EXTRA_TVG_LOGO, channelsDocumentary.get(position).getTvg_logo());
            intent.putExtra(TvShowActivity.EXTRA_URL, channelsDocumentary.get(position).getChannelUrl());
            //start tv activity
            startActivity(intent);
        }
    }

    @Override
    public void onFilmRowClicked(int position) {

    }

    @Override
    public void onFilmViewClicked(View v, int position) {
        if (!channelsFilm.isEmpty()) {
            Log.d("MainActivity-ViewClick", channelsFilm.get(position).getGroup_title() + ":: " + channelsFilm.get(position).getChannelUrl());
            Intent intent = new Intent(MainActivity.this, TvShowActivity.class);
            intent.putExtra(TvShowActivity.EXTRA_GROUP_TITLE, channelsFilm.get(position).getGroup_title());
            intent.putExtra(TvShowActivity.EXTRA_TVG_LOGO, channelsFilm.get(position).getTvg_logo());
            intent.putExtra(TvShowActivity.EXTRA_URL, channelsFilm.get(position).getChannelUrl());
            //start tv activity
            startActivity(intent);
        }
    }

    @Override
    public void onNewsRowClicked(int position) {

    }

    @Override
    public void onNewsViewClicked(View v, int position) {
        if (!channelsNews.isEmpty()) {
            Log.d("MainActivity-ViewClick", channelsNews.get(position).getGroup_title() + ":: " + channelsNews.get(position).getChannelUrl());
            Intent intent = new Intent(MainActivity.this, TvShowActivity.class);
            intent.putExtra(TvShowActivity.EXTRA_GROUP_TITLE, channelsNews.get(position).getGroup_title());
            intent.putExtra(TvShowActivity.EXTRA_TVG_LOGO, channelsNews.get(position).getTvg_logo());
            intent.putExtra(TvShowActivity.EXTRA_URL, channelsNews.get(position).getChannelUrl());
            //start tv activity
            startActivity(intent);
        }
    }

    @Override
    public void onSportRowClicked(int position) {

    }

    @Override
    public void onSportViewClicked(View v, int position) {
        if (!channelsSport.isEmpty()) {
            Log.d("MainActivity-ViewClick", channelsSport.get(position).getGroup_title() + ":: " + channelsSport.get(position).getChannelUrl());
            Intent intent = new Intent(MainActivity.this, TvShowActivity.class);
            intent.putExtra(TvShowActivity.EXTRA_GROUP_TITLE, channelsSport.get(position).getGroup_title());
            intent.putExtra(TvShowActivity.EXTRA_TVG_LOGO, channelsSport.get(position).getTvg_logo());
            intent.putExtra(TvShowActivity.EXTRA_URL, channelsSport.get(position).getChannelUrl());
            //start tv activity
            startActivity(intent);
        }
    }

    @Override
    public void onDiversRowClicked(int position) {

    }

    @Override
    public void onDiversViewClicked(View v, int position) {
        if (!channelsDivers.isEmpty()) {
            Log.d("MainActivity-ViewClick", channelsDivers.get(position).getGroup_title() + ":: " + channelsDivers.get(position).getChannelUrl());
            Intent intent = new Intent(MainActivity.this, TvShowActivity.class);
            intent.putExtra(TvShowActivity.EXTRA_GROUP_TITLE, channelsDivers.get(position).getGroup_title());
            intent.putExtra(TvShowActivity.EXTRA_TVG_LOGO, channelsDivers.get(position).getTvg_logo());
            intent.putExtra(TvShowActivity.EXTRA_URL, channelsDivers.get(position).getChannelUrl());
            //start tv activity
            startActivity(intent);
        }
    }

    private void initNavigationMenu() {
        final NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                updateCounter(nav_view);
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                //Toast.makeText(getApplicationContext(), item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();

                // Handle navigation view item clicks here.
                int id = item.getItemId();
                if (id == R.id.nav_allchannels) {

                    // Handle the camera action
                    actionBar.setTitle(item.getTitle());
                    Intent intent = new Intent(MainActivity.this,ChannelsActivity.class);
                    startActivity(intent);
                }

                else if (id == R.id.nav_category) {

                    // Handle the camera action
                    actionBar.setTitle(item.getTitle());
                    //Intent intent = new Intent(MainActivity.this,CategoryActivity.class);
                    //startActivity(intent);
                }

                else if (id == R.id.nav_country) {

                    // Handle the camera action
                    actionBar.setTitle(item.getTitle());
                    Intent intent = new Intent(MainActivity.this,CountriesActivity.class);
                    startActivity(intent);
                }

                else if (id == R.id.nav_discover) {
                    // Handle the camera action
                    actionBar.setTitle(item.getTitle());
                    //Intent intent = new Intent(getApplicationContext(),DiscoverActivity.class);
                    //startActivity(intent);
                }

                else if (id == R.id.nav_program) {
                    // Handle the camera action
                    actionBar.setTitle(item.getTitle());
                    //Intent intent = new Intent(getApplicationContext(),ProgramActivity.class);
                    //startActivity(intent);
                }

                else if (id == R.id.nav_contact) {
                    // Handle the camera action
                    actionBar.setTitle(item.getTitle());
                    //Intent intent = new Intent(getApplicationContext(),ContactActivity.class);
                    //startActivity(intent);
                }

                else if (id == R.id.nav_offer) {
                    // Handle the camera action
                    actionBar.setTitle(item.getTitle());
                    //Intent intent = new Intent(getApplicationContext(),OfferActivity.class);
                    //startActivity(intent);
                }

                drawer.closeDrawers();
                return true;
            }
        });

        // open drawer at start
        drawer.openDrawer(GravityCompat.START);
        updateCounter(nav_view);
    }

    private void updateCounter(NavigationView nav) {
        Menu m = nav.getMenu();
        ((TextView) m.findItem(R.id.nav_allchannels).getActionView().findViewById(R.id.text)).setText("456");
        ((TextView) m.findItem(R.id.nav_category).getActionView().findViewById(R.id.text)).setText("6");
        ((TextView) m.findItem(R.id.nav_country).getActionView().findViewById(R.id.text)).setText("85");
        ((TextView) m.findItem(R.id.nav_discover).getActionView().findViewById(R.id.text)).setText("47");
    }
}
