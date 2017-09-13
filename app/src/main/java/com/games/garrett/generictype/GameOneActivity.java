package com.games.garrett.generictype;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Game 1 :
 * Random words appear on the screen, the length of which depend ont he difficulty chosen.
 * Keyboard is pernamently on the screen. As the user types out the word, the letters
 * change from white to green.
 *
 * Created by Garrett on 2/17/2017.
 */

public class GameOneActivity  extends AppCompatActivity implements TaskCallBack{


    private static final String TAG = GameOneActivity.class.getCanonicalName();
    private ArrayList<String> dictionary;  // stores relevant words from dictionary
    private List<Integer> wordLengths;     // word lengths chosen based on difficulty
    private TextView randWord;             // field to dispaly a random word
    private TextView score;                // field to dispaly the score
    private Integer intScore;              // score as an integer type
    private CustomEdit userInput;          // onKeyPreImeMethod used
    private ProgressBar mProgress;         // progress bar to dispaly the timer
    private Integer mProgressStatus = 100; // integer to track the current status of the bar
    private Integer mProgressIncrement = 2;// level at which the progress will decay

    private Handler mHandler = new Handler(); // handler to pass to async task

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_one_activity);

        // extracts the difficutly form the starting intent
        Integer mDifficulty = getIntent().getIntExtra("difficulty", 0);

        // set up the progress bar, the edit, the intial score,
        // an on click listner for the editText, and put the focus
        //  on the edit text.
        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        userInput = (CustomEdit) findViewById(R.id.userInput);
        score = (TextView) findViewById(R.id.score);
        score.setText("0");
        setUpTextChangedListener(userInput);
        setFocusOnEditText(userInput);
        intScore = 0;

        // sets up the dictionary based on the difficulty selection
        // and shows the first word
        setWordLengths(mDifficulty);
        createDictionary();
        showRandomWord();

        // starts up the timer
        new AsyncTaskProgressBar(this, this).execute();

    }

    /**
     * Brings out keyboard when view appears
     * @param editText to be focused upon
     */
    private void setFocusOnEditText(CustomEdit editText){
        editText.clearFocus();
        editText.requestFocus();
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    /**
     * Attaches a listener to an edit text.
     * @param editText to have listener attached to
     */
    private void setUpTextChangedListener(CustomEdit editText){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    Log.d(TAG, "beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged" +
                "  start = " + i +
                "  before = " + i1 +
                "  count = " + i2 +
                "  characterSequence = " + charSequence);
                int randLen = randWord.getText().length();
                // only need to check when input is less than the rand word
                // otherwise, obviously the input is wrong
                if ( i2+i <= randLen) {
                    // whenever a new character is added, iterate through all
                    // letters and check if they match the randWord dispalyed

                    //changes userInput to red if wrong letter is inputted
                    userInput.setTextColor(ResourcesCompat.getColor(getResources(),
                            R.color.darkGreen, null));
                    for (int j = 0; j < i2 + i; j++) {
                        if (!(randWord.getText().charAt(j) == charSequence.charAt(j))) {
                            userInput.setTextColor(ResourcesCompat.getColor(getResources(),
                                    R.color.red, null));
                        }
                    }
                }

                //if the user has spelled the word correctly:
                if( (i2+i) == randLen && userInput.getCurrentTextColor() ==
                        ResourcesCompat.getColor(getResources(), R.color.darkGreen, null)){
                    randWord.setText(getRanddomWord());
                    userInput.setText("");

                    int newScore = (Integer.parseInt(score.getText().toString()))+10;
                    intScore = newScore;

                    // Reset timer every time the score is an increment of 50
                    if ( newScore % 50 == 0 )  mProgressStatus = 100;
                    // Increase timer speed every time score is an incerement of 50
                    if ( newScore % 50 == 0 )  mProgressIncrement += 1;

                    score.setText(String.valueOf(newScore));

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged");
            }
        });
    }

    /*
    Factory method for making a GameOneActivity Intent
     */
    public static Intent makeGameOneActivityIntent(Context context){

        return new Intent(context, GameOneActivity.class);
    }

    /**
     * Initializes the dictionary, only adding words that
     * are the appropriate length as chosen by the
     * difficulty.
     */
    private void createDictionary(){
        Log.d(TAG, "createDictionary()");
        this.dictionary = new ArrayList<>();

        BufferedReader dict; //hold dictionary file
        AssetManager am = this.getAssets();

        try{
            //access dictionary.txt in assets folder
            dict = new BufferedReader(new InputStreamReader(am.open("dictionary.txt")));
            String word;
            //reads in only words of some length determind by difficulty settings
            while ((word = dict.readLine()) != null ){
                if ( wordLengths.contains(word.length())){
                    dictionary.add(word);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a random word fromt he dictionary.
     * @return a random word from the dictionary whose
     * length is determined by the difficulty setting
     */
    private String getRanddomWord(){
        Log.d(TAG, "getRanddomWord()");
        if (dictionary != null) {
            Random rand = new Random();
            return dictionary.get(rand.nextInt(dictionary.size()));
        }
        return null;
    }

    /**
     * Creates a list of acceptable word lengths
     * based on the chosen difficulty.
     * @param difficulty the user selected diffulty for the game
     */
    private void setWordLengths(int difficulty){
        Log.d(TAG,"setWordLengths(int difficulty) difficulty = " + difficulty);
        this.wordLengths = new ArrayList<>(3);
        if ( difficulty == 2){
            this.wordLengths.add(6);
            this.wordLengths.add(7);
            this.wordLengths.add(8);
            this.wordLengths.add(9);
        }
        else if ( difficulty == 1){
            this.wordLengths.add(5);
            this.wordLengths.add(6);
            this.wordLengths.add(7);
        }
        else{
            this.wordLengths.add(3);
            this.wordLengths.add(4);
            this.wordLengths.add(5);
        }
    }

    private void showRandomWord(){
        randWord = (TextView)findViewById(R.id.randWord);
        randWord.setTextColor(Color.WHITE);
        randWord.setText(getRanddomWord());
    }

    public void done(){
        finish();
    }


    private class AsyncTaskProgressBar extends AsyncTask< Void, Void, Void>{

        TaskCallBack mTaskCallBack;
        Context mContext;

         AsyncTaskProgressBar(TaskCallBack callback, Context context){
            mTaskCallBack = callback;
            mContext = context;
        }
        @Override
        protected Void doInBackground(Void... voids) {

            long timeToSleep = 1000;
            boolean interrupted = false;

                while ( mProgressStatus > 0 ){
                    // Make the thread sleep 1 second while progress bar isn't done
                    try {
                        Thread.sleep(timeToSleep);
                    }catch(InterruptedException e){
                        //work out how much more time to sleep

                        interrupted = true;
                    }

                    if (interrupted){
                        Thread.currentThread().interrupt();
                    }

                    mProgressStatus -= mProgressIncrement;

                    // Update the progress bar
                    mHandler.post( new Runnable() {
                        public void run() {
                            synchronized (mProgressStatus) {
                                mProgress.setProgress(mProgressStatus);
                            }
                            synchronized (mProgressIncrement) {
                            }
                            synchronized (intScore) {
                            }

                        }
                    });
                }

            return null;
        }

        @Override
        protected void onPostExecute(Void k){
            Log.d(TAG, "Background thread finished.");
            Intent i = new Intent(mContext, HighScoreActivity.class);
            i.putExtra("score", intScore);
            startActivity(i);
            mTaskCallBack.done();
        }
    }

}
