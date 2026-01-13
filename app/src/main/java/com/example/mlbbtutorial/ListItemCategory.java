package com.example.mlbbtutorial;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mlbbtutorial.adapter.ItemCategoryAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ListItemCategory extends AppCompatActivity {

    private RecyclerView rvCategory;
    private ImageButton btnBack;

    private final List<String> categoryList = new ArrayList<>();

    private static final String CATEGORY_API =
            "https://raw.githubusercontent.com/rzkyfhrzi21/mlbb-tutorial-api/refs/heads/master/items/list_category.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item_category);

        rvCategory = findViewById(R.id.rvCategory);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        rvCategory.setLayoutManager(new LinearLayoutManager(this));

        loadCategories();
    }

    private void loadCategories() {

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                CATEGORY_API,
                null,
                response -> {
                    categoryList.clear();

                    try {
                        JSONArray data = response.getJSONArray("data");

                        // Pakai Set supaya category tidak duplikat
                        Set<String> uniqueCategories = new LinkedHashSet<>();

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = data.getJSONObject(i);
                            JSONArray categories = obj.getJSONArray("item_category");

                            for (int j = 0; j < categories.length(); j++) {
                                uniqueCategories.add(categories.getString(j));
                            }
                        }

                        categoryList.addAll(uniqueCategories);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Adapter + intent ke ItemByCategory
                    ItemCategoryAdapter adapter = new ItemCategoryAdapter(
                            this,
                            categoryList,
                            category -> {
                                Intent i = new Intent(
                                        ListItemCategory.this,
                                        ListItemByCategory.class
                                );
                                i.putExtra(
                                        "item_category",
                                        category.toLowerCase() // attack, magic, dst
                                );
                                startActivity(i);
                            }
                    );

                    rvCategory.setAdapter(adapter);
                },
                error -> error.printStackTrace()
        );

        Volley.newRequestQueue(this).add(request);
    }
}
