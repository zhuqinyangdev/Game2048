package com.example.game2048;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler x = new Handler();
        x.postDelayed(new splashhandler(), 2000);
    }
    class splashhandler implements Runnable{
        public void run() {
            startActivity(new Intent(getApplication(),MainActivity.class));
            SplashActivity.this.finish();
        }
    }
}
