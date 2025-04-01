package com.example.mealmate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.window.SplashScreen;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class splash_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Delay for 3 seconds (3000 milliseconds)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the main activity (Profile or Home screen)
                Intent intent = new Intent(splash_screen.this, login_page.class);
                startActivity(intent);

                // Finish splash activity so the user cannot go back to it
                finish();
            }
        }, 3000); // 3 seconds delay
    }
}