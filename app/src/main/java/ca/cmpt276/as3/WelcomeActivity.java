package ca.cmpt276.as3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    private static final int DELAY_MILLIS = 4000;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getSupportActionBar().hide();

        //start android animations
        Animation move = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move);
        TextView bug = findViewById(R.id.bug);
        bug.startAnimation(move);

        move.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            //delayed handler that starts the main activity
                handler = new Handler();
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                        finish();
                    }
                };
                handler.postDelayed(runnable, DELAY_MILLIS);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //skip button override of handler
        Button skipButton = findViewById(R.id.skipButton);
        skipButton.setOnClickListener(v -> {
            handler.removeCallbacks(runnable);
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}