package kz.nis.economy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by myrza on 10/16/15.
 */
public class SplashActivity extends Activity{

    private static final int SPLASH_TIME_OUT = 1200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                finish();
                Intent i =new Intent(SplashActivity.this, Main.class);
                startActivity( i);
                overridePendingTransition(0,0);
            }
        },SPLASH_TIME_OUT);
    }
}
