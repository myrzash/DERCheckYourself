package kz.nis.economyru;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class Main extends Activity {

    public static final String LOG = "Economy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageTitle = (ImageView) findViewById(R.id.imageCoverTitle);
        imageTitle.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_alpha_repeat));
        ImageView imageOnePlayer = (ImageView) findViewById(R.id.imageOnePlayer);
        ImageView imageDualPlayer = (ImageView) findViewById(R.id.imageDualPlayer);
        ImageView imageEdit = (ImageView) findViewById(R.id.imageEdit);
        imageOnePlayer.setOnTouchListener(new mOnTouchListener());
        imageDualPlayer.setOnTouchListener(new mOnTouchListener());
        imageEdit.setOnTouchListener(new mOnTouchListener());
    }

    private static long back_pressed;

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else
            Toast.makeText(getBaseContext(), R.string.toast_exit,
                    Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
    }

    private class mOnTouchListener implements View.OnTouchListener {
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
                            case R.id.imageOnePlayer:
                                startActivity(new Intent(Main.this, ActivityParts.class).putExtra("from", ActivityParts.PART_CODE_PLAY1));
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                break;
                            case R.id.imageDualPlayer:
                                startActivity(new Intent(Main.this, ActivityParts.class).putExtra("from", ActivityParts.PART_CODE_PLAY2));
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                break;
                            case R.id.imageEdit:
                                startActivity(new Intent(Main.this, ActivityIdentif.class));
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
}
