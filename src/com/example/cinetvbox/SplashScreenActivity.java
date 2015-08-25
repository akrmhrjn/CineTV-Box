package com.example.cinetvbox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class SplashScreenActivity extends Activity 
{
 
    private boolean mIsBackButtonPressed;
    private static final int SPLASH_DURATION = 3000; //3 seconds
    private Handler myhandler;
    final Context context = this;
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
 
        myhandler = new Handler();
 
        // run a thread to start the home screen
        myhandler.postDelayed(new Runnable() {
 
            @Override
            public void run() 
            {
 
               finish();
                
               if (!mIsBackButtonPressed)
               {
                    // start the home activity 
            	   Intent intent = new Intent(SplashScreenActivity.this, Login.class);
                   SplashScreenActivity.this.startActivity(intent);
                    
               }
                 
            }
 
        }, SPLASH_DURATION); 
    }
    
   
    //handle back button press
    @Override
    public void onBackPressed() 
    {
        mIsBackButtonPressed = true;
        super.onBackPressed();
    }
    
    
    
}