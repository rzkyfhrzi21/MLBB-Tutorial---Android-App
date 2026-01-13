package com.example.mlbbtutorial.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mlbbtutorial.DetailSkillDialog;
import com.example.mlbbtutorial.model.HeroSkillModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class HeroSkillAdapter
        extends RecyclerView.Adapter<HeroSkillAdapter.Holder> {

    private final Context ctx;
    private final ArrayList<HeroSkillModel> data;

    // DEFAULT ICON (AMAN)
    private static final String DEFAULT_SKILL =
            "https://raw.githubusercontent.com/rzkyfhrzi21/mlbb-tutorial-api/refs/heads/master/assets/skills/default.webp";

    public HeroSkillAdapter(Context ctx, ArrayList<HeroSkillModel> data) {
        this.ctx = ctx;
        this.data = data;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        /* ================= ROOT ================= */
        LinearLayout root = new LinearLayout(ctx);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setPadding(dp(12), dp(12), dp(12), dp(12));

        RecyclerView.LayoutParams rp = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        rp.setMargins(dp(6), dp(6), dp(6), dp(6));
        root.setLayoutParams(rp);

        /* ================= ICON ================= */
        ImageView icon = new ImageView(ctx);

        LinearLayout.LayoutParams ip =
                new LinearLayout.LayoutParams(dp(56), dp(56));
        ip.gravity = Gravity.CENTER_HORIZONTAL;
        icon.setLayoutParams(ip);

        // ⚠️ PENTING UNTUK ICON TRANSPARAN
        icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        icon.setBackgroundColor(Color.TRANSPARENT);

        /* ================= NAME ================= */
        TextView name = new TextView(ctx);
        name.setTextColor(Color.WHITE);
        name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        name.setGravity(Gravity.CENTER);
        name.setPadding(0, dp(6), 0, 0);

        root.addView(icon);
        root.addView(name);

        return new Holder(root, icon, name);
    }

    @Override
    public void onBindViewHolder(Holder h, int pos) {

        HeroSkillModel s = data.get(pos);

        // ===== NAME (TITLE CASE) =====
        h.name.setText(toTitleCase(s.getName()));

        // ===== ICON URL =====
        String iconUrl = s.getIconUrl();
        if (iconUrl == null || iconUrl.trim().isEmpty()) {
            iconUrl = DEFAULT_SKILL;
        }

        // ===== LOAD ICON (AMAN & KELIHATAN) =====
        Picasso.get()
                .load(iconUrl)
                .resize(dp(56), dp(56))     // WAJIB
                .centerInside()             // WAJIB
                .into(h.icon, new Callback() {
                    @Override public void onSuccess() {}

                    @Override
                    public void onError(Exception e) {
                        Picasso.get()
                                .load(DEFAULT_SKILL)
                                .resize(dp(56), dp(56))
                                .centerInside()
                                .into(h.icon);
                    }
                });

        // ===== CLICK → DETAIL DIALOG =====
        h.itemView.setOnClickListener(v ->
                DetailSkillDialog
                        .newInstance(s)
                        .show(
                                ((AppCompatActivity) ctx)
                                        .getSupportFragmentManager(),
                                "skill_detail"
                        )
        );
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /* ================= HOLDER ================= */

    static class Holder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;

        Holder(View v, ImageView i, TextView n) {
            super(v);
            icon = i;
            name = n;
        }
    }

    /* ================= UTIL ================= */

    private int dp(int v) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                v,
                ctx.getResources().getDisplayMetrics()
        );
    }

    private String toTitleCase(String s) {
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