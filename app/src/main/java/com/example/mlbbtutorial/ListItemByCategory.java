package com.example.mlbbtutorial;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

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
import com.example.mlbbtutorial.adapter.ListItemAdapter;
import com.example.mlbbtutorial.model.ListItemModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class ListItemByCategory extends AppCompatActivity {

    private static final String TAG = "ITEM_DEBUG";

    // ✅ FIX: JSON item ada di folder /items/
    private static final String BASE_API =
            "https://raw.githubusercontent.com/rzkyfhrzi21/mlbb-tutorial-api/refs/heads/master/items/";

    private RecyclerView rv;
    private ListItemAdapter adapter;

    private final ArrayList<ListItemModel> items = new ArrayList<>();
    private final ArrayList<ListItemModel> filtered = new ArrayList<>();

    private SearchView searchItem;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_item_by_category);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets sys = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(sys.left, sys.top, sys.right, sys.bottom);
            return insets;
        });

        String category = getIntent().getStringExtra("item_category");
        if (category == null || category.trim().isEmpty()) {
            category = "attack"; // default aman
            Log.w(TAG, "item_category kosong -> default: " + category);
        }

        // bersihkan category biar cocok nama file: attack.json, defense.json, dll
        category = category.toLowerCase(Locale.ROOT).trim().replace(" ", "").replace("_", "");

        // Title tampilan
        TextView title = findViewById(R.id.txtTitle);
        title.setText(toTitleCase(category) + " Items");

        ImageButton back = findViewById(R.id.btnBack);
        back.setOnClickListener(v -> finish());

        rv = findViewById(R.id.rvItems);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ListItemAdapter(this, filtered);
        rv.setAdapter(adapter);

        // SEARCH
        searchItem = findViewById(R.id.searchItem);
        searchItem.setIconifiedByDefault(false);
        searchItem.setQueryHint("Search Item...");
        searchItem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) {
                filter(query);
                return true;
            }

            @Override public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        loadItems(category);
    }

    private void loadItems(String category) {
        String url = BASE_API + category + ".json";
        Log.e(TAG, "REQUEST URL = " + url);

        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                res -> {
                    try {
                        JSONArray data = res.getJSONArray("data");

                        items.clear();
                        filtered.clear();

                        Log.e(TAG, "TOTAL ITEM = " + data.length());

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject o = data.getJSONObject(i);

                            ListItemModel m = new ListItemModel(
                                    o.optString("name"),
                                    o.optString("icon"),
                                    o.optInt("price"),
                                    o.isNull("sell") ? null : o.optInt("sell"),
                                    toStringList(o.optJSONArray("attribute")),
                                    toEffectList(o.optJSONArray("unique_passive")),
                                    toEffectList(o.optJSONArray("unique_active"))
                            );

                            items.add(m);

                            Log.e(TAG, "ITEM[" + i + "] " + m.name + " | icon=" + m.icon);
                        }

                        filtered.addAll(items);
                        adapter.notifyDataSetChanged();

                        Log.e(TAG, "ADAPTER COUNT = " + adapter.getItemCount());

                    } catch (Exception e) {
                        Log.e(TAG, "PARSE ERROR", e);
                    }
                },
                err -> {
                    Log.e(TAG, "REQUEST ERROR", err);
                    if (err.networkResponse != null) {
                        Log.e(TAG, "STATUS CODE = " + err.networkResponse.statusCode);
                    }
                }
        );

        Volley.newRequestQueue(this).add(req);
    }

    private void filter(String q) {
        filtered.clear();

        if (q == null || q.trim().isEmpty()) {
            filtered.addAll(items);
        } else {
            String key = q.toLowerCase(Locale.ROOT);
            for (ListItemModel m : items) {
                String name = (m.getName() == null) ? "" : m.getName();
                if (name.toLowerCase(Locale.ROOT).contains(key)) {
                    filtered.add(m);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

    private ArrayList<String> toStringList(JSONArray arr) {
        ArrayList<String> list = new ArrayList<>();
        if (arr == null) return list;
        for (int i = 0; i < arr.length(); i++) {
            list.add(arr.optString(i));
        }
        return list;
    }

    private ArrayList<ListItemModel.Effect> toEffectList(JSONArray arr) {
        ArrayList<ListItemModel.Effect> list = new ArrayList<>();
        if (arr == null) return list;

        for (int i = 0; i < arr.length(); i++) {
            JSONObject o = arr.optJSONObject(i);
            if (o != null) {
                list.add(new ListItemModel.Effect(
                        o.optString("name"),
                        o.optString("desc")
                ));
            }
        }
        return list;
    }

    private String toTitleCase(String input) {
        if (input == null) return "";
        String s = input.trim().replace("_", " ");
        if (s.isEmpty()) return "";

        String[] parts = s.split("\\s+");
        StringBuilder out = new StringBuilder();
        for (String p : parts) {
            if (p.isEmpty()) continue;
            out.append(Character.toUpperCase(p.charAt(0)))
                    .append(p.substring(1).toLowerCase(Locale.ROOT))
                    .append(" ");
        }
        return out.toString().trim();
    }
}
