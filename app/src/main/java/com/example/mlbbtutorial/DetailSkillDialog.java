package com.example.mlbbtutorial;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mlbbtutorial.model.HeroSkillModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class DetailSkillDialog extends DialogFragment {

    private static final String DEFAULT_SKILL_ICON =
            "https://raw.githubusercontent.com/rzkyfhrzi21/mlbb-tutorial-api/refs/heads/master/assets/skills/default.webp";

    public static DetailSkillDialog newInstance(HeroSkillModel s) {
        DetailSkillDialog d = new DetailSkillDialog();
        Bundle b = new Bundle();

        b.putString("name", s.getName());
        b.putString("icon", s.getIconUrl());
        b.putString("type", s.getType());
        b.putString("desc", s.getDescription());
        b.putStringArrayList("unique", s.getUniques());
        b.putIntegerArrayList("cooldown", s.getCooldown());
        b.putIntegerArrayList("mana", s.getManaCost());

        d.setArguments(b);
        return d;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window w = getDialog().getWindow();
        if (w != null) {
            w.setBackgroundDrawableResource(android.R.color.transparent);
            w.setLayout(
                    (int) (requireContext().getResources().getDisplayMetrics().widthPixels * 0.9),
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull android.view.LayoutInflater i,
            @Nullable ViewGroup c,
            @Nullable Bundle s
    ) {

        Bundle a = getArguments();

        ScrollView scroll = new ScrollView(requireContext());
        scroll.setFillViewport(true);

        LinearLayout card = new LinearLayout(requireContext());
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(20), dp(20), dp(20), dp(20));

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(Color.parseColor("#0B0B0B"));
        bg.setCornerRadius(dp(18));
        card.setBackground(bg);

        scroll.addView(card);

        /* ================= HEADER + CLOSE ================= */
        FrameLayout header = new FrameLayout(requireContext());
        header.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        ImageView btnClose = new ImageView(requireContext());
        btnClose.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        btnClose.setColorFilter(Color.WHITE);

        FrameLayout.LayoutParams closeParam =
                new FrameLayout.LayoutParams(dp(24), dp(24));
        closeParam.gravity = Gravity.END | Gravity.TOP;
        btnClose.setLayoutParams(closeParam);

        btnClose.setOnClickListener(v -> dismiss());

        header.addView(btnClose);
        card.addView(header);

        /* ================= ICON ================= */
        ImageView icon = new ImageView(requireContext());
        LinearLayout.LayoutParams ip =
                new LinearLayout.LayoutParams(dp(96), dp(96));
        ip.gravity = Gravity.CENTER_HORIZONTAL;
        icon.setLayoutParams(ip);
        icon.setScaleType(ImageView.ScaleType.CENTER_CROP);

        String iconUrl = a.getString("icon");
        if (iconUrl == null || iconUrl.trim().isEmpty()) {
            iconUrl = DEFAULT_SKILL_ICON;
        }

        Picasso.get()
                .load(iconUrl)
                .into(icon, new Callback() {
                    @Override public void onSuccess() {}
                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(DEFAULT_SKILL_ICON).into(icon);
                    }
                });

        card.addView(icon);
        card.addView(space(12));

        /* ================= TITLE ================= */
        card.addView(title(toTitleCase(a.getString("name"))));
        card.addView(space(4));
        card.addView(text("Type: " + toTitleCase(a.getString("type"))));

        card.addView(space(12));
        card.addView(text(a.getString("desc")));

        showIntList(card, "Cooldown", a.getIntegerArrayList("cooldown"), " detik");
        showIntList(card, "Mana Cost", a.getIntegerArrayList("mana"), "");

        ArrayList<String> u = a.getStringArrayList("unique");
        if (u != null && !u.isEmpty()) {
            card.addView(space(10));
            card.addView(label("Skill Unique"));
            for (String x : u) card.addView(bullet(x));
        }

        return scroll;
    }

    /* ================= UTIL ================= */

    private void showIntList(LinearLayout c, String l, ArrayList<Integer> d, String s) {
        if (d == null || d.isEmpty()) return;
        c.addView(space(8));
        c.addView(label(l));
        c.addView(text(join(d) + s));
    }

    private String join(ArrayList<Integer> l) {
        ArrayList<String> s = new ArrayList<>();
        for (Integer i : l) s.add(String.valueOf(i));
        return android.text.TextUtils.join(" / ", s);
    }

    private TextView title(String t) {
        TextView v = text(t);
        v.setTextColor(Color.parseColor("#64FFDA"));
        v.setTextSize(18);
        v.setGravity(Gravity.CENTER);
        v.setTypeface(null, android.graphics.Typeface.BOLD);
        return v;
    }

    private TextView label(String t) {
        TextView v = text(t);
        v.setTextColor(Color.parseColor("#64FFDA"));
        v.setTypeface(null, android.graphics.Typeface.BOLD);
        return v;
    }

    private TextView text(String t) {
        TextView v = new TextView(requireContext());
        v.setText(t == null ? "-" : t);
        v.setTextColor(Color.WHITE);
        v.setTextSize(13);
        return v;
    }

    private TextView bullet(String t) {
        return text("• " + t);
    }

    private View space(int d) {
        View v = new View(requireContext());
        v.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(d)
        ));
        return v;
    }

    private int dp(int v) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                v,
                requireContext().getResources().getDisplayMetrics()
        );
    }

    private String toTitleCase(String s) {
        if (s == null || s.isEmpty()) return "-";
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
