package com.example.mlbbtutorial.adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class HeroAttributeAdapter extends RecyclerView.Adapter<HeroAttributeAdapter.VH> {

    private final ArrayList<String> keys = new ArrayList<>();
    private final ArrayList<String> values = new ArrayList<>();

    public HeroAttributeAdapter(JSONObject obj) {
        Iterator<String> it = obj.keys();
        while (it.hasNext()) {
            String k = it.next();
            keys.add(prettyKey(k));
            values.add(String.valueOf(obj.opt(k)));
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {

        LinearLayout box = new LinearLayout(p.getContext());
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(p, 12), dp(p, 10), dp(p, 12), dp(p, 10));
        box.setBackgroundColor(Color.parseColor("#121212"));

        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        lp.setMargins(dp(p, 6), dp(p, 6), dp(p, 6), dp(p, 6));
        box.setLayoutParams(lp);

        TextView k = new TextView(p.getContext());
        k.setTextColor(Color.WHITE);
        k.setTextSize(13);
        k.setTypeface(null, Typeface.BOLD);

        TextView val = new TextView(p.getContext());
        val.setTextColor(Color.parseColor("#64FFDA"));
        val.setTextSize(14);

        box.addView(k);
        box.addView(val);

        return new VH(box, k, val);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int i) {
        h.key.setText(keys.get(i));
        h.val.setText(values.get(i));
    }

    @Override
    public int getItemCount() {
        return keys.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView key, val;
        VH(@NonNull View v, TextView k, TextView va) {
            super(v);
            key = k;
            val = va;
        }
    }

    private String prettyKey(String raw) {
        // "movement_speed" -> "Movement Speed"
        String s = raw.replace("_", " ").trim().toLowerCase();
        String[] parts = s.split("\\s+");
        StringBuilder out = new StringBuilder();
        for (String p : parts) {
            if (p.length() == 0) continue;
            out.append(Character.toUpperCase(p.charAt(0)))
                    .append(p.substring(1))
                    .append(" ");
        }
        return out.toString().trim();
    }

    private int dp(ViewGroup p, int v) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                v,
                p.getResources().getDisplayMetrics()
        );
    }
}
