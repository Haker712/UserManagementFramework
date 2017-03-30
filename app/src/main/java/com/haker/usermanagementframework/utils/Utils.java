package com.haker.usermanagementframework.utils;

/**
 * Created by haker on 3/29/17.
 */

public class Utils {
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
