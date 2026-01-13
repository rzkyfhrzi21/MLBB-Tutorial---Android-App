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

import com.example.mlbbtutorial.model.ListEmblemModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailEmblemDialog extends DialogFragment {

    private static final String BASE_REPO =
            "https://raw.githubusercontent.com/rzkyfhrzi21/mlbb-tutorial-api/refs/heads/master";
    private static final String DEFAULT_ICON =
            "https://raw.githubusercontent.com/rzkyfhrzi21/mlbb-tutorial-api/refs/heads/master/assets/items/default.webp";

    public static DetailEmblemDialog newInstance(ListEmblemModel e) {
        DetailEmblemDialog d = new DetailEmblemDialog();
        Bundle b = new Bundle();

        b.putString("kind", e.getKind());
        b.putString("name", e.getName());
        b.putString("icon", e.getIconPath());

        if ("main".equals(e.getKind())) {
            b.putStringArrayList("attributes", e.getAttributes());
        } else {
            b.putInt("section", e.getSection());
            b.putString("benefits", e.getBenefits());
            b.putString("desc", e.getDesc());
            if (e.getCd() != null) b.putInt("cd", e.getCd());
            b.putBoolean("has_cd", e.getCd() != null);
        }

        d.setArguments(b);
        return d;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            Window w = getDialog().getWindow();
            w.setBackgroundDrawableResource(android.R.color.transparent);
            w.setWindowAnimations(R.style.DialogAnimation);

            int width = (int) (requireContext().getResources()
                    .getDisplayMetrics().widthPixels * 0.90);
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

        String kind = getArguments().getString("kind", "");
        String name = getArguments().getString("name", "");
        String iconPath = getArguments().getString("icon", "");

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

        // HEADER (CLOSE)
        FrameLayout header = new FrameLayout(requireContext());
        header.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        ImageView btnClose = new ImageView(requireContext());
        btnClose.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        btnClose.setColorFilter(Color.WHITE);
        FrameLayout.LayoutParams cp = new FrameLayout.LayoutParams(dp(24), dp(24));
        cp.gravity = Gravity.END | Gravity.TOP;
        btnClose.setLayoutParams(cp);
        btnClose.setOnClickListener(v -> dismiss());

        header.addView(btnClose);
        card.addView(header);

        // ICON
        ImageView icon = new ImageView(requireContext());
        FrameLayout.LayoutParams iconParams =
                new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
        iconParams.gravity = Gravity.CENTER;
        icon.setLayoutParams(iconParams);
        icon.setScaleType(ImageView.ScaleType.CENTER_CROP);

        String iconUrl = (iconPath != null && !iconPath.trim().isEmpty())
                ? BASE_REPO + iconPath
                : DEFAULT_ICON;

        Picasso.get()
                .load(iconUrl)
                .into(icon, new Callback() {
                    @Override public void onSuccess() {}
                    @Override public void onError(Exception e) {
                        Picasso.get().load(DEFAULT_ICON).into(icon);
                    }
                });

        // TITLE
        TextView title = new TextView(requireContext());
        title.setText(name);
        title.setTextColor(Color.parseColor("#64FFDA"));
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView subtitle = new TextView(requireContext());
        subtitle.setText("main".equals(kind) ? "Main Emblem" : "Ability Emblem");
        subtitle.setTextColor(Color.parseColor("#B0B0B0"));
        subtitle.setGravity(Gravity.CENTER);

        card.addView(icon);
        card.addView(space(10));
        card.addView(title);
        card.addView(subtitle);
        card.addView(space(16));

        // CONTENT
        if ("main".equals(kind)) {
            ArrayList<String> attrs = getArguments().getStringArrayList("attributes");
            card.addView(label("Atribut"));
            if (attrs != null && attrs.size() > 0) {
                for (String a : attrs) {
                    card.addView(bullet(a));
                }
            } else {
                card.addView(text("Tidak ada atribut."));
            }
        } else {
            int section = getArguments().getInt("section", 1);
            String benefits = getArguments().getString("benefits", "");
            String desc = getArguments().getString("desc", null);
            boolean hasCd = getArguments().getBoolean("has_cd", false);
            int cd = getArguments().getInt("cd", 0);

            card.addView(info("Section", String.valueOf(section)));
            card.addView(info("Benefit", benefits));

            if (desc != null && !desc.trim().isEmpty() && !"null".equalsIgnoreCase(desc.trim())) {
                card.addView(info("Deskripsi", desc));
            }

            if (hasCd) {
                card.addView(info("Cooldown", cd + " detik"));
            }
        }

        return scroll;
    }

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
        box.setPadding(0, dp(8), 0, dp(8));

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
        tv.setPadding(0, dp(6), 0, dp(6));
        return tv;
    }

    private View space(int dp) {
        View v = new View(requireContext());
        v.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(dp)
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
