package com.example.marvin.ecommerce.HelperClass;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class InputValidation
{
    Context context;

    public InputValidation(Context context) {
        this.context = context;
    }

    /**method to check InputText field***/

    public boolean isInputEditTextField(TextInputEditText textInputEditText,
                                        TextInputLayout textInputLayout, String message)
    {
      String value =textInputEditText.getText().toString().trim();
        if (value.isEmpty()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public boolean isInputEditTextMatches(TextInputEditText textInputEditText1,
                                          TextInputEditText textInputEditText2, TextInputLayout textInputLayout, String message) {
        String value1 = textInputEditText1.getText().toString().trim();
        String value2 = textInputEditText2.getText().toString().trim();
        if (!value1.contentEquals(value2)) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText2);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }




    private void hideKeyboardFrom(View view)
    {
        InputMethodManager imm= (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);

        if (imm !=null)
        {
            imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }
}
