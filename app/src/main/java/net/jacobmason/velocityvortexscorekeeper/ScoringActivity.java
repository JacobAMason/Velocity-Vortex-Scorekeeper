package net.jacobmason.velocityvortexscorekeeper;

import android.content.Context;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ScoringActivity extends AppCompatActivity {
    private TableLayout scoringTable;
    private String webserverIP;
    private String gameMode;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("ScoringActivity", "Created Activity");

        setContentView(R.layout.activity_scoring_screen);
        scoringTable = (TableLayout) findViewById(R.id.relativeLayout);
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

        webserverIP = getIntent().getStringExtra("ip_address");
        gameMode = getIntent().getStringExtra("game_mode");

        View topPadding = findViewById(R.id.topPadding);
        View bottomPadding = findViewById(R.id.bottomPadding);
        switch (getIntent().getStringExtra("scoring_style")) {
            case "Red":
                topPadding.setBackgroundResource(R.color.red);
                scoringTable.addView(new ScoringButton(this, "Red", "Center"));
                scoringTable.addView(new ScoringButton(this, "Red", "Corner"));
                bottomPadding.setBackgroundResource(R.color.red);
                break;
            case "Red Center":
                topPadding.setBackgroundResource(R.color.red);
                scoringTable.addView(new ScoringButton(this, "Red", "Center"));
                bottomPadding.setBackgroundResource(R.color.red);
                break;
            case "Red Corner":
                topPadding.setBackgroundResource(R.color.red);
                scoringTable.addView(new ScoringButton(this, "Red", "Corner"));
                bottomPadding.setBackgroundResource(R.color.red);
                break;

            case "Blue":
                topPadding.setBackgroundResource(R.color.blue);
                scoringTable.addView(new ScoringButton(this, "Blue", "Center"));
                scoringTable.addView(new ScoringButton(this, "Blue", "Corner"));
                bottomPadding.setBackgroundResource(R.color.blue);
                break;
            case "Blue Center":
                topPadding.setBackgroundResource(R.color.blue);
                scoringTable.addView(new ScoringButton(this, "Blue", "Center"));
                bottomPadding.setBackgroundResource(R.color.blue);
                break;
            case "Blue Corner":
                topPadding.setBackgroundResource(R.color.blue);
                scoringTable.addView(new ScoringButton(this, "Blue", "Corner"));
                bottomPadding.setBackgroundResource(R.color.blue);
                break;

            case "2 Centers":
                topPadding.setBackgroundResource(R.color.red);
                scoringTable.addView(new ScoringButton(this, "Red", "Center"));
                scoringTable.addView(new ScoringButton(this, "Blue", "Center"));
                bottomPadding.setBackgroundResource(R.color.blue);
                break;

            case "2 Corners":
                topPadding.setBackgroundResource(R.color.red);
                scoringTable.addView(new ScoringButton(this, "Red", "Corner"));
                scoringTable.addView(new ScoringButton(this, "Blue", "Corner"));
                bottomPadding.setBackgroundResource(R.color.blue);
                break;

            case "All Goals":
                topPadding.setBackgroundResource(R.color.red);
                scoringTable.addView(new ScoringButton(this, "Red", "Center"));
                scoringTable.addView(new ScoringButton(this, "Red", "Corner"));
                scoringTable.addView(new ScoringButton(this, "Blue", "Center"));
                scoringTable.addView(new ScoringButton(this, "Blue", "Corner"));
                bottomPadding.setBackgroundResource(R.color.blue);
                break;
        }
    }

    class ScoringButton extends TableRow {
        private String alliance;
        private String goal;
        private int score = 0;

        public ScoringButton(Context context, final String alliance, String goal) {
            super(context);
            this.alliance = alliance;
            this.goal = goal;
            final String goalName = String.format(Locale.US, "%s %s Goal", alliance, goal);
            int backgroundColor;
            switch (alliance) {
                case "Red":
                    backgroundColor = R.color.red;
                    break;
                case "Blue":
                    backgroundColor = R.color.blue;
                    break;
                default:
                    backgroundColor = R.color.FTCorange;
            }

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
                    update_score();
                }
            });
            button.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (score > 0) {
                        button.setText(String.format(Locale.US, "%s\n%d", goalName, --score));
                    }
                    update_score();
                    return true;
                }
            });
            this.addView(button);
        }

        private void update_score() {
            if (!webserverIP.isEmpty()) {
                final String address = String.format(Locale.US, "http://%s:3486/scorekeeper/submit", webserverIP);
                try {
                    JSONObject params = new JSONObject();
                    params.put("gameMode", gameMode.toLowerCase());
                    params.put("alliance", alliance.toLowerCase());
                    params.put("goal", goal.toLowerCase());
                    params.put("score", score);

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, address, params,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("ScoringActivity", error.toString());
                                    Toast.makeText(getApplicationContext(), "Error sending score to server\nCheck your IP address and connectivity", Toast.LENGTH_SHORT).show();
                                }
                            });
                    ApplicationController.get_instance().getRequestQueue().add(request);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        public int getScore() {
            return score;
        }
    }
}
