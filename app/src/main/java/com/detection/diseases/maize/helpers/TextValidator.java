package com.detection.diseases.maize.helpers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public abstract class TextValidator implements TextWatcher {
    private final EditText editText;
    public TextValidator(EditText editText) {
        this.editText = editText;
    }
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