package com.wzx.base.baseframe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.wzx.base.aspect.net.CheckNet;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dosth();
    }
    @CheckNet
    public void dosth(){
        Log.e("TAG","--------正常");
    }
}
