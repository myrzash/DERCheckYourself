package kz.nis.economykz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import kz.nis.economykz.R;
import kz.nis.economykz.extra.FontFactory;

public class ActivityIdentif extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identif);

        Typeface typeface = FontFactory.getFont1(this);
        TextView  textVerified = (TextView) findViewById(R.id.textVerification);
        textVerified.setTypeface(typeface);
        TextView  textVerified2 = (TextView) findViewById(R.id.textBottomVerification);
        textVerified2.setTypeface(typeface);

       new Thread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.imageScan).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_scan));
            }
        }).start();

        findViewById(R.id.imageFingerDetecor).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new TaskVerification().execute();
                return true;
            }
        });


        TextView  home = (TextView) findViewById(R.id.textStartGame);
        home.setTypeface(typeface);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityIdentif.this, Main.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

    }


    //    public void onClickTwoPlayer() {
//        startActivity(new Intent(Main.this, ActivityGameTwo.class));
//        overridePendingTransition(0,0);
//    }
    private class TaskVerification extends AsyncTask<Void, String, Void> {

        private TextView textVerified;

        @Override
        protected void onPreExecute() {
            textVerified = (TextView) findViewById(R.id.textVerification);
            textVerified.setText(R.string.identification_verified);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                for (int i = 0; i < 3; i++) {
                    TimeUnit.MILLISECONDS.sleep(200);
                    publishProgress(".");
                }
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            textVerified.append(".");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            startActivity(new Intent(ActivityIdentif.this, ActivityParts.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            super.onPostExecute(aVoid);
        }
    }
}

