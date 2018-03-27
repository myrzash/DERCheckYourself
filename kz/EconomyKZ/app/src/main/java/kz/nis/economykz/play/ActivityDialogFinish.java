package kz.nis.economykz.play;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import kz.nis.economykz.R;

public class ActivityDialogFinish extends Activity implements View.OnTouchListener{

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_finish);
        ImageView imageTitleTop = (ImageView) findViewById(R.id.imageTitleTop);
        ImageView imageTitleBottom = (ImageView) findViewById(R.id.imageTitleBottom);

        if (getIntent().getAction().equals("top")) {
            imageTitleTop.setImageResource(R.mipmap.eng_win);
            imageTitleBottom.setImageResource(R.mipmap.eng_game_over);
        } else {
            imageTitleTop.setImageResource(R.mipmap.eng_game_over);
            imageTitleBottom.setImageResource(R.mipmap.eng_win);
        }
        mediaStart();
        findViewById(R.id.imageMainMenu).setOnTouchListener(this);
        findViewById(R.id.imageRestart).setOnTouchListener(this);
        findViewById(R.id.imageMainMenu2).setOnTouchListener(this);
        findViewById(R.id.imageRestart2).setOnTouchListener(this);
    }

    private void mediaStart() {
        mediaPlayer = MediaPlayer.create(this, R.raw.battle_win);
        mediaPlayer.start();
    }

    @Override
    public void onPause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying())
            mediaPlayer.pause();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
    }

    public void onClickMainMenu() {
        setResult(FragmentPlayer.RESULT_CODE_MAIN, new Intent());
        finish();
    }

    public void onClickRestart() {
        setResult(FragmentPlayer.RESULT_CODE_RESTART, new Intent());
        finish();
    }

    private boolean click = false;

    @SuppressLint("NewApi")
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int eventaction = event.getAction();

        switch (eventaction) {
            case MotionEvent.ACTION_DOWN:
                click = true;
                v.setScaleX(0.8f);
                v.setScaleY(0.8f);
                break;
            case MotionEvent.ACTION_MOVE:
                int left = -20;
                int right = v.getWidth() + 20;
                int top = -20;
                int bottom = v.getHeight() + 20;
                if (left >= event.getX() || event.getX() >= right || top >= event.getY() || event.getY() >= bottom) {
                    v.setScaleX(1f);
                    v.setScaleY(1f);
                    click = false;
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (click) {
                    switch (v.getId()) {
                        case R.id.imageMainMenu:
                            onClickMainMenu();
                            break;
                        case R.id.imageRestart:
                            onClickRestart();
                            break;
                        case R.id.imageMainMenu2:
                            onClickMainMenu();
                            break;
                        case R.id.imageRestart2:
                            onClickRestart();
                            break;
                    }
                }
                v.setScaleX(1f);
                v.setScaleY(1f);
                click = false;
                break;
        }

        return true;
    }
}

