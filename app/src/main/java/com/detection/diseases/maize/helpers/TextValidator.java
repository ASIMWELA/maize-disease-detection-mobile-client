package com.detection.diseases.maize.helpers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * @author Augustine
 *
 * A  Custom text validator .
 *
 * Helps in live validation of user inputs on EditText and also live searching
 */
public abstract class TextValidator implements TextWatcher {
    /**
     * Constructor for creating object of this class
     *
     * @param editText Attach the validator this text field
     */
    public TextValidator(EditText editText) {
    }

    /**
     * Custom method to be implemented for validating inputs
     */
    public abstract void validate();

    @Override
    final public void afterTextChanged(Editable s) {

        validate();
    }

    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* Don't care */ }

    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) { /* Don't care */ }
}