package com.games.garrett.generictype;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * A custom text with onKeyPreIme method. This allows for the user's input
 * to be taken as they type, rather than waiting for them to press send.
 *
 * Created by Garrett on 3/28/2017.
 */

public class CustomEdit extends android.support.v7.widget.AppCompatEditText {

    private static final String TAG = CustomEdit.class.getCanonicalName();

    public CustomEdit(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        Log.e(TAG, "onKeyPrime");

        return true;
    }
}
