package com.example.mlbbtutorial.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
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

import com.example.mlbbtutorial.DetailBattleSpellDialog;
import com.example.mlbbtutorial.R;
import com.example.mlbbtutorial.model.ListBattleSpellModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

public class ListBattleSpellAdapter
        extends RecyclerView.Adapter<ListBattleSpellAdapter.SpellViewHolder> {

    private final Context context;
    private final List<ListBattleSpellModel> spellList;

    private static final String BASE_URL =
            "https://raw.githubusercontent.com/rzkyfhrzi21/mlbb-tutorial-api/refs/heads/master/assets/battle_spells/";

    public ListBattleSpellAdapter(Context context, List<ListBattleSpellModel> spellList) {
        this.context = context;
        this.spellList = spellList;
    }

    @NonNull
    @Override
    public SpellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardView card = new CardView(context);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(dp(6), dp(6), dp(6), dp(6));
        card.setLayoutParams(params);
        card.setRadius(dp(12));
        card.setCardBackgroundColor(Color.parseColor("#121212"));

        LinearLayout row = new LinearLayout(context);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(dp(12), dp(10), dp(12), dp(10));
        row.setGravity(Gravity.CENTER_VERTICAL);
        card.addView(row);

        // ICON FRAME
        FrameLayout frame = new FrameLayout(context);
        FrameLayout.LayoutParams fp =
                new FrameLayout.LayoutParams(dp(56), dp(56));
        frame.setLayoutParams(fp);

        GradientDrawable ring = new GradientDrawable();
        ring.setShape(GradientDrawable.OVAL);
        ring.setStroke(dp(2), Color.parseColor("#64FFDA"));
        frame.setBackground(ring);
        frame.setPadding(dp(3), dp(3), dp(3), dp(3));

        ImageView img = new ImageView(context);
        img.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        frame.addView(img);

        LinearLayout text = new LinearLayout(context);
        text.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams tp =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
        tp.leftMargin = dp(12);
        text.setLayoutParams(tp);

        TextView name = new TextView(context);
        name.setTextColor(Color.WHITE);
        name.setTextSize(14);

        TextView usage = new TextView(context);
        usage.setTextColor(Color.parseColor("#B0B0B0"));
        usage.setTextSize(12);

        text.addView(name);
        text.addView(usage);

        row.addView(frame);
        row.addView(text);

        return new SpellViewHolder(card, img, name, usage);
    }

    @Override
    public void onBindViewHolder(@NonNull SpellViewHolder holder, int position) {

        ListBattleSpellModel spell = spellList.get(position);

        holder.name.setText(spell.getName());
        holder.usage.setText(spell.getUsage());

        String iconName = spell.getName()
                .toLowerCase()
                .replace(" ", "_");

        String imageUrl = BASE_URL + iconName + ".webp";

        // ✅ LOAD ICON + FALLBACK KE DRAWABLE
        Picasso.get()
                .load(imageUrl)
                .transform(new CircleTransform())
                .into(holder.icon, new Callback() {
                    @Override
                    public void onSuccess() {}

                    @Override
                    public void onError(Exception e) {
                        Picasso.get()
                                .load(R.drawable.default_icon)
                                .transform(new CircleTransform())
                                .into(holder.icon);
                    }
                });

        holder.itemView.setOnClickListener(v -> {
            DetailBattleSpellDialog dialog =
                    DetailBattleSpellDialog.newInstance(
                            spell.getName(),
                            spell.getUsage(),
                            spell.getDescription(),
                            spell.getCooldown(),
                            spell.getUnlockedAtLevel()
                    );

            dialog.show(
                    ((AppCompatActivity) context).getSupportFragmentManager(),
                    "detail_spell"
            );
        });
    }

    @Override
    public int getItemCount() {
        return spellList.size();
    }

    /* ================= VIEW HOLDER ================= */

    static class SpellViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name, usage;

        SpellViewHolder(View v, ImageView i, TextView n, TextView u) {
            super(v);
            icon = i;
            name = n;
            usage = u;
        }
    }

    /* ================= UTIL ================= */

    private int dp(int v) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                v,
                context.getResources().getDisplayMetrics()
        );
    }

    static class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            Bitmap out = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(out);
            Paint p = new Paint();
            p.setAntiAlias(true);
            p.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
            c.drawCircle(size / 2f, size / 2f, size / 2f, p);
            source.recycle();
            return out;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}
