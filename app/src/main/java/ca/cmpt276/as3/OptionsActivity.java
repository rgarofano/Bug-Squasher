package ca.cmpt276.as3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
        }
    }

    private void updateBoardSize(final int numRows, final int numCols) {
        gameLogic.changeOptions(numRows, numCols, gameLogic.getNumBugs());
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
        }
    }

    private void updateNumBugs(final int numBugs) {
        final int currentNumRows = gameLogic.getNumberOfRows();
        final int currentNumCols = gameLogic.getNumberOfColumns();
        gameLogic.changeOptions(currentNumRows, currentNumCols, numBugs);
    }

    static Intent getIntent(Context context) {
        Intent intent = new Intent(context, OptionsActivity.class);
        return intent;
    }
}