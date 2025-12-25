package com.example.mlbbtutorial;

import android.util.Log;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mlbbtutorial.adapter.SkillAdapter;
import com.example.mlbbtutorial.model.SkillModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailHeroes extends AppCompatActivity {

    // ================= VIEW =================
    private ImageView IV_Hero;
    private TextView TV_HeroName, TV_ReleaseYear, TV_Role, TV_Speciality;
    private TextView TV_HP, TV_Mana, TV_PhysicalAtk, TV_MagicRes, TV_MoveSpeed;
    private RecyclerView RV_Skills;

    // ================= URL =================
    private static final String BASE_HERO_URL =
            "https://raw.githubusercontent.com/rzkyfhrzi21/mlbb-tutorial-api/refs/heads/master/heroes/";

    private static final String HERO_IMAGE =
            "https://raw.githubusercontent.com/rzkyfhrzi21/mlbb-tutorial-api/9c6d2251a29d5ec6058dbbafd089806b432f9299/assets/heroes/webp/";

    private static final String DEFAULT_HERO_ASSETS =
            HERO_IMAGE + "default_skill.webp";

    // ================= DATA =================
    private final ArrayList<SkillModel> skillList = new ArrayList<>();
    private SkillAdapter skillAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_heroes);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        initViews();
//        setupRecycler();
        loadHeroData();
    }

    // ================= INIT VIEW =================
//    private void initViews() {
//        IV_Hero = findViewById(R.id.IV_Hero);
//
//        TV_HeroName = findViewById(R.id.TV_HeroName);
//        TV_ReleaseYear = findViewById(R.id.TV_ReleaseYear);
//        TV_Role = findViewById(R.id.TV_Role);
//        TV_Speciality = findViewById(R.id.TV_Speciality);
//
//        TV_HP = findViewById(R.id.TV_HP);
//        TV_Mana = findViewById(R.id.TV_Mana);
//        TV_PhysicalAtk = findViewById(R.id.TV_PhysicalAtk);
//        TV_MagicRes = findViewById(R.id.TV_MagicRes);
//        TV_MoveSpeed = findViewById(R.id.TV_MoveSpeed);
//
//        RV_Skills = findViewById(R.id.RV_Skills);
//    }

//    private void setupRecycler() {
//        skillAdapter = new SkillAdapter(this, skillList);
//        RV_Skills.setLayoutManager(new LinearLayoutManager(this));
//        RV_Skills.setAdapter(skillAdapter);
//    }

    // ================= LOAD DATA =================
    private void loadHeroData() {

        String heroUrl = getIntent().getStringExtra("hero_url");
        if (heroUrl == null) return;

        String HERO_URL = BASE_HERO_URL + heroUrl;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                HERO_URL,
                null,
//                response -> {
//                    try {
//                        JSONObject hero = response.getJSONObject("hero");
//
//                        // ===== HERO INFO =====
//                        TV_HeroName.setText(hero.optString("hero_name"));
//                        TV_ReleaseYear.setText("Release: " + hero.optInt("release_year"));
//                        TV_Role.setText(joinArray(hero.optJSONArray("role")));
//                        TV_Speciality.setText(joinArray(hero.optJSONArray("speciality")));
//
//                        String heroIcon = hero.optString("hero_icon");
//                        String heroImageUrl = heroIcon.isEmpty()
//                                ? DEFAULT_HERO_ASSETS
//                                : HERO_IMAGE + heroIcon;
//
//                        Picasso.get()
//                                .load(heroImageUrl)
//                                .transform(new CircleTransform())
//                                .into(IV_Hero);
//
//                        // ===== BASE ATTRIBUTES =====
//                        JSONObject attr = hero.optJSONObject("base_attributes");
//                        if (attr != null) {
//                            TV_HP.setText("HP: " + attr.optInt("hp"));
//                            TV_Mana.setText("Mana: " + attr.optInt("mana"));
//                            TV_PhysicalAtk.setText("Physical ATK: " + attr.optInt("physical_attack"));
//                            TV_MagicRes.setText("Magic RES: " + attr.optInt("magic_resistance"));
//                            TV_MoveSpeed.setText("Move SPD: " + attr.optInt("movement_speed"));
//                        }
//
//                        // ===== SKILLS =====
//                        skillList.clear();
//                        JSONArray skills = hero.optJSONArray("skills");
//                        if (skills != null) {
//                            for (int i = 0; i < skills.length(); i++) {
//                                JSONObject s = skills.getJSONObject(i);
//
//                                skillList.add(new SkillModel(
//                                        s.optString("skill_name"),
//                                        s.optString("skill_icon"),
//                                        s.optString("type"),
//                                        s.optString("description"),
//                                        joinArrayNumber(s.optJSONArray("cooldown")),
//                                        joinArrayNumber(s.optJSONArray("manacost")),
//                                        joinArray(s.optJSONArray("skill_unique"))
//                                ));
//                            }
//                        }
//
//                        skillAdapter.notifyDataSetChanged();
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                },
                response -> {
                    try {
                        JSONObject hero = response.getJSONObject("hero");

                        // ===== HERO INFO (LOG) =====
                        String heroName = hero.optString("hero_name", "-");
                        int releaseYear = hero.optInt("release_year", 0);
                        String role = joinArray(hero.optJSONArray("role"));
                        String speciality = joinArray(hero.optJSONArray("speciality"));
                        String heroIcon = hero.optString("hero_icon", "");

                        Log.d("HERO_DETAIL", "===== HERO INFO =====");
                        Log.d("HERO_DETAIL", "Name        : " + heroName);
                        Log.d("HERO_DETAIL", "Release Year : " + releaseYear);
                        Log.d("HERO_DETAIL", "Role        : " + role);
                        Log.d("HERO_DETAIL", "Speciality  : " + speciality);
                        Log.d("HERO_DETAIL", "Hero Icon   : " + heroIcon);

                        // ===== BASE ATTRIBUTES (LOG) =====
                        JSONObject attr = hero.optJSONObject("base_attributes");
                        Log.d("HERO_DETAIL", "===== BASE ATTRIBUTES =====");
                        if (attr != null) {
                            Log.d("HERO_DETAIL", "HP              : " + attr.optInt("hp"));
                            Log.d("HERO_DETAIL", "Mana            : " + attr.optInt("mana"));
                            Log.d("HERO_DETAIL", "Physical Attack : " + attr.optInt("physical_attack"));
                            Log.d("HERO_DETAIL", "Magic Resistance: " + attr.optInt("magic_resistance"));
                            Log.d("HERO_DETAIL", "Movement Speed  : " + attr.optInt("movement_speed"));
                        } else {
                            Log.d("HERO_DETAIL", "base_attributes: -");
                        }

                        // ===== SKILLS (LOG) =====
                        JSONArray skills = hero.optJSONArray("skills");
                        Log.d("HERO_DETAIL", "===== SKILLS =====");
                        if (skills != null) {
                            for (int i = 0; i < skills.length(); i++) {
                                JSONObject s = skills.getJSONObject(i);

                                String skillName = s.optString("skill_name", "-");
                                String skillIcon = s.optString("skill_icon", "-");
                                String type = s.optString("type", "-");
                                String desc = s.optString("description", "-");
                                String cooldown = joinArrayNumber(s.optJSONArray("cooldown"));
                                String manacost = joinArrayNumber(s.optJSONArray("manacost"));
                                String unique = joinArray(s.optJSONArray("skill_unique"));

                                Log.d("HERO_DETAIL", "Skill #" + (i + 1));
                                Log.d("HERO_DETAIL", "  Name     : " + skillName);
                                Log.d("HERO_DETAIL", "  Icon     : " + skillIcon);
                                Log.d("HERO_DETAIL", "  Type     : " + type);
                                Log.d("HERO_DETAIL", "  Desc     : " + desc);
                                Log.d("HERO_DETAIL", "  Cooldown : " + cooldown);
                                Log.d("HERO_DETAIL", "  Mana     : " + manacost);
                                Log.d("HERO_DETAIL", "  Unique   : " + unique);
                            }
                        } else {
                            Log.d("HERO_DETAIL", "skills: -");
                        }

                        // (Opsional) log response mentah (JSON full)
                        // Log.d("HERO_DETAIL_RAW", response.toString());

                    } catch (Exception e) {
                        Log.e("HERO_DETAIL", "Parse error: " + e.getMessage(), e);
                    }
                },

                error -> error.printStackTrace()
        );

        Volley.newRequestQueue(this).add(request);
    }

    // ================= HELPER =================
    private String joinArray(JSONArray arr) {
        if (arr == null) return "-";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length(); i++) {
            sb.append(arr.optString(i));
            if (i < arr.length() - 1) sb.append(", ");
        }
        return sb.toString();
    }

    private String joinArrayNumber(JSONArray arr) {
        if (arr == null) return "-";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length(); i++) {
            sb.append(arr.optInt(i));
            if (i < arr.length() - 1) sb.append(", ");
        }
        return sb.toString();
    }

    // ================= CIRCLE IMAGE =================
    static class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
            if (squared != source) source.recycle();

            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setAntiAlias(true);

            BitmapShader shader = new BitmapShader(squared, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squared.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}
