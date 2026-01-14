package com.example.mlbbtutorial.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mlbbtutorial.DetailItemDialog;
import com.example.mlbbtutorial.R;
import com.example.mlbbtutorial.model.ListItemModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListItemAdapter
        extends RecyclerView.Adapter<ListItemAdapter.Holder> {

    private static final String TAG = "ITEM_ADAPTER";

    private final Context ctx;
    private final ArrayList<ListItemModel> data;

    private static final String BASE_REPO =
            "https://raw.githubusercontent.com/rzkyfhrzi21/mlbb-tutorial-api/refs/heads/master";

    private static final String DEFAULT_ICON =
            "https://raw.githubusercontent.com/rzkyfhrzi21/mlbb-tutorial-api/refs/heads/master/assets/items/default.webp";

    public ListItemAdapter(Context ctx, ArrayList<ListItemModel> data) {
        this.ctx = ctx;
        this.data = data;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        LinearLayout root = new LinearLayout(ctx);
        root.setOrientation(LinearLayout.HORIZONTAL);
        root.setPadding(dp(12), dp(12), dp(12), dp(12));
        root.setGravity(Gravity.CENTER_VERTICAL);

        RecyclerView.LayoutParams rp = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        rp.setMargins(dp(8), dp(6), dp(8), dp(6));
        root.setLayoutParams(rp);

        ImageView icon = new ImageView(ctx);
        icon.setLayoutParams(new LinearLayout.LayoutParams(dp(52), dp(52)));
        icon.setScaleType(ImageView.ScaleType.CENTER_CROP);

        LinearLayout textBox = new LinearLayout(ctx);
        textBox.setOrientation(LinearLayout.VERTICAL);
        textBox.setPadding(dp(12), 0, 0, 0);

        TextView name = new TextView(ctx);
        name.setTextColor(Color.WHITE);
        name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        name.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView attr = new TextView(ctx);
        attr.setTextColor(Color.parseColor("#B0B0B0"));
        attr.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);

        textBox.addView(name);
        textBox.addView(attr);

        root.addView(icon);
        root.addView(textBox);

        return new Holder(root, icon, name, attr);
    }

    @Override
    public void onBindViewHolder(Holder h, int pos) {

        ListItemModel m = data.get(pos);

        h.name.setText(m.name);

        // ATTRIBUTE
        if (m.attributes != null && !m.attributes.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String s : m.attributes) {
                sb.append("• ").append(s).append("\n");
            }
            h.attr.setText(sb.toString().trim());
        } else {
            h.attr.setText("-");
        }

        // ===============================
        // ICON URL FINAL + DEBUG LOG
        // ===============================
        String iconUrl;

        if (m.icon != null && !m.icon.trim().isEmpty()) {
            iconUrl = BASE_REPO + "/assets/items/" + m.icon.trim();
        } else {
            iconUrl = DEFAULT_ICON;
        }

        Log.d(TAG, "------------------------------");
        Log.d(TAG, "Item Name : " + m.name);
        Log.d(TAG, "Icon JSON : " + m.icon);
        Log.d(TAG, "Final URL : " + iconUrl);

        Picasso.get()
                .load(iconUrl)
                .into(h.icon, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "ICON LOAD SUCCESS : " + m.name);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "ICON LOAD FAILED : " + m.name);
                        Log.e(TAG, "REASON : " + e.getMessage());

                        Picasso.get().
                                load(R.drawable.default_icon).
                                into(h.icon);


                        Log.d(TAG, "FALLBACK DEFAULT ICON USED");
                    }
                });

        // CLICK → DETAIL
        h.itemView.setOnClickListener(v ->
                DetailItemDialog
                        .newInstance(m)
                        .show(
                                ((AppCompatActivity) ctx)
                                        .getSupportFragmentManager(),
                                "item_detail"
                        )
        );
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name, attr;

        Holder(View v, ImageView i, TextView n, TextView a) {
            super(v);
            icon = i;
            name = n;
            attr = a;
        }
    }

    private int dp(int v) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                v,
                ctx.getResources().getDisplayMetrics()
        );
    }
}
