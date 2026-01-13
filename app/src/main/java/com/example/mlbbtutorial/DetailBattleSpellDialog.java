package com.example.mlbbtutorial;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
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

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class DetailBattleSpellDialog extends DialogFragment {

    private static final String ARG_NAME = "name";
    private static final String ARG_USAGE = "usage";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_COOLDOWN = "cooldown";
    private static final String ARG_UNLOCK = "unlock";

    private static final String BASE_ICON_URL =
            "https://raw.githubusercontent.com/rzkyfhrzi21/mlbb-tutorial-api/refs/heads/master/assets/battle_spells/";
    private static final String DEFAULT_ICON =
            BASE_ICON_URL + "default.webp";

    public static DetailBattleSpellDialog newInstance(
            String name,
            String usage,
            String description,
            int cooldown,
            int unlockedAt
    ) {
        DetailBattleSpellDialog dialog = new DetailBattleSpellDialog();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_USAGE, usage);
        args.putString(ARG_DESCRIPTION, description);
        args.putInt(ARG_COOLDOWN, cooldown);
        args.putInt(ARG_UNLOCK, unlockedAt);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() != null && getDialog().getWindow() != null) {
            Window window = getDialog().getWindow();
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setWindowAnimations(R.style.DialogAnimation);

            int width = (int) (requireContext().getResources()
                    .getDisplayMetrics().widthPixels * 0.90);

            window.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        String name = getArguments().getString(ARG_NAME, "");
        String usage = getArguments().getString(ARG_USAGE, "");
        String description = getArguments().getString(ARG_DESCRIPTION, "");
        int cooldown = getArguments().getInt(ARG_COOLDOWN, 0);
        int unlock = getArguments().getInt(ARG_UNLOCK, 0);

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

        // HEADER (CLOSE BUTTON)
        FrameLayout header = new FrameLayout(requireContext());
        header.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        ImageView btnClose = new ImageView(requireContext());
        btnClose.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        btnClose.setColorFilter(Color.WHITE);

        FrameLayout.LayoutParams closeParams =
                new FrameLayout.LayoutParams(dp(24), dp(24));
        closeParams.gravity = Gravity.END | Gravity.TOP;
        btnClose.setLayoutParams(closeParams);

        btnClose.setOnClickListener(v -> dismiss());

        header.addView(btnClose);
        card.addView(header);

        // ICON
        FrameLayout frame = new FrameLayout(requireContext());

        LinearLayout.LayoutParams frameParams =
                new LinearLayout.LayoutParams(dp(110), dp(110));
        frameParams.gravity = Gravity.CENTER_HORIZONTAL;

        frame.setLayoutParams(frameParams);


        // HANYA BAGIAN ICON YANG DITUNJUKKAN (LAINNYA TETAP SAMA)
        ImageView icon = new ImageView(requireContext());
        FrameLayout.LayoutParams iconParams =
                new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
        iconParams.gravity = Gravity.CENTER;
        icon.setLayoutParams(iconParams);
        icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        frame.addView(icon);


        String iconName = name.toLowerCase().replace(" ", "_");
        String iconUrl = BASE_ICON_URL + iconName + ".webp";

        Picasso.get()
                .load(iconUrl)
                .into(icon, new Callback() {
                    @Override
                    public void onSuccess() {}

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(DEFAULT_ICON).into(icon);
                    }
                });

        card.addView(frame);
        card.addView(space(12));

        // TITLE
        card.addView(title(name));
        card.addView(space(18));

        // CONTENT
        card.addView(info("Kegunaan", usage));
        card.addView(info("Deskripsi", description));
        card.addView(info("Cooldown", cooldown + " detik"));
        card.addView(info("Terbuka di Level", "Level " + unlock));

        return scroll;
    }

    private TextView title(String text) {
        TextView tv = new TextView(requireContext());
        tv.setText(text);
        tv.setTextColor(Color.parseColor("#64FFDA"));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(null, android.graphics.Typeface.BOLD);
        return tv;
    }

    private LinearLayout info(String label, String value) {
        LinearLayout box = new LinearLayout(requireContext());
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(0, dp(10), 0, dp(10));

        TextView lbl = new TextView(requireContext());
        lbl.setText(label);
        lbl.setTextColor(Color.parseColor("#64FFDA"));
        lbl.setTextSize(14);

        TextView val = new TextView(requireContext());
        val.setText(value);
        val.setTextColor(Color.WHITE);
        val.setTextSize(13);

        box.addView(lbl);
        box.addView(val);
        return box;
    }

    private View space(int dp) {
        View v = new View(requireContext());
        v.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(dp)
        ));
        return v;
    }

    private int dp(int value) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value,
                requireContext().getResources().getDisplayMetrics()
        );
    }
}
