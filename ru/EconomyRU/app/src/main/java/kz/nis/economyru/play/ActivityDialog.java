package kz.nis.economyru.play;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import kz.nis.economyru.R;

public class ActivityDialog extends Activity implements View.OnTouchListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        findViewById(R.id.imageMainMenu).setOnTouchListener(this);
        findViewById(R.id.imageRestart).setOnTouchListener(this);
        findViewById(R.id.imageContinue).setOnTouchListener(this);
        findViewById(R.id.imageMainMenu2).setOnTouchListener(this);
        findViewById(R.id.imageRestart2).setOnTouchListener(this);
        findViewById(R.id.imageContinue2).setOnTouchListener(this);
    }

    public void onClickMainMenu() {
        setResult(FragmentPlayer.RESULT_CODE_MAIN, new Intent());
        finish();
    }

    public void onClickRestart() {
        setResult(FragmentPlayer.RESULT_CODE_RESTART, new Intent());
        finish();
    }

    public void onClickContinue() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
                        case R.id.imageContinue:
                            onClickContinue();
                            break;
                        case R.id.imageMainMenu2:
                            onClickMainMenu();
                            break;
                        case R.id.imageRestart2:
                            onClickRestart();
                            break;
                        case R.id.imageContinue2:
                            onClickContinue();
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

