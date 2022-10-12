package ca.cmpt276.as3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

public class GameActivity extends AppCompatActivity {

    private static final int NUM_ROWS = 2;
    private static final int NUM_COLS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        populateButtons();
    }

    private void populateButtons() {
        TableLayout table = findViewById(R.id.tableForButtons);
        for (int row = 0; row < NUM_ROWS; row++) {
            TableRow tableRow = createCustomTableRow();
            table.addView(tableRow);
            for (int col = 0; col < NUM_COLS; col++) {
                Button button = createCustomButton();
                tableRow.addView(button);
            }
        }
    }

    private TableRow createCustomTableRow() {
        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT,
                1.0f));
        return tableRow;
    }

    private Button createCustomButton() {
        Button button = new Button(this);
        button.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT,
                1.0f));
        return button;
    }
}