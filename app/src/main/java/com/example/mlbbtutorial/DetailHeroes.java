package com.example.mlbbtutorial;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mlbbtutorial.adapter.HeroAttributeAdapter;
import com.example.mlbbtutorial.adapter.HeroSkillAdapter;
import com.example.mlbbtutorial.model.HeroSkillModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class DetailHeroes extends AppCompatActivity {

    private static final String BASE_API =
            "https://raw.githubusercontent.com/rzkyfhrzi21/mlbb-tutorial-api/refs/heads/master/heroes/";
    private static final String BASE_ASSET =
            "https://raw.githubusercontent.com/rzkyfhrzi21/mlbb-tutorial-api/refs/heads/master/assets/";
    private static final String DEFAULT_SKILL =
            BASE_ASSET + "skills/default.webp";

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_heroes);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {
                    Insets i = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(i.left, i.top, i.right, i.bottom);
                    return insets;
                });

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        String heroName = getIntent().getStringExtra("hero_name");
        if (heroName != null) loadHero(heroName.toLowerCase());
    }

    private void loadHero(String hero) {

        String url = BASE_API + hero + ".json";

        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                res -> {
                    try {
                        JSONObject h = res.getJSONObject("hero");

                        TextView txtName = findViewById(R.id.txtHeroName);
                        TextView txtRelease = findViewById(R.id.txtReleaseYear);
                        TextView txtRole = findViewById(R.id.txtRole);
                        TextView txtSpec = findViewById(R.id.txtSpeciality);
                        TextView txtLane = findViewById(R.id.txtLaning);
                        TextView txtPrice = findViewById(R.id.txtPrice);
                        ImageView imgHero = findViewById(R.id.imgHero);

                        // ===== TEXT (TITLE CASE) =====
                        txtName.setText(titleCase(h.optString("hero_name")));

                        txtRelease.setText("Released Date: " + h.optInt("release_year", 0));
                        txtRole.setText("Role: " + titleCase(joinArray(h.optJSONArray("role"))));
                        txtSpec.setText("Speciality: " + titleCase(joinArray(h.optJSONArray("speciality"))));
                        txtLane.setText("Laning: " + titleCase(joinArray(h.optJSONArray("laning"))));

                        JSONObject price = h.optJSONObject("price");
                        if (price != null) {
                            int bp = price.optInt("battle_point", 0);
                            int dm = price.optInt("diamond", 0);
                            txtPrice.setText("Price: BP " + bp + " • Diamond " + dm);
                        } else {
                            txtPrice.setText("Price: -");
                        }

                        // ===== HERO ICON (TETAP) =====
                        String heroIcon = h.optString("hero_icon", "").toLowerCase(Locale.ROOT);
                        String heroIconUrl = heroIcon.isEmpty()
                                ? BASE_ASSET + "default.webp"
                                : BASE_ASSET + "heroes/" + heroIcon;

                        Picasso.get()
                                .load(heroIconUrl)
                                .resize(240, 240)
                                .centerInside()
                                .into(imgHero, new com.squareup.picasso.Callback() {
                                    @Override public void onSuccess() {}

                                    @Override
                                    public void onError(Exception e) {
                                        Picasso.get()
                                                .load(BASE_ASSET + "default.webp")
                                                .resize(240, 240)
                                                .centerInside()
                                                .into(imgHero);
                                    }
                                });

                        // ===== SKILLS (TETAP, JANGAN DIUBAH) =====
                        ArrayList<HeroSkillModel> skillList = new ArrayList<>();
                        JSONArray skills = h.getJSONArray("skills");

                        for (int i = 0; i < skills.length(); i++) {
                            JSONObject s = skills.getJSONObject(i);

                            String icon = s.optString("skill_icon", "");
                            String iconUrlSkill = icon.isEmpty()
                                    ? DEFAULT_SKILL
                                    : BASE_ASSET + "skills/" + icon.toLowerCase();

                            skillList.add(new HeroSkillModel(
                                    s.optString("skill_name"),
                                    iconUrlSkill,
                                    s.optString("type"),
                                    s.optString("description"),
                                    toStringList(s.optJSONArray("skill_unique")),
                                    toIntList(s.optJSONArray("cooldown")),
                                    toIntList(s.optJSONArray("manacost"))
                            ));
                        }

                        RecyclerView rvSkill = findViewById(R.id.rvSkills);
                        rvSkill.setLayoutManager(
                                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                        );
                        rvSkill.setAdapter(new HeroSkillAdapter(this, skillList));

                        // ===== ATTRIBUTES (TETAP) =====
                        RecyclerView rvAttr = findViewById(R.id.rvAttributes);
                        rvAttr.setLayoutManager(new GridLayoutManager(this, 2));
                        rvAttr.setAdapter(
                                new HeroAttributeAdapter(h.getJSONObject("base_attributes"))
                        );

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace
        );

        Volley.newRequestQueue(this).add(req);
    }

    /* ========================= UTIL ========================= */

    private String joinArray(JSONArray a) {
        if (a == null) return "-";
        ArrayList<String> l = new ArrayList<>();
        for (int i = 0; i < a.length(); i++) l.add(a.optString(i));
        return android.text.TextUtils.join(" / ", l);
    }

    private ArrayList<String> toStringList(JSONArray a) {
        ArrayList<String> l = new ArrayList<>();
        if (a == null) return l;
        for (int i = 0; i < a.length(); i++) l.add(a.optString(i));
        return l;
    }

    private ArrayList<Integer> toIntList(JSONArray a) {
        ArrayList<Integer> l = new ArrayList<>();
        if (a == null) return l;
        for (int i = 0; i < a.length(); i++) l.add(a.optInt(i));
        return l;
    }

    // ===== TITLE CASE HELPER =====
    private String titleCase(String s) {
        if (s == null || s.trim().isEmpty()) return "-";
        String[] p = s.replace("_", " ").split("\\s+");
        StringBuilder out = new StringBuilder();
        for (String x : p) {
            out.append(Character.toUpperCase(x.charAt(0)))
                    .append(x.substring(1).toLowerCase(Locale.ROOT))
                    .append(" ");
        }
        return out.toString().trim();
    }
}
