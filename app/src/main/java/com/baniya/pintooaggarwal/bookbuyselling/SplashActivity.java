package com.baniya.pintooaggarwal.bookbuyselling;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static java.lang.Thread.sleep;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

//                SharedPreferences getprefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//                boolean isFirstStart =  getprefs.getBoolean("firstStart",true);
//                if(isFirstStart)
//                {
//                    startActivity(new Intent(SplashActivity.this , IntroOfApp.class));
//                    SharedPreferences.Editor e = getprefs.edit();
//                    e.putBoolean("firstStart",false);
//                    e.apply();
//                }
                try {
                    sleep(2000);
                    Intent intent = new Intent(getApplicationContext(), StartingActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();




//        Thread mythread = new Thread() {
//            @Override
//            public void run() {
//
//            }
//        };
//        mythread.start();

    }
}