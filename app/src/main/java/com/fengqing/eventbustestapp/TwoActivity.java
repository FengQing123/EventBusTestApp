package com.fengqing.eventbustestapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fengqing.buslibrary.EventBus;

/**
 * @author fengqing
 * @date 2019/6/10
 */

public class TwoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
    }

    public void postTesttwo(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                EventBus.getEventBus().post("from two");
            }
        }).start();
    }
}
