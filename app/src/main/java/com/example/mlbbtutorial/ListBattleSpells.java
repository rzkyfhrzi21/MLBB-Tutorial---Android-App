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
import com.example.mlbbtutorial.adapter.ListBattleSpellAdapter;
import com.example.mlbbtutorial.model.ListBattleSpellModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListBattleSpells extends AppCompatActivity {

    private RecyclerView rvBattleSpells;
    private SearchView searchBattleSpell;
    private ImageButton btnBack;

    private ListBattleSpellAdapter spellAdapter;
    private final ArrayList<ListBattleSpellModel> spellList = new ArrayList<>();
    private final ArrayList<ListBattleSpellModel> spellListFull = new ArrayList<>();

    private static final String BATTLE_SPELL_API =
            "https://raw.githubusercontent.com/rzkyfhrzi21/mlbb-tutorial-api/refs/heads/master/battle_spells/battle_spells.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_battle_spells);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvBattleSpells = findViewById(R.id.rvBattleSpells);
        searchBattleSpell = findViewById(R.id.searchBattleSpell);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        rvBattleSpells.setLayoutManager(new LinearLayoutManager(this));
        spellAdapter = new ListBattleSpellAdapter(this, spellList);
        rvBattleSpells.setAdapter(spellAdapter);

        // SEARCH LANGSUNG AKTIF (TANPA KLIK ICON)
        searchBattleSpell.setIconifiedByDefault(false);
        searchBattleSpell.setFocusable(true);
        searchBattleSpell.setFocusableInTouchMode(true);
        searchBattleSpell.setQueryHint("Search Battle Spell...");
//        searchBattleSpell.requestFocus();

        searchBattleSpell.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterSpell(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterSpell(newText);
                return true;
            }
        });

        loadBattleSpells();
    }

    private void loadBattleSpells() {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                BATTLE_SPELL_API,
                null,
                response -> {
                    spellList.clear();
                    spellListFull.clear();
                    try {
                        JSONArray spells = response.getJSONArray("data");
                        for (int i = 0; i < spells.length(); i++) {
                            JSONObject obj = spells.getJSONObject(i);

                            ListBattleSpellModel spell = new ListBattleSpellModel(
                                    obj.optString("name"),
                                    obj.optString("icon"),
                                    obj.optString("usage"),
                                    obj.optString("description"),
                                    obj.optInt("cooldown", 0),
                                    obj.optInt("unlocked_at_level", 0)
                            );

                            spellList.add(spell);
                            spellListFull.add(spell);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    spellAdapter.notifyDataSetChanged();
                },
                error -> error.printStackTrace()
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void filterSpell(String keyword) {
        spellList.clear();

        if (keyword == null || keyword.trim().isEmpty()) {
            spellList.addAll(spellListFull);
        } else {
            String key = keyword.toLowerCase();
            for (ListBattleSpellModel spell : spellListFull) {
                if (spell.getName().toLowerCase().contains(key)) {
                    spellList.add(spell);
                }
            }
        }
        spellAdapter.notifyDataSetChanged();
    }
}
