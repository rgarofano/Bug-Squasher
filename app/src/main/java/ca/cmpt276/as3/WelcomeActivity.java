package ca.cmpt276.as3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    public static final int DELAY_MILLIS = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getSupportActionBar().hide();

        //start android animations

        //delayed handler that starts the main activity
       Handler handler =  new Handler();
       Runnable runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        };
       handler.postDelayed(runnable, DELAY_MILLIS);

        //skip button override of handler
        Button skipButton = findViewById(R.id.skipButton);
        skipButton.setOnClickListener(v -> {
            handler.removeCallbacks(runnable);
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        });
    }
}