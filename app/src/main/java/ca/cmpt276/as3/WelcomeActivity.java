package ca.cmpt276.as3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

        setupSkipButton();
        startAnimations();
    }

    private void startAnimations() {
        TextView bug = findViewById(R.id.bug);
        Animation move = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_bug);
        bug.startAnimation(move);

        move.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                handler = new Handler();

                //delayed handler that starts the main activity
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                        finish();
                    }
                };
                handler.postDelayed(runnable, DELAY_MILLIS + animation.getDuration());
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                TextView bug = findViewById(R.id.bug);
                bug.setText(R.string.dead_bug);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void setupSkipButton() {
        //skip button override of handler
        Button skipButton = findViewById(R.id.skipButton);
        skipButton.setOnClickListener(v -> {
            if(runnable != null) {
                handler.removeCallbacks(runnable);
            }
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