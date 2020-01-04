package com.example.examapplikation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MySplashScreenActivity extends AppCompatActivity { // this class is for the Splashscreen displayed before app enters mainactivity

//#Region  SplashScreen


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // fulllscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_splash_screen); // set here to not set the view before i fullscreen it

        getSupportActionBar().hide(); // hide actionbar

        LogoForapplicationLauncher logoForapplicationLauncher = new LogoForapplicationLauncher(); // object of our class to start
        logoForapplicationLauncher.start();; // show logo
    }
    private class LogoForapplicationLauncher extends Thread{ // sets the logo timer
        public void run(){
            try{
                sleep(1000 * 3);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            // manually loads main after few secs
            Intent intent = new Intent(MySplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
            MySplashScreenActivity.this.finish();
        }
    }
}

//#EndRegion
