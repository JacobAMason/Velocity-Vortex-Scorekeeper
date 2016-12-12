package net.jacobmason.velocityvortexscorekeeper;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.Locale;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ScoringActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scoring_screen);
        TableLayout mContentView = (TableLayout) findViewById(R.id.fullscreen_content);
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        switch (getIntent().getStringExtra("scoring_style")) {
            case "Red":
            case "Red Corner":
            case "Red Center":
                mContentView.setBackgroundColor(0xFFFF0000);
                break;
            case "Blue":
            case "Blue Corner":
            case "Blue Center":
                mContentView.setBackgroundColor(0xFF0000FF);
                break;
            default:
                mContentView.setBackgroundColor(0xFFF57E25);
        }

        mContentView.addView(new ScoringButton(this, "Center Goal"));
        mContentView.addView(new ScoringButton(this, "Corner Goal"));
    }

    class ScoringButton extends TableRow {
        private int score = 0;

        public ScoringButton(Context context, final String goalName) {
            super(context);

            TableLayout.LayoutParams params = new TableLayout.LayoutParams();
            params.weight = 1;
            this.setLayoutParams(params);

            final Button button = (Button) getLayoutInflater().inflate(R.layout.scoring_button, this, false);
            button.setText(String.format(Locale.US, "%s\n0", goalName));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    button.setText(String.format(Locale.US, "%s\n%d", goalName, ++score));
                }
            });
            this.addView(button);
        }

        public int getScore() {
            return score;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
