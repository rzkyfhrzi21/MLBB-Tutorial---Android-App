package com.example.mlbbtutorial.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mlbbtutorial.DetailEmblemDialog;
import com.example.mlbbtutorial.model.ListEmblemModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ListEmblemSectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static class Row {
        public final int type; // 0 header, 1 item
        public final String title;
        public final ListEmblemModel emblem;

        private Row(int type, String title, ListEmblemModel emblem) {
            this.type = type;
            this.title = title;
            this.emblem = emblem;
        }

        public static Row header(String title) {
            return new Row(0, title, null);
        }

        public static Row item(ListEmblemModel emblem) {
            return new Row(1, null, emblem);
        }
    }

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private final Context context;
    private final List<Row> rows;

    // ICON BASE dari path JSON: "/assets/emblems/...."
    private static final String BASE_REPO =
            "https://raw.githubusercontent.com/rzkyfhrzi21/mlbb-tutorial-api/refs/heads/master";

    private static final String DEFAULT_ICON =
            "https://raw.githubusercontent.com/rzkyfhrzi21/mlbb-tutorial-api/refs/heads/master/assets/items/default.webp";

    public ListEmblemSectionAdapter(Context context, List<Row> rows) {
        this.context = context;
        this.rows = rows;
    }

    @Override
    public int getItemViewType(int position) {
        return rows.get(position).type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEADER) {
            TextView tv = new TextView(context);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            lp.setMargins(dp(6), dp(10), dp(6), dp(4));
            tv.setLayoutParams(lp);
            tv.setTextColor(Color.parseColor("#64FFDA"));
            tv.setTextSize(14);
            tv.setTypeface(tv.getTypeface(), android.graphics.Typeface.BOLD);
            tv.setPadding(dp(6), dp(6), dp(6), dp(6));
            return new HeaderVH(tv);
        }

        // ITEM
        CardView card = new CardView(context);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(dp(6), dp(6), dp(6), dp(6));
        card.setLayoutParams(params);
        card.setRadius(dp(12));
        card.setCardBackgroundColor(Color.parseColor("#121212"));
        card.setCardElevation(dp(2));

        LinearLayout row = new LinearLayout(context);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(dp(12), dp(10), dp(12), dp(10));
        row.setGravity(Gravity.CENTER_VERTICAL);
        card.addView(row);

        ImageView icon = new ImageView(context);
        LinearLayout.LayoutParams ip = new LinearLayout.LayoutParams(dp(44), dp(44));
        icon.setLayoutParams(ip);
        icon.setScaleType(ImageView.ScaleType.CENTER_CROP);

        LinearLayout textBox = new LinearLayout(context);
        textBox.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
        );
        tp.leftMargin = dp(12);
        textBox.setLayoutParams(tp);

        TextView txtName = new TextView(context);
        txtName.setTextColor(Color.WHITE);
        txtName.setTextSize(14);

        TextView txtSub = new TextView(context);
        txtSub.setTextColor(Color.parseColor("#B0B0B0"));
        txtSub.setTextSize(12);

        textBox.addView(txtName);
        textBox.addView(txtSub);

        // BADGE SECTION (kanan)
        TextView badge = new TextView(context);
        FrameLayout.LayoutParams bp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        badge.setLayoutParams(bp);
        badge.setTextColor(Color.parseColor("#0B0B0B"));
        badge.setTextSize(11);
        badge.setPadding(dp(10), dp(4), dp(10), dp(4));

        GradientDrawable bg = new GradientDrawable();
        bg.setCornerRadius(dp(999));
        bg.setColor(Color.parseColor("#64FFDA"));
        badge.setBackground(bg);

        row.addView(icon);
        row.addView(textBox);
        row.addView(badge);

        return new ItemVH(card, icon, txtName, txtSub, badge);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Row r = rows.get(position);

        if (holder instanceof HeaderVH) {
            ((HeaderVH) holder).title.setText(r.title);
            return;
        }

        ItemVH h = (ItemVH) holder;
        ListEmblemModel e = r.emblem;

        h.txtName.setText(e.getName());

        // subtext + badge
        if ("main".equals(e.getKind())) {
            ArrayList<String> attrs = e.getAttributes();
            String sub = (attrs != null && attrs.size() > 0)
                    ? attrs.get(0) + (attrs.size() > 1 ? " • " + attrs.get(1) : "")
                    : "Main Emblem";
            h.txtSub.setText(sub);

            h.badge.setVisibility(View.GONE);
        } else {
            String sub = "Section " + e.getSection();
            if (e.getBenefits() != null && !e.getBenefits().trim().isEmpty()) {
                sub = sub + " • " + e.getBenefits();
            }
            h.txtSub.setText(sub);

            // Badge S1 / S2 / S3
            int s = e.getSection();
            h.badge.setVisibility(View.VISIBLE);
            h.badge.setText("S" + (s > 0 ? s : 1));
        }

        // icon dari path JSON
        String iconUrl = (e.getIconPath() != null && !e.getIconPath().trim().isEmpty())
                ? BASE_REPO + e.getIconPath()
                : DEFAULT_ICON;

        Picasso.get()
                .load(iconUrl)
                .into(h.icon, new Callback() {
                    @Override public void onSuccess() {}
                    @Override public void onError(Exception ex) {
                        Picasso.get().load(DEFAULT_ICON).into(h.icon);
                    }
                });

        h.itemView.setOnClickListener(v -> {
            DetailEmblemDialog dialog = DetailEmblemDialog.newInstance(e);
            dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "detail_emblem");
        });
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    static class HeaderVH extends RecyclerView.ViewHolder {
        TextView title;
        HeaderVH(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView;
        }
    }

    static class ItemVH extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView txtName, txtSub, badge;

        ItemVH(@NonNull View itemView, ImageView icon, TextView txtName, TextView txtSub, TextView badge) {
            super(itemView);
            this.icon = icon;
            this.txtName = txtName;
            this.txtSub = txtSub;
            this.badge = badge;
        }
    }

    private int dp(int v) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                v,
                context.getResources().getDisplayMetrics()
        );
    }
}
