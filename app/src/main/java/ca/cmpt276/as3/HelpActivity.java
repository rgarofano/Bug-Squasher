package ca.cmpt276.as3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        getSupportActionBar().hide();
        setupHyperlinks();
    }

    private void setupHyperlinks() {
        TextView aboutAuthor = findViewById(R.id.aboutAuthor);
        aboutAuthor.setMovementMethod(LinkMovementMethod.getInstance());

        TextView references = findViewById(R.id.references);
        references.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, HelpActivity.class);
        return intent;
    }
}