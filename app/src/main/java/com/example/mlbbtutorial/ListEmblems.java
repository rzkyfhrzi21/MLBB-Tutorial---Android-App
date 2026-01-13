package com.example.mlbbtutorial;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mlbbtutorial.adapter.ListEmblemSectionAdapter;
import com.example.mlbbtutorial.model.ListEmblemModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListEmblems extends AppCompatActivity {

    private RecyclerView rvEmblems;
    private SearchView searchEmblem;
    private ImageButton btnBack;

    private ListEmblemSectionAdapter adapter;

    // MASTER DATA
    private final ArrayList<ListEmblemModel> mainFull = new ArrayList<>();
    private final ArrayList<ListEmblemModel> abilityFull = new ArrayList<>();

    // ROWS (untuk 1 RecyclerView: header + item)
    private final ArrayList<ListEmblemSectionAdapter.Row> rows = new ArrayList<>();

    private static final String EMBLEM_API =
            "https://raw.githubusercontent.com/rzkyfhrzi21/mlbb-tutorial-api/refs/heads/master/emblems/emblems.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_emblems);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvEmblems = findViewById(R.id.rvEmblems);
        searchEmblem = findViewById(R.id.searchEmblem);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        rvEmblems.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListEmblemSectionAdapter(this, rows);
        rvEmblems.setAdapter(adapter);

        // SEARCH LANGSUNG AKTIF (TANPA KLIK ICON)
        searchEmblem.setIconifiedByDefault(false);
        searchEmblem.setFocusable(true);
        searchEmblem.setFocusableInTouchMode(true);
        searchEmblem.setQueryHint("Search Battle Spell...");
//        searchEmblem.requestFocus();

        // SEARCH (SAMA POLA BATTLE SPELL)
        searchEmblem.setQueryHint("Search Emblem...");
        searchEmblem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterAndBuildRows(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterAndBuildRows(newText);
                return true;
            }
        });

        loadEmblems();
    }

    private void loadEmblems() {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                EMBLEM_API,
                null,
                response -> {
                    mainFull.clear();
                    abilityFull.clear();

                    try {
                        JSONObject dataObj = response.getJSONObject("data");

                        // MAIN
                        JSONArray mainArr = dataObj.getJSONArray("main_emblems");
                        for (int i = 0; i < mainArr.length(); i++) {
                            JSONObject obj = mainArr.getJSONObject(i);

                            ArrayList<String> attrs = new ArrayList<>();
                            JSONArray attrArr = obj.getJSONArray("attributes");
                            for (int j = 0; j < attrArr.length(); j++) {
                                attrs.add(attrArr.getString(j));
                            }

                            ListEmblemModel m = new ListEmblemModel(
                                    obj.optString("name"),
                                    obj.optString("icon"),
                                    attrs
                            );
                            mainFull.add(m);
                        }

                        // ABILITY
                        JSONArray abilityArr = dataObj.getJSONArray("ability_emblems");
                        for (int i = 0; i < abilityArr.length(); i++) {
                            JSONObject obj = abilityArr.getJSONObject(i);

                            ListEmblemModel a = new ListEmblemModel(
                                    obj.optString("name"),
                                    obj.optString("icon"),
                                    obj.optInt("section", 0),
                                    obj.optString("benefits"),
                                    obj.isNull("desc") ? null : obj.optString("desc"),
                                    obj.isNull("cd") ? null : obj.optInt("cd")
                            );
                            abilityFull.add(a);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // awal tampil tanpa filter
                    filterAndBuildRows("");
                },
                error -> error.printStackTrace()
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void filterAndBuildRows(String keyword) {
        String key = (keyword == null) ? "" : keyword.trim().toLowerCase();

        ArrayList<ListEmblemModel> mainFiltered = new ArrayList<>();
        ArrayList<ListEmblemModel> abilityFiltered = new ArrayList<>();

        if (key.isEmpty()) {
            mainFiltered.addAll(mainFull);
            abilityFiltered.addAll(abilityFull);
        } else {
            for (ListEmblemModel m : mainFull) {
                if (m.getName() != null && m.getName().toLowerCase().contains(key)) {
                    mainFiltered.add(m);
                }
            }
            for (ListEmblemModel a : abilityFull) {
                if (a.getName() != null && a.getName().toLowerCase().contains(key)) {
                    abilityFiltered.add(a);
                }
            }
        }

        rows.clear();

        if (!mainFiltered.isEmpty()) {
            rows.add(ListEmblemSectionAdapter.Row.header("Main Emblems"));
            for (ListEmblemModel m : mainFiltered) {
                rows.add(ListEmblemSectionAdapter.Row.item(m));
            }
        }

        if (!abilityFiltered.isEmpty()) {
            rows.add(ListEmblemSectionAdapter.Row.header("Ability Emblems"));
            for (ListEmblemModel a : abilityFiltered) {
                rows.add(ListEmblemSectionAdapter.Row.item(a));
            }
        }

        adapter.notifyDataSetChanged();
    }
}
