package com.games.garrett.generictype;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.games.garrett.generictype.R.color.white;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getCanonicalName();

    private TextView menuTitle;
    private TextView menuDifficulty;
    private List<Button> selectionButtons;
    private Integer mDifficulty = 0;
    private static final int[] BUTTON_IDS = {
            R.id.game_one_button,
            R.id.game_two_button,
            R.id.exit_button,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpButtons();

        menuTitle = (TextView)findViewById(R.id.menuTitle);
        menuTitle.setText("Generic Type");
        menuTitle.setTextSize(40);
        menuTitle.setTextColor(Color.WHITE);
        menuTitle.setGravity(17);

        menuDifficulty = (TextView)findViewById(R.id.menuDifficulty);
        menuDifficulty.setText("Difficulty");
        menuDifficulty.setTextSize(25);
        menuDifficulty.setTextColor(Color.WHITE);
        menuDifficulty.setGravity(17);

    }

    private void setUpButtons(){
        selectionButtons= new ArrayList<>(BUTTON_IDS.length);
        for(int id : BUTTON_IDS) {
            Button button = (Button) findViewById(id);
            button.setOnClickListener(this);
            button.setTextSize(20);
            selectionButtons.add(button);
        }
    }

    public void onClick(View view){

        switch(view.getId()){
            case R.id.game_one_button:
                Intent intent = GameOneActivity.makeGameOneActivityIntent(this);
                intent.putExtra("difficulty", mDifficulty);
                finish();
                startActivity(intent);
                break;
            case R.id.game_two_button:
                Intent intentTwo = GameTwoActivity.makeGameTwoActivityIntent(this);
                intentTwo.putExtra("difficulty", mDifficulty);
                finish();
                startActivity(intentTwo);
                break;
            case R.id.exit_button:
                finish();
                break;
        }
    }

    public void onRadioButtonClicked(View view){
        // is the button now checked?
        boolean checked = ((RadioButton)view).isChecked();

        // check which button is checked:
        switch(view.getId()){
            case R.id.radio_easy :
                if (checked) mDifficulty = 0;
                Log.d(TAG, "difficulty set to easy");
                break;

            case R.id.radio_normal :
                if (checked) mDifficulty = 1;
                Log.d(TAG, "difficulty set to normal");
                break;

            case R.id.radio_hard :
                if (checked) mDifficulty = 2;
                Log.d(TAG, "difficulty set to hard");
                break;
        }
    }
}
