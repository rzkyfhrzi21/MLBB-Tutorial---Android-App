package com.example.mlbbtutorial;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

public class TentangActivity extends AppCompatActivity {

    private static final String BASE_IMG =
            "https://raw.githubusercontent.com/rzkyfhrzi21/mlbb-tutorial-api/refs/heads/master/assets/tentang/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tentang);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets s = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(s.left, s.top, s.right, s.bottom);
            return insets;
        });

        // BACK
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // FOTO
        ImageView imgRizky = findViewById(R.id.imgRizky);
        ImageView imgAdji  = findViewById(R.id.imgAdji);
        ImageView imgDewa  = findViewById(R.id.imgDewa);

        Picasso.get().load(BASE_IMG + "rizky.jpeg").into(imgRizky);
        Picasso.get().load(BASE_IMG + "adji.jpeg").into(imgAdji);
        Picasso.get().load(BASE_IMG + "dewa.jpeg").into(imgDewa);

        // IG
        openLink(findViewById(R.id.igRizky), "https://instagram.com/rzkydev666");
        openLink(findViewById(R.id.igAdji),  "https://instagram.com/rrival_dii");
        openLink(findViewById(R.id.igDewa),  "https://instagram.com/d.dewaaaa");

        // EMAIL
        openEmail(findViewById(R.id.emailRizky), "rzkyfhrzi21@gmail.com");
        openEmail(findViewById(R.id.emailAdji),  "adjirivaldi7@gmail.com");
        openEmail(findViewById(R.id.emailDewa),  "dewarastine@gmail.com");

        // SUPPORT
        openEmail(findViewById(R.id.emailSupport), "mlbbtutorialofc@gmail.com");
    }

    private void openLink(TextView tv, String url) {
        tv.setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        );
    }

    private void openEmail(TextView tv, String email) {
        tv.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_SENDTO);
            i.setData(Uri.parse("mailto:" + email));
            startActivity(i);
        });
    }
}
