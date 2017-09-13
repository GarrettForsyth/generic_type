package com.games.garrett.generictype;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;



public class GameTwoActivity extends AppCompatActivity{

    private static final String TAG = GameTwoActivity.class.getCanonicalName();


    private ArrayList<String> book;                         // array to hold book
    private ArrayList<String> wordsInPassage;               // array to hold passage from book
    private ScrollingTextView passage;                      // a scrolling text view to display passage
    private CustomEdit userInput;                           // users input

    // these global variables are for dealing with colouring the passage
    private CharSequence passageWithTags;                   // a reference to the passage with html tags

    private final String GREEN_OPEN_TAG = "<font color=green>";
    private final String RED_OPEN_TAG = "<font color=red>";
    private final String CLOSE_TAG = "</font>";
    private final int GREEN_CHAR_LENGTH = GREEN_OPEN_TAG.length() + CLOSE_TAG.length();
    private final int RED_CHAR_LENGTH = RED_OPEN_TAG.length() + CLOSE_TAG.length();

    private boolean lastTagGreen = false;
    private int numGreenTags = 0;  // track green and red tags for indexing purposes
    private int numRedTags = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_two);

        // extract difficulty from starting intent
        final Integer difficulty = getIntent().getIntExtra("difficulty", 0);

        // create a random passage from a book and store it as an arraylist of strings
        String bookFileName = "brothers_karamazov.txt"; // name of file were book text is stored
        createBook(bookFileName);
        createPassage(difficulty);

        // set up scrolling text view text view
        passage = (ScrollingTextView) findViewById(R.id.passage);
        setPassageSpeed(difficulty, passage);
        passage.setText(TextUtils.join(" ", wordsInPassage));

        passageWithTags = passage.getText(); // get the text as a character sequence

        userInput = (CustomEdit) findViewById(R.id.userInputTwo);
        setUpTextChangedListener(userInput);

    }


    /**
     * Continue to check user input and and update the text view such that:
     *  characters that are correct are green
     *  characters that are incorrect are red
     *  characters yet to be reached remain white
     * @param editText
     */
    private void setUpTextChangedListener(CustomEdit editText){

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // TODO : Create a speacial caes for erasing spaces. Right now it doesn't register as a charcter
                Log.d(TAG, "onTextChanged BEFORE TEXT CHANGED" +
                        "  start = " + i +
                        "  before = " + i1 +
                        "  count = " + i2 +
                        "  characterSequence = " + charSequence);

                int inputLength =  i + i1; // i1 + i2 = charSequence.length where the char sequence is the input before change
                int offset = numGreenTags*(GREEN_CHAR_LENGTH) + numRedTags*(RED_CHAR_LENGTH);

                if ( i1 > i2 ){ // a backspace occurs
                    Log.d(TAG, "a back space has occured; user is erasing : " +  charSequence.charAt(inputLength-1) + " corresponding to text's " + passageWithTags.charAt(inputLength -1 + offset - CLOSE_TAG.length()));
                    if ( charSequence.charAt(inputLength-1) == passageWithTags.charAt(inputLength -1 + offset - CLOSE_TAG.length())){
                        //if ( charSequence.charAt(inputLength-1) == ' ' ) inputLength++; // textwatcher won't add for spaces so must compensate.
                        //remove green tag:
                        Log.d(TAG, "REMOVING TAG INPUT LENGTH = " + inputLength + "\n " +
                                passageWithTags.subSequence(0, inputLength -1 + offset - CLOSE_TAG.length() - GREEN_OPEN_TAG.length()) + "" + "\n " +
                                passageWithTags.subSequence(inputLength -1 + offset - CLOSE_TAG.length(), inputLength -1 + offset - CLOSE_TAG.length()+ 1 ) + "" + "\n " +
                                passageWithTags.subSequence(inputLength -1 + offset - CLOSE_TAG.length()+ 1 + CLOSE_TAG.length(), passageWithTags.length()));

                        passageWithTags = passageWithTags.subSequence(0, inputLength -1 + offset - CLOSE_TAG.length() - GREEN_OPEN_TAG.length()) + "" +
                                passageWithTags.subSequence(inputLength -1 + offset - CLOSE_TAG.length(), inputLength -1 + offset - CLOSE_TAG.length()+ 1 ) + "" +
                                passageWithTags.subSequence(inputLength -1 + offset - CLOSE_TAG.length()+ 1 + CLOSE_TAG.length(), passageWithTags.length());

                        passage.setText(Html.fromHtml(passageWithTags.toString(), Html.FROM_HTML_MODE_LEGACY));
                        numGreenTags--;
                    }else{
                       // if ( charSequence.charAt(inputLength-1) == ' ' ) inputLength++; // textwatcher won't add for spaces so must compensate.
                        //remove red tag:
                        Log.d(TAG, "REMOVING TAG INPUT LENGTH = " + inputLength + "\n " +
                                passageWithTags.subSequence(0, inputLength -1 + offset - CLOSE_TAG.length() - RED_OPEN_TAG.length()) + "" + "\n " +
                                passageWithTags.subSequence(inputLength -1 + offset - CLOSE_TAG.length(), inputLength -1 + offset - CLOSE_TAG.length()+ 1 ) + "" + "\n " +
                                passageWithTags.subSequence(inputLength -1 + offset - CLOSE_TAG.length()+ 1 + CLOSE_TAG.length(), passageWithTags.length()));

                        passageWithTags = passageWithTags.subSequence(0, inputLength -1 + offset - CLOSE_TAG.length() - RED_OPEN_TAG.length()) + "" +
                                passageWithTags.subSequence(inputLength -1 + offset - CLOSE_TAG.length(), inputLength -1 + offset - CLOSE_TAG.length()+ 1 ) + "" +
                                passageWithTags.subSequence(inputLength -1 + offset - CLOSE_TAG.length()+ 1 + CLOSE_TAG.length(), passageWithTags.length());

                        passage.setText(Html.fromHtml(passageWithTags.toString(), Html.FROM_HTML_MODE_LEGACY));
                        numRedTags--;
                    }
                }

            }

            /**
             * This method will handle updating the textView based on the users input
             * @param charSequence The current user's input.
             * @param i  start
             * @param before before
             * @param i2 count
             */
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int before, int i2) {
                Log.d(TAG, "onTextChanged ON TEXT CHANGED" +
                        "  start = " + i +
                        "  before = " + before +
                        "  count = " + i2 +
                        "  characterSequence = " + charSequence);
                // the user back spaced if i1 < i2+i
                int inputLength = i + i2;
                // total number of characters that colour tags take up
                int offset = numGreenTags*(GREEN_CHAR_LENGTH) + numRedTags*(RED_CHAR_LENGTH);
                // before + < count == > inserting ; otherwise deleting
                if ( before  < i2 ){ // insertion of a new letter
                    checkInput(charSequence, inputLength, offset);
                }
                else{
                    // remove last tags happens on BEOFRE TEXT CHANGE so the character
                    // erased can be checkec to determine if it was correct or not
                }

            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void removeLastTag(CharSequence userInput, int inputLength, int offset){

    }

    // compare the user's last inserted letter with the text
    private void checkInput( CharSequence userInput, int inputLength, int offset){
        // if the input letter matches the text ==== > colour letter green


        Log.d(TAG, " input Length = " + inputLength + "\n" +
                        passageWithTags.subSequence(0, inputLength - 1 + offset) + "\n" +
                        GREEN_OPEN_TAG + "\n" +
                        passageWithTags.subSequence(inputLength - 1 + offset ,inputLength - 1 + offset + 1 ) + "\n" +
                        CLOSE_TAG + "\n" +
                        passageWithTags.subSequence(inputLength - 1 + offset + 1, passageWithTags.length()));

        Log.d(TAG, "Comparing user input : " + userInput.charAt(inputLength-1) + " with the text's : " +  passageWithTags.charAt(inputLength - 1 + offset));
        if ( userInput.charAt(inputLength-1) == passageWithTags.charAt(inputLength - 1 + offset)){
            passageWithTags = passageWithTags.subSequence(0, inputLength - 1 + offset) +
                               GREEN_OPEN_TAG +
                               passageWithTags.subSequence(inputLength - 1 + offset ,inputLength - 1 + offset + 1 ) +
                               CLOSE_TAG +
                               passageWithTags.subSequence(inputLength - 1 + offset + 1, passageWithTags.length());

            passage.setText(Html.fromHtml(passageWithTags.toString(), Html.FROM_HTML_MODE_LEGACY));
            numGreenTags++;

        }else{ // input doesn't match text === > colour letter red
            Log.d(TAG, " input Length = " + inputLength + "\n" +
                    passageWithTags.subSequence(0, inputLength - 1 + offset) + "\n" +
                    RED_OPEN_TAG + "\n" +
                    passageWithTags.subSequence(inputLength - 1 + offset ,inputLength - 1 + offset + 1 ) + "\n" +
                    CLOSE_TAG + "\n" +
                    passageWithTags.subSequence(inputLength - 1 + offset + 1, passageWithTags.length()));

                passageWithTags = passageWithTags.subSequence(0, inputLength - 1 + offset) +
                        RED_OPEN_TAG +
                        passageWithTags.subSequence(inputLength - 1 + offset, inputLength - 1 + offset + 1) +
                        CLOSE_TAG +
                        passageWithTags.subSequence(inputLength - 1 + offset + 1, passageWithTags.length());

                passage.setText(Html.fromHtml(passageWithTags.toString(), Html.FROM_HTML_MODE_LEGACY));
                numRedTags++;
        }

    }


    private void setPassageSpeed(int difficulty, ScrollingTextView stv) {
        switch (difficulty) {
            case 0:
                stv.setSpeed(80);
                break;
            case 1:
                stv.setSpeed(60);
                break;
            default:
                stv.setSpeed(40);
                break;

        }

    }

    /**
     * Facotry method for creating an intent to start this activity.
     * @param context the starting activitys' context
     * @return an intent that can start this activity
     */
    public static Intent makeGameTwoActivityIntent(Context context){

        return new Intent(context, GameTwoActivity.class);
    }


    /**
     * Creates an array list of a random passage.
     * The length is determined by the difficulty selection.
     * @param difficulty difficulty selected
     */
    private void createPassage(Integer difficulty){
        Log.d( TAG, "createPassage() called.");
        this.wordsInPassage = new ArrayList<>();
        Integer passageLength;
        Integer increment = 0 ; // used to add more words if necessary
        switch ( difficulty ){
            case 0 :
                passageLength = 10;
                break;
            case 1 :
                passageLength = 20;
                break;
            default:
                passageLength = 30;
                break;
        }

        // add at least the passageLength, then keep going until
        // word with termination punctuation is found i.e. . ! ? etc.
        Random rd = new Random();  // create a new random generator
        Integer passageStart = rd.nextInt(book.size() - passageLength); // random starting point

        // fill up wordsInPassage with at least the required length
        // note the range of sub list is [ .. ) i.e. inclusive start , exclusive end
        this.wordsInPassage.addAll(this.book.subList(passageStart, passageStart + passageLength));

        // keep checking the end word to see if it ends in punctuation.
        // the regex here is:
        // . check any character
        // * for zero or more occurances of
        // [.?!] the list of charcters to check for.
        while ( !Pattern.matches(".*[.?!]", wordsInPassage.get(wordsInPassage.size()-1))){
            wordsInPassage.add((book.get(passageStart + passageLength + increment)).trim());
            increment++;
        }
        // add final word.
        wordsInPassage.add((book.get(passageStart + passageLength + increment)).trim());

        Log.d( TAG, "passage filled with " + wordsInPassage.size() + " words.");
    }

    /**
     * Creates an ArrayList of words in a book.
     * @param bookFileName THe name of the text file containing the book.
     */
    private void createBook(String bookFileName){
        Log.d( TAG, "createBook() called.");

        this.book = new ArrayList<>();

        BufferedReader br;
        AssetManager am = this.getAssets();

        try{
            // access the book from the assets folder
            br = new BufferedReader(new InputStreamReader(am.open(bookFileName)));
            String word;
            while ( (word = br.readLine()) != null){
                book.add(word);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        Log.d( TAG, "book filled with " + book.size() + " words.");

    }

}
