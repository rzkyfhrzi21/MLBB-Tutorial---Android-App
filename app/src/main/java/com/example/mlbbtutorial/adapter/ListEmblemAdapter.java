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

public class ListEmblemAdapter extends RecyclerView.Adapter<ListEmblemAdapter.EmblemViewHolder> {

    private final Context context;
    private final List<ListEmblemModel> list;

    // ICON BASE dari path JSON: "/assets/emblems/...."
    private static final String BASE_REPO =
            "https://raw.githubusercontent.com/rzkyfhrzi21/mlbb-tutorial-api/refs/heads/master";

    // DEFAULT ICON (sesuai request user)
    private static final String DEFAULT_ICON =
            "https://raw.githubusercontent.com/rzkyfhrzi21/mlbb-tutorial-api/refs/heads/master/assets/items/default.webp";

    public ListEmblemAdapter(Context context, List<ListEmblemModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public EmblemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

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
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
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

        row.addView(icon);
        row.addView(textBox);

        return new EmblemViewHolder(card, icon, txtName, txtSub);
    }

    @Override
    public void onBindViewHolder(@NonNull EmblemViewHolder holder, int position) {
        ListEmblemModel e = list.get(position);

        holder.txtName.setText(e.getName());

        // Subtext beda untuk main vs ability
        if ("main".equals(e.getKind())) {
            ArrayList<String> attrs = e.getAttributes();
            String sub = (attrs != null && attrs.size() > 0)
                    ? attrs.get(0) + (attrs.size() > 1 ? " • " + attrs.get(1) : "")
                    : "Main Emblem";
            holder.txtSub.setText(sub);
        } else {
            String sub = "Section " + e.getSection();
            if (e.getBenefits() != null && !e.getBenefits().trim().isEmpty()) {
                sub = sub + " • " + e.getBenefits();
            }
            holder.txtSub.setText(sub);
        }

        // ICON pakai path dari JSON
        String iconUrl = (e.getIconPath() != null && !e.getIconPath().trim().isEmpty())
                ? BASE_REPO + e.getIconPath()
                : DEFAULT_ICON;

        Picasso.get()
                .load(iconUrl)
                .into(holder.icon, new Callback() {
                    @Override
                    public void onSuccess() {}

                    @Override
                    public void onError(Exception ex) {
                        Picasso.get().load(DEFAULT_ICON).into(holder.icon);
                    }
                });

        holder.itemView.setOnClickListener(v -> {
            DetailEmblemDialog dialog = DetailEmblemDialog.newInstance(e);
            dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "detail_emblem");
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class EmblemViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView txtName, txtSub;

        EmblemViewHolder(@NonNull View itemView, ImageView icon, TextView txtName, TextView txtSub) {
            super(itemView);
            this.icon = icon;
            this.txtName = txtName;
            this.txtSub = txtSub;
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
