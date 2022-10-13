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
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    private static final int NUM_ROWS = 4;
    private static final int NUM_COLS = 6;
    private Button[][] buttons = new Button[NUM_ROWS][NUM_COLS];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().hide();
        populateButtons();
    }

    private void populateButtons() {
        TableLayout table = findViewById(R.id.tableForButtons);
        for (int row = 0; row < NUM_ROWS; row++) {
            // Create a new row in the table and set the layout parameters
            // such that the row  spaces evenly with other rows to take up all available space
            TableRow tableRow = new TableRow(this);
            setTableRowLayoutParams(tableRow);
            // add the TableRow as a child of TableLayout in the .xml
            table.addView(tableRow);
            for (int col = 0; col < NUM_COLS; col++) {
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
        Button button = buttons[row][col];
        lockButtonSizes();
        // change size of background image and set it as the bug icon
        int newWidth = button.getWidth();
        int newHeight = button.getHeight();
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bug_red);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
        Resources resource = getResources();
        button.setBackground(new BitmapDrawable(resource, scaledBitmap));
    }

    private void lockButtonSizes() {
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
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
}