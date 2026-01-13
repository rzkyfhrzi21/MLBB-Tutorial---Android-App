package com.example.mlbbtutorial;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.Locale;

public class ListHeroes extends AppCompatActivity {

    private RecyclerView rvHeroes;
    private ListHeroAdapter heroAdapter;

    private final ArrayList<ListHeroModel> heroList = new ArrayList<>();
    private final ArrayList<ListHeroModel> heroFull = new ArrayList<>();

    private String keyword = "";
    private String selectedRole = "all";

    private static final String HERO_LIST_API =
            "https://raw.githubusercontent.com/rzkyfhrzi21/mlbb-tutorial-api/refs/heads/master/heroes/list_heroes.json";

    // PRIMARY COLOR
    private static final int PRIMARY = 0xFF64FFDA;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_heroes);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, i) -> {
            Insets s = i.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(s.left, s.top, s.right, s.bottom);
            return i;
        });

        // BACK
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // SEARCH
        EditText search = findViewById(R.id.searchHero);
        search.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s,int a,int b,int c){}
            public void onTextChanged(CharSequence s,int a,int b,int c){
                keyword = s.toString();
                applyFilter();
            }
            public void afterTextChanged(Editable e){}
        });

        // SPINNER ROLE (TEKS PRIMARY, TIDAK PAKAI bg_spinner)
        Spinner spinnerRole = findViewById(R.id.spinnerRole);
        String[] roles = {"All","Assassin","Marksman","Mage","Fighter","Tank","Support"};

        ArrayAdapter<String> roleAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                roles
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextColor(PRIMARY);                 // ✅ teks yang tampil di bar
                tv.setTextSize(14);
                tv.setSingleLine(true);
                return tv;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView tv = (TextView) super.getDropDownView(position, convertView, parent);
                tv.setTextColor(PRIMARY);                 // ✅ teks item dropdown
                tv.setBackgroundColor(0xFF121212);        // optional: biar dropdown gelap rapi
                tv.setPadding(24, 24, 24, 24);
                return tv;
            }
        };

        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(roleAdapter);

        spinnerRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                selectedRole = roles[pos].toLowerCase(Locale.ROOT);
                applyFilter();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // RECYCLER
        rvHeroes = findViewById(R.id.rvHeroes);
        rvHeroes.setLayoutManager(new GridLayoutManager(this, 4));

        heroAdapter = new ListHeroAdapter(this, heroList);
        rvHeroes.setAdapter(heroAdapter);

        loadHeroes();
    }

    private void loadHeroes() {
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                HERO_LIST_API,
                null,
                res -> {
                    try {
                        JSONArray arr = res.getJSONArray("hero");

                        heroList.clear();
                        heroFull.clear();

                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject o = arr.getJSONObject(i);

                            ArrayList<String> roles = new ArrayList<>();
                            JSONArray roleArr = o.optJSONArray("role");
                            if (roleArr != null) {
                                for (int r = 0; r < roleArr.length(); r++) {
                                    roles.add(roleArr.optString(r).toLowerCase(Locale.ROOT));
                                }
                            }

                            ListHeroModel hero = new ListHeroModel(
                                    o.optString("hero_id"),
                                    o.optString("hero_name"),
                                    o.optString("hero_icon"),
                                    o.optString("hero_url"),
                                    roles
                            );

                            heroList.add(hero);
                            heroFull.add(hero);
                        }

                        heroAdapter.notifyDataSetChanged();
                        applyFilter(); // biar role default "all" langsung sinkron

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace
        );

        Volley.newRequestQueue(this).add(req);
    }

    private void applyFilter() {
        heroList.clear();

        String key = keyword == null ? "" : keyword.trim().toLowerCase(Locale.ROOT);

        for (ListHeroModel h : heroFull) {

            boolean matchName =
                    key.isEmpty()
                            || h.getHeroName().toLowerCase(Locale.ROOT).contains(key);

            boolean matchRole =
                    selectedRole.equals("all")
                            || (h.getRoles() != null && h.getRoles().contains(selectedRole));

            if (matchName && matchRole) {
                heroList.add(h);
            }
        }

        heroAdapter.notifyDataSetChanged();
    }
}
