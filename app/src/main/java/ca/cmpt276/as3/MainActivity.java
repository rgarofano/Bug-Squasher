package ca.cmpt276.as3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        setupPlayButton();
    }

    private void setupPlayButton() {
        Button playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(view -> launchGame());
    }

    private void launchGame() {
        Intent intent = GameActivity.getIntent(this);
        startActivity(intent);
    }
}