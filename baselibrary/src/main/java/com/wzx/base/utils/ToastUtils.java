package com.wzx.base.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    public static void showToast(Context context,CharSequence text){
        Toast.makeText(context,text,Toast.LENGTH_LONG).show();
    }
}
