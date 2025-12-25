package com.example.mlbbtutorial;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mlbbtutorial.adapter.ListHeroAdapter;
import com.example.mlbbtutorial.model.ListHeroModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListHeroes extends AppCompatActivity {

    private RecyclerView rvHeroes;
    private ListHeroAdapter heroAdapter;
    private final ArrayList<ListHeroModel> heroList = new ArrayList<>();

    private static final String HERO_LIST_API =
            "https://raw.githubusercontent.com/rzkyfhrzi21/mlbb-tutorial-api/refs/heads/master/heroes/list_heroes.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_heroes);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvHeroes = findViewById(R.id.rvHeroes);
        rvHeroes.setLayoutManager(new GridLayoutManager(this, 4));

        heroAdapter = new ListHeroAdapter(this, heroList);
        rvHeroes.setAdapter(heroAdapter);

        loadHeroes();
    }

    private void loadHeroes() {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                HERO_LIST_API,
                null,
                response -> {
                    heroList.clear();
                    try {
                        JSONArray heroes = response.getJSONArray("hero");
                        for (int i = 0; i < heroes.length(); i++) {
                            JSONObject obj = heroes.getJSONObject(i);

                            heroList.add(new ListHeroModel(
                                    obj.optString("hero_id"),
                                    obj.optString("hero_name"),
                                    obj.optString("hero_icon"),
                                    obj.optString("hero_url")
                            ));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    heroAdapter.notifyDataSetChanged();
                },
                error -> error.printStackTrace()
        );

        Volley.newRequestQueue(this).add(request);
    }
}
