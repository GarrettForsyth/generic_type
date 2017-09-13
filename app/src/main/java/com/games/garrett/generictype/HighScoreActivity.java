package com.games.garrett.generictype;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Garrett on 4/5/2017.
 */

public class HighScoreActivity extends AppCompatActivity {

    private static final String TAG = HighScoreActivity.class.getCanonicalName();
    TextView mHighScores;
    Button backToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_score);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        //hide the keyboard
       // View view = this.getCurrentFocus();
        //if (view != null) {
        //    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
         //   imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        //}


        Log.d(TAG, "High Score activity entered.");
        mHighScores = (TextView) findViewById(R.id.highScores);
        backToMain = (Button) findViewById(R.id.backToMain);

        //get user score
        int userScore = getIntent().getIntExtra("score",0);

        Log.d(TAG, "User score extracted as : " + userScore);

        // get current high scores
        SharedPreferences pref = this.getSharedPreferences("highScores", Context.MODE_PRIVATE);
         Integer [] highScores = {
                pref.getInt("s1",0),
                pref.getInt("s2",0),
                pref.getInt("s3",0),
                pref.getInt("s4",0),
                pref.getInt("s5",0),
                userScore
        };

        // insert user score where appropriate
         Arrays.sort(highScores, Collections.reverseOrder());

        // restore high scores
        SharedPreferences.Editor editor = pref.edit();

        String scoreString = "" ;
        for (int i = 1; i < 6; i++){
            String str = "s" + Integer.toString(i);

            editor.putInt(str, highScores[i-1]);
            editor.apply();

            Log.d(TAG, "Checking key name : " + scoreString);
            if (Integer.toString(i) != null) {
                scoreString += "\n" + Integer.toString(highScores[i-1]);
            }
        }

        mHighScores.setText(scoreString);


    }

    public void onButtonClicked(View v){
        Log.d(TAG, "Back to Main Menu button clicked.");
        Intent i = new Intent(this, MainActivity.class);
        finish();
        startActivity(i);

    }

}
