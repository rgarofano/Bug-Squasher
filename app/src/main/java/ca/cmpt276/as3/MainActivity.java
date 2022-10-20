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
        setupHelpButton();
        setupOptionsButton();
    }

    private void setupPlayButton() {
        Button playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(view -> launchGame());
    }

    private void setupHelpButton() {
        Button playButton = findViewById(R.id.helpButton);
        playButton.setOnClickListener(view -> launchHelp());
    }

    private void setupOptionsButton() {
        Button optionsButton = findViewById(R.id.optionButton);
        optionsButton.setOnClickListener(view -> launchOptions());
    }

    private void launchGame() {
        Intent intent = GameActivity.getIntent(this);
        startActivity(intent);
    }

    private void launchHelp() {
        Intent intent = HelpActivity.getIntent(this);
        startActivity(intent);
    }

    private void launchOptions() {
        Intent intent = OptionsActivity.getIntent(this);
        startActivity(intent);
    }
}