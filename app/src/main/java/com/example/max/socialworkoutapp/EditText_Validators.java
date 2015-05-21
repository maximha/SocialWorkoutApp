package com.example.max.socialworkoutapp;

import java.util.regex.Pattern;

import android.widget.EditText;

public class EditText_Validators {

    private static final String REQUIRED_MSG = "required";
    private static final String PASSWORD_MSG = "invalid password";
    private static final String NAME_MSG = "invalid name";
    private static final String NAME_REGEX = "^[a-zA-Z]+$";
    private static final String PASSWORD_REGEX = "^[a-zA-Z]\\w{3,14}$";


    public static boolean isName(EditText editText, boolean required)
    {
        return isValid(editText, NAME_REGEX, NAME_MSG, required);
    }

    public static boolean isPassword(EditText editText, boolean required)
    {
        return isValid(editText, PASSWORD_REGEX, PASSWORD_MSG, required);
    }

    public static boolean isConfirm(EditText editText1, EditText editText2)
    {
        if(!editText1.getText().toString().equals(editText2.getText().toString()))
        {
            editText2.setError("incorrect password");
            return false ;
        }
        return true;
    }
    // return true if the input field is valid, based on the parameter passed
    public static boolean isValid(EditText editText, String regex, String errMsg, boolean required) {

        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);

        // text required and editText is blank, so return false
        if ( required && !hasText(editText) ) return false;

        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            editText.setError(errMsg);
            return false;
        }

        return true;
    }
    // check the input field has any text or not
    // return true if it contains text otherwise false
    public static boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError(REQUIRED_MSG);
            return false;
        }

        return true;
    }
}

