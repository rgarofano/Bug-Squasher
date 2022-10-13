package ca.cmpt276.as3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import ca.cmpt276.as3.model.GameLogic;

public class GameActivity extends AppCompatActivity {
    private GameLogic gameLogic;
    private int numberOfRows;
    private int numberOfColumns;
    private int debugCount = 0;
    private Button[][] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().hide();
        gameLogic = GameLogic.getInstance();
        numberOfRows = gameLogic.getNumberOfRows();
        numberOfColumns = gameLogic.getNumberOfColumns();
        buttons = new Button[numberOfRows][numberOfColumns];
        populateButtons();
        gameLogic.initializeGame();
    }

    private void populateButtons() {
        TableLayout table = findViewById(R.id.tableForButtons);
        for (int row = 0; row < numberOfRows; row++) {
            // Create a new row in the table and set the layout parameters
            // such that the row  spaces evenly with other rows to take up all available space
            TableRow tableRow = new TableRow(this);
            setTableRowLayoutParams(tableRow);
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
        button.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT,
                1.0f));
    }

    private void createButtonOnClickListener(Button button, final int row, final int col) {
        button.setOnClickListener(view -> gameButtonClicked(row, col));
    }

    private void gameButtonClicked(int row, int col) {
        GameLogic.GameStatus gameStatus = gameLogic.generateClickResponse(row, col);
        Button button = buttons[row][col];
        lockButtonSizes();
        if (gameStatus == GameLogic.GameStatus.BUG_FOUND
         || gameStatus == GameLogic.GameStatus.GAME_OVER) {
            revealBug(button);
            updateBugsFound();
        }

        if (gameStatus != GameLogic.GameStatus.NO_SCAN_USED) {
            updateDebugCount();
            updateUIText();
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

    private void updateBugsFound() {
        TextView bugsFound = findViewById(R.id.bugsFound);
        bugsFound.setText("Found " + gameLogic.getBugsFound() + " of " + gameLogic.getNumBugs() + " Bugs" );
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
            }
        }
    }
}