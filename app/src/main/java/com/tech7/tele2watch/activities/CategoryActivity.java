package com.tech7.tele2watch.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.tech7.tele2watch.R;
import com.tech7.tele2watch.adapter.CategoryAdapter;
import com.tech7.tele2watch.object.Category;
import com.tech7.tele2watch.object.Channel;
import com.tech7.tele2watch.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity implements CategoryAdapter.RecyclerViewClickListenerCategory {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private String titleBar;

    RecyclerView rcvCategories;
    CategoryAdapter adapter;
    List<Category> categories;
    private static String JSON_URL = "http://test.panzidrc.com/api/categories";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        initToolbar();
        categories = new ArrayList<>();
        rcvCategories = findViewById(R.id.rcvCategories);

        extractCategories();
    }

    private void initToolbar() {
        //setting title bar
        titleBar = "Categories TV";
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(titleBar);
        Tools.setSystemBarColor(this, R.color.colorPrimary);
    }

    private void extractCategories(){
        // Display the progress bar.
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        //json
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject categoryObject = response.getJSONObject(i);

                        Category category = new Category();
                        category.setCategoryName(categoryObject.getString("CategoryName").toString());
                        //category.setCategoryImage(categoryObject.getString("CategoryImage").toString());
                        categories.add(category);
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
            }
        });

        queue.add(jsonArrayRequest);
    }

    private void setAdapter() {
        // set adapter
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        adapter = new CategoryAdapter(getApplication(), categories, this);
        rcvCategories.setLayoutManager(gridLayoutManager);
        rcvCategories.setAdapter(adapter);

        //Hide progressbar
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
    }


    @Override
    public void onCategoryRowClicked(int position) {

    }

    @Override
    public void onCategoryViewClicked(View v, int position) {
        if (!categories.isEmpty()) {
            Log.d("CategActivity-ViewClick", categories.get(position).getCategoryName());
            Intent intent = new Intent(CategoryActivity.this, ChannelsByCategoryActivity.class);
            intent.putExtra(ChannelsByCategoryActivity.EXTRA_CATEGORYNAME, categories.get(position).getCategoryName());
            //start CategoryDetail activity
            startActivity(intent);
        }
    }
}
