package com.example.mlbbtutorial.adapter;

import android.content.Context;
import android.content.Intent;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mlbbtutorial.DetailHeroes;
import com.example.mlbbtutorial.model.ListHeroModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

public class ListHeroAdapter extends RecyclerView.Adapter<ListHeroAdapter.HeroViewHolder> {

    private final Context context;
    private final List<ListHeroModel> heroList;

    // BASE URL (RAW GitHub, assets/heroes/)
    private static final String BASE_URL =
            "https://raw.githubusercontent.com/rzkyfhrzi21/mlbb-tutorial-api/9c6d2251a29d5ec6058dbbafd089806b432f9299/assets/heroes/webp/";

    // DEFAULT IMAGE
    private static final String DEFAULT_IMAGE =
            BASE_URL + "default_skill.webp";

    public ListHeroAdapter(Context context, List<ListHeroModel> heroList) {
        this.context = context;
        this.heroList = heroList;
    }

    @NonNull
    @Override
    public HeroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardView card = new CardView(context);
        RecyclerView.LayoutParams cardParams = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        int margin = dp(6);
        cardParams.setMargins(margin, margin, margin, margin);
        card.setLayoutParams(cardParams);
        card.setRadius(dp(12));
        card.setCardElevation(dp(2));
        card.setCardBackgroundColor(Color.parseColor("#121212"));

        LinearLayout container = new LinearLayout(context);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setGravity(Gravity.CENTER);
        container.setPadding(dp(10), dp(10), dp(10), dp(10));
        card.addView(container);

        FrameLayout frame = new FrameLayout(context);
        frame.setLayoutParams(new FrameLayout.LayoutParams(dp(76), dp(76)));

        GradientDrawable ring = new GradientDrawable();
        ring.setShape(GradientDrawable.OVAL);
        ring.setColor(Color.TRANSPARENT);
        ring.setStroke(dp(2), Color.parseColor("#64FFDA"));
        frame.setBackground(ring);
        frame.setPadding(dp(3), dp(3), dp(3), dp(3));

        ImageView imgHero = new ImageView(context);
        imgHero.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        imgHero.setScaleType(ImageView.ScaleType.CENTER_CROP);
        frame.addView(imgHero);

        TextView txtName = new TextView(context);
        LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        txtParams.topMargin = dp(8);
        txtName.setLayoutParams(txtParams);
        txtName.setTextColor(Color.WHITE);
        txtName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        txtName.setGravity(Gravity.CENTER);

        container.addView(frame);
        container.addView(txtName);

        return new HeroViewHolder(card, imgHero, txtName);
    }

    @Override
    public void onBindViewHolder(@NonNull HeroViewHolder holder, int position) {
        ListHeroModel hero = heroList.get(position);
        String heroUrl = hero.getHeroUrl();

        holder.txtName.setText(hero.getHeroUrl());

        String fileName = hero.getHeroIcon();

        // 👉 Set tag CardView = CV_(nama_hero)
        String safeHeroName = heroUrl.toLowerCase().replace(" ", "_");
        holder.itemView.setTag("CV_" + safeHeroName);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailHeroes.class);

            // Kirim extra (nilai nama hero)
            intent.putExtra("hero_url", heroUrl);

            context.startActivity(intent);
        });

        String imageUrl = (fileName != null && !fileName.trim().isEmpty())
                ? BASE_URL + fileName.trim()
                : DEFAULT_IMAGE;

        // LOAD IMAGE DENGAN CALLBACK FALLBACK
        Picasso.get()
                .load(imageUrl)
                .transform(new CircleTransform())
                .into(holder.imgHero, new Callback() {
                    @Override
                    public void onSuccess() {
                        // gambar hero berhasil dimuat
                    }

                    @Override
                    public void onError(Exception e) {
                        // ❗ fallback ke default image (URL)
                        Picasso.get()
                                .load(DEFAULT_IMAGE)
                                .transform(new CircleTransform())
                                .into(holder.imgHero);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return heroList.size();
    }

    static class HeroViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHero;
        TextView txtName;

        HeroViewHolder(@NonNull View itemView, ImageView imgHero, TextView txtName) {
            super(itemView);
            this.imgHero = imgHero;
            this.txtName = txtName;
        }
    }

    private int dp(int value) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value,
                context.getResources().getDisplayMetrics()
        );
    }

    // ===== Circle Transform (pengganti Glide.circleCrop) =====
    static class CircleTransform implements Transformation {

        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
            if (squared != source) source.recycle();

            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setAntiAlias(true);

            BitmapShader shader =
                    new BitmapShader(squared, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squared.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}
