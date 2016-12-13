package net.jacobmason.velocityvortexscorekeeper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.HapticFeedbackConstants;
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
    private TableLayout scoringTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("ScoringActivity", "Created Activity");

        setContentView(R.layout.activity_scoring_screen);
        scoringTable = (TableLayout) findViewById(R.id.scoring_table);
        scoringTable.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        View topPadding = findViewById(R.id.topPadding);
        View bottomPadding = findViewById(R.id.bottomPadding);
        switch (getIntent().getStringExtra("scoring_style")) {
            case "Red":
                topPadding.setBackgroundResource(R.color.red);
                scoringTable.addView(new ScoringButton(this, "Center Goal", R.color.red));
                scoringTable.addView(new ScoringButton(this, "Corner Goal", R.color.red));
                bottomPadding.setBackgroundResource(R.color.red);
                break;
            case "Red Corner":
                topPadding.setBackgroundResource(R.color.red);
                scoringTable.addView(new ScoringButton(this, "Corner Goal", R.color.red));
                bottomPadding.setBackgroundResource(R.color.red);
                break;
            case "Red Center":
                topPadding.setBackgroundResource(R.color.red);
                scoringTable.addView(new ScoringButton(this, "Center Goal", R.color.red));
                bottomPadding.setBackgroundResource(R.color.red);
                break;

            case "Blue":
                topPadding.setBackgroundResource(R.color.blue);
                scoringTable.addView(new ScoringButton(this, "Center Goal", R.color.blue));
                scoringTable.addView(new ScoringButton(this, "Corner Goal", R.color.blue));
                bottomPadding.setBackgroundResource(R.color.blue);
                break;
            case "Blue Corner":
                topPadding.setBackgroundResource(R.color.blue);
                scoringTable.addView(new ScoringButton(this, "Corner Goal", R.color.blue));
                bottomPadding.setBackgroundResource(R.color.blue);
                break;
            case "Blue Center":
                topPadding.setBackgroundResource(R.color.blue);
                scoringTable.addView(new ScoringButton(this, "Center Goal", R.color.blue));
                bottomPadding.setBackgroundResource(R.color.blue);
                break;

            case "2 Corners":
                topPadding.setBackgroundResource(R.color.red);
                scoringTable.addView(new ScoringButton(this, "Red Corner Goal", R.color.red));
                scoringTable.addView(new ScoringButton(this, "Blue Corner Goal", R.color.blue));
                bottomPadding.setBackgroundResource(R.color.blue);
                break;

            case "2 Centers":
                topPadding.setBackgroundResource(R.color.red);
                scoringTable.addView(new ScoringButton(this, "Red Center Goal", R.color.red));
                scoringTable.addView(new ScoringButton(this, "Blue Center Goal", R.color.blue));
                bottomPadding.setBackgroundResource(R.color.blue);
                break;

            case "All Goals":
                topPadding.setBackgroundResource(R.color.red);
                scoringTable.addView(new ScoringButton(this, "Red Center Goal", R.color.red));
                scoringTable.addView(new ScoringButton(this, "Red Corner Goal", R.color.red));
                scoringTable.addView(new ScoringButton(this, "Blue Center Goal", R.color.blue));
                scoringTable.addView(new ScoringButton(this, "Blue Corner Goal", R.color.blue));
                bottomPadding.setBackgroundResource(R.color.blue);
                break;
        }
    }

    class ScoringButton extends TableRow {
        private int score = 0;

        public ScoringButton(Context context, final String goalName, int backgroundColor) {
            super(context);

            TableLayout.LayoutParams params = new TableLayout.LayoutParams();
            params.weight = 1;
            this.setLayoutParams(params);
            this.setBackgroundResource(backgroundColor);

            final Button button = (Button) getLayoutInflater().inflate(R.layout.scoring_button, this, false);
            button.setText(String.format(Locale.US, "%s\n0", goalName));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    button.setText(String.format(Locale.US, "%s\n%d", goalName, ++score));
                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                }
            });
            button.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (score > 0) {
                        button.setText(String.format(Locale.US, "%s\n%d", goalName, --score));
                    }
                    return true;
                }
            });
            this.addView(button);
        }

        public int getScore() {
            return score;
        }
    }

    @Override
    public void onBackPressed() {
        for (int i = 0; i < scoringTable.getChildCount(); ++i) {
            Log.d("ScoringActivity", String.valueOf(((ScoringButton) scoringTable.getChildAt(i)).getScore()));
        }
        super.onBackPressed();
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
