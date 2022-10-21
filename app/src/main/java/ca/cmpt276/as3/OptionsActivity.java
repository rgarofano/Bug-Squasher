package ca.cmpt276.as3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import ca.cmpt276.as3.model.GameLogic;

public class OptionsActivity extends AppCompatActivity {

    private GameLogic gameLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        getSupportActionBar().hide();
        gameLogic = GameLogic.getInstance();
        populateBoardSizeRadioButtons();
        populateNumberBugsRadioButtons();
        setupTimesPlayedResetButton();
    }

    private void populateBoardSizeRadioButtons() {
        RadioGroup group = findViewById(R.id.radioGroupBoardSize);
        String[] sizeOptions = getResources().getStringArray(R.array.game_board_size);

        for (int i = 0; i < sizeOptions.length; i++) {
            // extract rows and columns
            String[] splitValues = sizeOptions[i].split(" ");
            final int numRows = Integer.parseInt(splitValues[0]);
            final int numCols = Integer.parseInt(splitValues[1]);
            // Create radio button
            RadioButton button = new RadioButton(this);
            button.setText(numRows + " rows " + numCols + " columns");
            button.setTextColor(Color.parseColor("#39FF14"));
            button.setOnClickListener(view -> updateBoardSize(numRows, numCols));
            group.addView(button);

            if (numRows == OptionsActivity.getNumberRows(this)
             && numCols == OptionsActivity.getNumberCols(this)) {
                button.setChecked(true);
            }
        }
    }

    private void updateBoardSize(final int numRows, final int numCols) {
        gameLogic.changeOptions(numRows, numCols, gameLogic.getNumBugs());
        saveGameBoardSize(numRows, numCols);
    }

    private void saveGameBoardSize(final int numRows, final int numCols) {
        SharedPreferences prefs = this.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("number of rows", numRows);
        editor.putInt("number of columns", numCols);
        editor.apply();
    }

    public static int getNumberRows(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        int rows = prefs.getInt("number of rows", 4);
        return rows;
    }

    public static int getNumberCols(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        int cols = prefs.getInt("number of columns", 6);
        return cols;
    }

    private void populateNumberBugsRadioButtons() {
        RadioGroup group = findViewById(R.id.radioGroupNumBugs);
        int[] numberBugsOptions = getResources().getIntArray(R.array.num_bugs_options);

        for (int i = 0; i < numberBugsOptions.length; i++) {
            final int numberBugs = numberBugsOptions[i];
            RadioButton button = new RadioButton(this);
            button.setText(numberBugs + " Bugs");
            button.setTextColor(Color.parseColor("#39FF14"));
            button.setOnClickListener(view -> updateNumBugs(numberBugs));
            group.addView(button);

            if (numberBugs == getNumberBugs(this)) {
                button.setChecked(true);
            }
        }
    }

    private void updateNumBugs(final int numBugs) {
        final int currentNumRows = gameLogic.getNumberOfRows();
        final int currentNumCols = gameLogic.getNumberOfColumns();
        gameLogic.changeOptions(currentNumRows, currentNumCols, numBugs);
        saveNumberBugs(numBugs);
    }

    private void setupTimesPlayedResetButton() {
        Button resetBtn = findViewById(R.id.buttonResetTimesPlayed);
        resetBtn.setOnClickListener(view -> resetTimesPlayed());
    }

    private void saveNumberBugs(int numBugs) {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("number of bugs", numBugs);
        editor.apply();
    }

    public static int getNumberBugs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        int numBugs = prefs.getInt("number of bugs", 6);
        return numBugs;
    }

    public static void saveTimesPlayed(Context context, int numberTimesPlayed) {
        SharedPreferences prefs = context.getSharedPreferences("TimesPlayed", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("Number of times played", numberTimesPlayed);
        editor.apply();
    }

    public static int getTimesPlayed(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("TimesPlayed", MODE_PRIVATE);
        int numberTimesPlayed = prefs.getInt("Number of times played", 1);
        return numberTimesPlayed;
    }

    private void resetTimesPlayed() {
        SharedPreferences prefs = getSharedPreferences("TimesPlayed", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
        Toast.makeText(this, "Number of Times Played Data Erased", Toast.LENGTH_SHORT)
                .show();
    }

    static Intent getIntent(Context context) {
        Intent intent = new Intent(context, OptionsActivity.class);
        return intent;
    }
}