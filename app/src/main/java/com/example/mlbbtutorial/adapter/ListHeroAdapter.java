package com.example.mlbbtutorial.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mlbbtutorial.DetailHeroes;
import com.example.mlbbtutorial.R;
import com.example.mlbbtutorial.model.ListHeroModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;
import java.util.Locale;

public class ListHeroAdapter extends RecyclerView.Adapter<ListHeroAdapter.HeroViewHolder> {

    private final Context context;
    private final List<ListHeroModel> heroList;

    // BASE URL HERO ICON
    private static final String BASE_URL =
            "https://raw.githubusercontent.com/rzkyfhrzi21/mlbb-tutorial-api/refs/heads/master/assets/heroes/";

    public ListHeroAdapter(Context context, List<ListHeroModel> heroList) {
        this.context = context;
        this.heroList = heroList;
    }

    @NonNull
    @Override
    public HeroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardView card = new CardView(context);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        int m = dp(6);
        params.setMargins(m, m, m, m);
        card.setLayoutParams(params);
        card.setRadius(dp(12));
        card.setCardBackgroundColor(0xFF121212);

        LinearLayout box = new LinearLayout(context);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setGravity(Gravity.CENTER);
        box.setPadding(dp(8), dp(8), dp(8), dp(8));
        card.addView(box);

        ImageView icon = new ImageView(context);
        icon.setLayoutParams(new FrameLayout.LayoutParams(dp(72), dp(72)));
        icon.setScaleType(ImageView.ScaleType.CENTER_CROP);

        TextView name = new TextView(context);
        name.setTextColor(0xFFFFFFFF);
        name.setTextSize(12);
        name.setGravity(Gravity.CENTER);
        name.setPadding(0, dp(6), 0, 0);

        box.addView(icon);
        box.addView(name);

        return new HeroViewHolder(card, icon, name);
    }

    @Override
    public void onBindViewHolder(@NonNull HeroViewHolder h, int pos) {

        ListHeroModel hero = heroList.get(pos);
        h.txtName.setText(hero.getHeroName());

        String heroIcon = hero.getHeroIcon() == null
                ? ""
                : hero.getHeroIcon().toLowerCase(Locale.ROOT);

        String imgUrl = heroIcon.isEmpty()
                ? null
                : BASE_URL + heroIcon;

        // ✅ FALLBACK KE DRAWABLE LOKAL
        Picasso.get()
                .load(imgUrl)
                .placeholder(R.drawable.default_icon)
                .error(R.drawable.default_icon)
                .transform(new CircleTransform())
                .into(h.imgHero);

        h.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, DetailHeroes.class);
            i.putExtra("hero_name", hero.getHeroName().toLowerCase(Locale.ROOT));
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return heroList.size();
    }

    static class HeroViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHero;
        TextView txtName;

        HeroViewHolder(View v, ImageView i, TextView n) {
            super(v);
            imgHero = i;
            txtName = n;
        }
    }

    private int dp(int v) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                v,
                context.getResources().getDisplayMetrics()
        );
    }

    // ROUND ICON
    static class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap src) {
            int size = Math.min(src.getWidth(), src.getHeight());
            int x = (src.getWidth() - size) / 2;
            int y = (src.getHeight() - size) / 2;

            Bitmap squared = Bitmap.createBitmap(src, x, y, size, size);
            if (squared != src) src.recycle();

            Bitmap out = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(out);
            Paint p = new Paint();
            p.setAntiAlias(true);
            p.setShader(new BitmapShader(squared, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
            c.drawCircle(size / 2f, size / 2f, size / 2f, p);

            squared.recycle();
            return out;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}
