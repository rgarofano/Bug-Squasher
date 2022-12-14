package ca.cmpt276.as3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.security.cert.PKIXRevocationChecker;

import ca.cmpt276.as3.model.GameLogic;

public class GameActivity extends AppCompatActivity {
    private GameLogic gameLogic;
    private boolean gameOver = false;
    private int numberOfRows;
    private int numberOfColumns;
    private int debugCount = 0;
    private int numberTimesPlayed;
    private Button[][] buttons;
    private MediaPlayer bugFoundMediaPlayer;
    private MediaPlayer scanMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().hide();
        gameLogic = GameLogic.getInstance();
        restoreGameSettings();
        initializeBugsFoundText();
        numberOfRows = gameLogic.getNumberOfRows();
        numberOfColumns = gameLogic.getNumberOfColumns();
        buttons = new Button[numberOfRows][numberOfColumns];
        populateButtons();
        gameLogic.initializeGame();
        bugFoundMediaPlayer = MediaPlayer.create(this, R.raw.shine);
        scanMediaPlayer = MediaPlayer.create(this, R.raw.scan);
        setNumberTimesPlayedText();
        setBestScoreText();
    }

    private void restoreGameSettings() {
        int numberRows = OptionsActivity.getNumberRows(this);
        int numberCols = OptionsActivity.getNumberCols(this);
        int numberBugs = OptionsActivity.getNumberBugs(this);
        gameLogic.changeOptions(numberRows, numberCols, numberBugs);
    }

    private void initializeBugsFoundText() {
        TextView bugsFound = findViewById(R.id.bugsFound);
        bugsFound.setText("Found 0 of " + gameLogic.getNumBugs() + " Bugs");
    }

    private void populateButtons() {
        TableLayout table = findViewById(R.id.tableForButtons);
        for (int row = 0; row < numberOfRows; row++) {
            // Create a new row in the table and set the layout parameters
            // such that the row  spaces evenly with other rows to take up all available space
            TableRow tableRow = new TableRow(this);
            setTableRowLayoutParams(tableRow);
            // Style TableRow to match theme
            tableRow.setBackgroundColor(Color.WHITE);
            tableRow.getBackground().setAlpha(100);
            // add the TableRow as a child of TableLayout in the .xml
            table.addView(tableRow);
            for (int col = 0; col < numberOfColumns; col++) {
                // Create a new button in the table row and set the layout
                // parameters such that the button spaces evenly with other buttons in the row
                Button button = new Button(this);
                setButtonLayoutParams(button);
                // click listener requires the passed in row and column to be final int
                // separate function created to resolve this and maintain readability
                createButtonOnClickListener(button, row, col);
                // improves readability of text at large grid sizes
                button.setPadding(0,0,0,0);
                // Style button to match theme
                button.setBackgroundColor(Color.BLACK);
                button.getBackground().setAlpha(200);
                // add the button as a child of the TableRow in the .xml
                tableRow.addView(button);
                // add the button to private array so that we can access it in click listener
                buttons[row][col] = button;
                // implement button into game logic
                gameLogic.addGameButton(row, col);
            }
        }
    }

    private void setTableRowLayoutParams(TableRow tableRow) {
        tableRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT,
                1.0f));
    }

    private void setButtonLayoutParams(Button button) {
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f);

        int leftMargin=5;
        int topMargin=5;
        int rightMargin=5;
        int bottomMargin=5;

        tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

        button.setLayoutParams(tableRowParams);
    }

    private void createButtonOnClickListener(Button button, final int row, final int col) {
        button.setOnClickListener(view -> gameButtonClicked(row, col));
    }

    private void gameButtonClicked(int row, int col) {
        if (gameOver) {
            return;
        }

        GameLogic.GameStatus gameStatus = gameLogic.generateClickResponse(row, col);
        Button button = buttons[row][col];
        lockButtonSizes();

        if (gameStatus == GameLogic.GameStatus.GAME_OVER) {
            revealBug(button);
            updateBugsFound(gameLogic.getNumBugs());
            playBugFoundSoundEffect();
            launchDialog();
            OptionsActivity.saveTimesPlayed(this, ++numberTimesPlayed);
            OptionsActivity.saveBestScore(this, numberOfRows, numberOfColumns, gameLogic.getNumBugs(), debugCount);
            gameOver = true;
        } else if (gameStatus == GameLogic.GameStatus.BUG_FOUND) {
            revealBug(button);
            updateBugsFound(gameLogic.getBugsFound());
            playBugFoundSoundEffect();
        } else if (gameStatus != GameLogic.GameStatus.NO_SCAN_USED) {
            updateDebugCount();
            playScanSoundEffect();
        }

        updateUIText();
    }

    private void lockButtonSizes() {
        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < numberOfColumns; col++) {
                Button button = buttons[row][col];
                int width = button.getWidth();
                button.setMinWidth(width);
                button.setMaxWidth(width);
                int height = button.getHeight();
                button.setMinHeight(height);
                button.setMaxHeight(height);
            }
        }
    }

    private void revealBug(Button button) {
        int newWidth = button.getWidth();
        int newHeight = button.getHeight();
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bug_red);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
        Resources resource = getResources();
        button.setBackground(new BitmapDrawable(resource, scaledBitmap));
    }

    private void updateBugsFound(int numBugsFound) {
        TextView bugsFound = findViewById(R.id.bugsFound);
        bugsFound.setText("Found " + numBugsFound + " of " + gameLogic.getNumBugs() + " Bugs" );
    }

    private void playBugFoundSoundEffect() {
        if (scanMediaPlayer.isPlaying()) {
            scanMediaPlayer.seekTo(3000);
        }
        if (bugFoundMediaPlayer.isPlaying()) {
            bugFoundMediaPlayer.seekTo(0);
        } else {
            bugFoundMediaPlayer.start();
        }
    }

    private void playScanSoundEffect() {
        if (scanMediaPlayer.isPlaying()) {
            scanMediaPlayer.seekTo(0);
        } else {
            scanMediaPlayer.start();
        }
    }

    private void setNumberTimesPlayedText() {
        TextView txtTimesPlayed = findViewById(R.id.textViewTimesPlayed);
        numberTimesPlayed = OptionsActivity.getTimesPlayed(this);
        txtTimesPlayed.setText("Times Played: " + numberTimesPlayed);
    }

    private void setBestScoreText() {
        TextView txtBestScore = findViewById(R.id.textViewBestScore);
        int numberBugs = gameLogic.getNumBugs();
        int bestScore = OptionsActivity.getBestScore(this, numberOfRows, numberOfColumns, numberBugs);
        String text = bestScore == -1 ? "Best Score: -" : "Best Score: " + bestScore;
        txtBestScore.setText(text);
    }

    private void updateDebugCount() {
        debugCount++;
        TextView debugAttempts = findViewById(R.id.debugAttempts);
        debugAttempts.setText("Debug Attempts: " + debugCount);
    }

    private void updateUIText() {
        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < numberOfColumns; col++) {
                Button button = buttons[row][col];
                String updatedText = gameLogic.getText(row, col);
                button.setText(updatedText);
                button.setTextColor(Color.parseColor("#39FF14"));
            }
        }
    }

    private void launchDialog() {
        // followed a tutorial for this code -> https://www.youtube.com/watch?v=5PaWtQAOdi8&ab_channel=TechnicalSkillz
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.win_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button okButton = dialog.findViewById(R.id.btnOk);
        okButton.setOnClickListener(view -> finish());
        dialog.show();
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, GameActivity.class);
        return intent;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameLogic.resetGame();
    }
}