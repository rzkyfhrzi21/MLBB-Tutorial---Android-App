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

import com.example.mlbbtutorial.model.ListItemModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailItemDialog extends DialogFragment {

    private static final String BASE_REPO =
            "https://raw.githubusercontent.com/rzkyfhrzi21/mlbb-tutorial-api/refs/heads/master/assets/items/";

    private static final String DEFAULT_ICON =
            "https://raw.githubusercontent.com/rzkyfhrzi21/mlbb-tutorial-api/refs/heads/master/assets/items/default.webp";

    /* =========================
       NEW INSTANCE (WAJIB)
       ========================= */
    public static DetailItemDialog newInstance(ListItemModel m) {
        DetailItemDialog d = new DetailItemDialog();
        Bundle b = new Bundle();

        b.putString("name", m.name);
        b.putString("icon", m.icon);

        b.putInt("price", m.price != null ? m.price : 0);
        b.putBoolean("has_sell", m.sell != null);
        if (m.sell != null) b.putInt("sell", m.sell);

        b.putStringArrayList("attributes", m.attributes);

        // UNIQUE PASSIVE
        ArrayList<String> up = new ArrayList<>();
        for (ListItemModel.Effect e : m.uniquePassive) {
            up.add(e.name + "||" + e.desc);
        }
        b.putStringArrayList("unique_passive", up);

        // UNIQUE ACTIVE
        ArrayList<String> ua = new ArrayList<>();
        for (ListItemModel.Effect e : m.uniqueActive) {
            ua.add(e.name + "||" + e.desc);
        }
        b.putStringArrayList("unique_active", ua);

        d.setArguments(b);
        return d;
    }

    /* ========================= */

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            Window w = getDialog().getWindow();
            w.setBackgroundDrawableResource(android.R.color.transparent);
            w.setWindowAnimations(R.style.DialogAnimation);

            int width = (int) (requireContext()
                    .getResources().getDisplayMetrics().widthPixels * 0.90);
            w.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull android.view.LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        Bundle a = getArguments();
        if (a == null) return null;

        String name = a.getString("name", "");
        String iconPath = a.getString("icon", "");

        ScrollView scroll = new ScrollView(requireContext());
        scroll.setFillViewport(true);

        LinearLayout card = new LinearLayout(requireContext());
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(20), dp(20), dp(20), dp(20));
        scroll.addView(card);

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(Color.parseColor("#0F0F0F"));
        bg.setCornerRadius(dp(22));
        card.setBackground(bg);

        /* ===== HEADER CLOSE ===== */
        FrameLayout header = new FrameLayout(requireContext());
        ImageView btnClose = new ImageView(requireContext());
        btnClose.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        btnClose.setColorFilter(Color.WHITE);

        FrameLayout.LayoutParams cp =
                new FrameLayout.LayoutParams(dp(24), dp(24));
        cp.gravity = Gravity.END;
        btnClose.setLayoutParams(cp);
        btnClose.setOnClickListener(v -> dismiss());

        header.addView(btnClose);
        card.addView(header);

        /* ===== ICON ===== */
        ImageView icon = new ImageView(requireContext());
        LinearLayout.LayoutParams ip =
                new LinearLayout.LayoutParams(dp(110), dp(110));
        ip.gravity = Gravity.CENTER_HORIZONTAL;
        icon.setLayoutParams(ip);

        String iconUrl =
                (iconPath != null && !iconPath.trim().isEmpty())
                        ? BASE_REPO + iconPath
                        : DEFAULT_ICON;

        Picasso.get().load(iconUrl).into(icon, new Callback() {
            @Override public void onSuccess() {}
            @Override public void onError(Exception e) {
                Picasso.get().load(DEFAULT_ICON).into(icon);
            }
        });

        /* ===== TITLE ===== */
        TextView title = new TextView(requireContext());
        title.setText(name);
        title.setTextColor(Color.parseColor("#64FFDA"));
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        title.setGravity(Gravity.CENTER);

        card.addView(icon);
        card.addView(space(12));
        card.addView(title);
        card.addView(space(16));

        /* ===== PRICE ===== */
        card.addView(info("Harga", String.valueOf(a.getInt("price", 0))));
        if (a.getBoolean("has_sell", false)) {
            card.addView(info("Jual", String.valueOf(a.getInt("sell", 0))));
        }

        /* ===== ATTRIBUTES ===== */
        card.addView(space(10));
        card.addView(label("Attribute"));

        ArrayList<String> attrs = a.getStringArrayList("attributes");
        if (attrs != null && !attrs.isEmpty()) {
            for (String s : attrs) card.addView(bullet(s));
        } else {
            card.addView(text("Tidak ada attribute."));
        }

        /* ===== UNIQUE PASSIVE ===== */
        ArrayList<String> up = a.getStringArrayList("unique_passive");
        if (up != null && !up.isEmpty()) {
            card.addView(space(14));
            card.addView(label("Unique Passive"));
            for (String s : up) {
                String[] p = s.split("\\|\\|");
                card.addView(info(p[0], p[1]));
            }
        }

        /* ===== UNIQUE ACTIVE ===== */
        ArrayList<String> ua = a.getStringArrayList("unique_active");
        if (ua != null && !ua.isEmpty()) {
            card.addView(space(14));
            card.addView(label("Unique Active"));
            for (String s : ua) {
                String[] p = s.split("\\|\\|");
                card.addView(info(p[0], p[1]));
            }
        }

        return scroll;
    }

    /* ========================= UTIL ========================= */

    private TextView label(String t) {
        TextView tv = new TextView(requireContext());
        tv.setText(t);
        tv.setTextColor(Color.parseColor("#64FFDA"));
        tv.setTextSize(14);
        tv.setTypeface(null, android.graphics.Typeface.BOLD);
        return tv;
    }

    private TextView text(String t) {
        TextView tv = new TextView(requireContext());
        tv.setText(t);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(13);
        return tv;
    }

    private LinearLayout info(String k, String v) {
        LinearLayout box = new LinearLayout(requireContext());
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(0, dp(6), 0, dp(6));

        TextView kk = new TextView(requireContext());
        kk.setText(k);
        kk.setTextColor(Color.parseColor("#64FFDA"));
        kk.setTextSize(14);

        TextView vv = new TextView(requireContext());
        vv.setText(v);
        vv.setTextColor(Color.WHITE);
        vv.setTextSize(13);

        box.addView(kk);
        box.addView(vv);
        return box;
    }

    private TextView bullet(String t) {
        TextView tv = new TextView(requireContext());
        tv.setText("• " + t);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(13);
        tv.setPadding(0, dp(4), 0, dp(4));
        return tv;
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
}
