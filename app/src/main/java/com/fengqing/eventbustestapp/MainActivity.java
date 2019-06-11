package com.fengqing.eventbustestapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.fengqing.buslibrary.EventBus;
import com.fengqing.buslibrary.Subscribe;
import com.fengqing.buslibrary.ThreadMode;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getEventBus().register(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getString(String s) {
        Log.e(TAG, "in " + Thread.currentThread().getName() + " thread happen s=" + s);
    }

    @Override
    protected void onDestroy() {
        EventBus.getEventBus().unregister(this);
        super.onDestroy();
    }

    public void postTest(View view) {
        EventBus.getEventBus().post("msg");
    }

    public void toNext(View view) {
        startActivity(new Intent(this, TwoActivity.class));
    }


}
