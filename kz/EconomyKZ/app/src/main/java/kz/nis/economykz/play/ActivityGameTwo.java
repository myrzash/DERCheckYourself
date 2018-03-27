package kz.nis.economykz.play;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.*;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import kz.nis.economykz.R;

public class ActivityGameTwo extends FragmentActivity {

    private static final float scale = 0.75f;
    private static final float deltaScaleHero = 0.08f;
    public static float scaleFlame = 1.0f;
    public static float scaleBo = 1.0f;
    private static final long duration_scale = 1000;
    private static RelativeLayout containerLeftBo;
    private static RelativeLayout containerFlame;

    private ImageView imageBo;
    private ImageView imageFlame;
    private static View imageHero;
    private static View imageDino;
    private AnimationDrawable leftBoAnimation;
    private AnimationDrawable flameAnimation;
    private CheckBox checkBoxSound;
    private CheckBox checkBoxSound2;
    private MediaPlayer mediaPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_two);
        scaleFlame = 1.0f;
        scaleBo = 1.0f;
        initViews();
        mediaStart();
    }

    private void mediaStart() {
        mediaPlayer = MediaPlayer.create(ActivityGameTwo.this, R.raw.music_play);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        if (mediaPlayer != null)
            mediaPlayer.setVolume(0.4f, 0.4f);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.stop();
        if (mediaPlayer != null)
            mediaPlayer = null;
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        if (mediaPlayer != null)
            mediaPlayer.setVolume(1f, 1f);
        super.onResume();
    }

    public static void animFlameBo(String fragmentTag) {
        if (fragmentTag.equals("bottom")) {
            scaleBo = scaleBo(containerLeftBo, scaleBo, deltaScaleHero);
            scaleFlame = scaleFlame(containerFlame, scaleFlame, -deltaScaleHero);
        }
        if (fragmentTag.equals("top")) {
            scaleFlame = scaleFlame(containerFlame, scaleFlame, deltaScaleHero);
            scaleBo = scaleBo(containerLeftBo, scaleBo, -deltaScaleHero);
        }
    }

    private static float scaleBo(final View v, final float startScale, final float deltaScale) {
        float endScale = startScale + deltaScale;
        Animation scaleAnim = new ScaleAnimation(
                startScale, endScale,// Start and end values for the X axis scaling
                startScale * scale, endScale * scale,// Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        scaleAnim.setFillAfter(true); // Needed to keep the result of the animation
        scaleAnim.setDuration(duration_scale);
        v.startAnimation(scaleAnim);
        return endScale;
    }

    private static float scaleFlame(final View v, final float startScale, final float deltaScale) {
        float endScale = startScale + deltaScale;
        Animation scaleAnim = new ScaleAnimation(
                startScale, endScale,// Start and end values for the X axis scaling
                startScale * scale, endScale * scale,// Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 1f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        scaleAnim.setFillAfter(true); // Needed to keep the result of the animation
        scaleAnim.setDuration(duration_scale);
        v.startAnimation(scaleAnim);
        return endScale;
    }

    @SuppressLint("NewApi")
    protected static void translate(String fragmentTag) {

        View hiddenView = fragmentTag.equals("bottom") ? imageDino : imageHero;
        hiddenView.setVisibility(View.INVISIBLE);
        View hiddenView2 = fragmentTag.equals("bottom") ? containerFlame : containerLeftBo;
        hiddenView2.setVisibility(View.INVISIBLE);
        View v = fragmentTag.equals("bottom") ? containerLeftBo : containerFlame;
        float fromX =0f;
        float toX = fragmentTag.equals("bottom") ? (fromX+20f):(fromX-20f);
        float scaleView = fragmentTag.equals("bottom") ? scaleBo : scaleFlame;
        float pivotX = fragmentTag.equals("bottom") ? 0f : 1f;
        AnimationSet as = new AnimationSet(true);
        as.setFillEnabled(true);
        as.setFillAfter(true);
        as.setInterpolator(new AccelerateDecelerateInterpolator());
        TranslateAnimation ta = new TranslateAnimation(fromX, toX, 0, 0);
        ta.setDuration(duration_scale);
        as.addAnimation(ta);
        Animation scaleAnim = new ScaleAnimation(
                scaleView, scaleView,// Start and end values for the X axis scaling
                scaleView * scale, scaleView * scale,// Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, pivotX, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        scaleAnim.setFillAfter(true);
        scaleAnim.setDuration(duration_scale);
        as.addAnimation(scaleAnim);

        v.startAnimation(as);
    }

    @Override
    public void onBackPressed() {}

    private CompoundButton.OnCheckedChangeListener onCheckedChanged = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            checkBoxSound.setChecked(isChecked);
            checkBoxSound2.setChecked(isChecked);
//
            if (mediaPlayer == null)
                return;
            if (isChecked) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
            } else {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            }
        }
    };

    private void initViews() {
        imageHero = (ImageView) findViewById(R.id.imageHero);
        imageDino = (ImageView) findViewById(R.id.imageDino);
        containerLeftBo = (RelativeLayout) findViewById(R.id.containerLeftBo);
        containerFlame = (RelativeLayout) findViewById(R.id.containerFlame);
        imageBo = (ImageView) findViewById(R.id.imageViewBo);
        imageFlame = (ImageView) findViewById(R.id.imageViewFlame);
        if (leftBoAnimation == null) leftBoAnimation = (AnimationDrawable) imageBo.getBackground();
        leftBoAnimation.start();
        if (flameAnimation == null) flameAnimation = (AnimationDrawable) imageFlame.getBackground();
        flameAnimation.start();

        checkBoxSound = (CheckBox) findViewById(R.id.checkBoxSound);
        checkBoxSound.setOnCheckedChangeListener(onCheckedChanged);
        checkBoxSound2 = (CheckBox) findViewById(R.id.checkBoxSound2);
        checkBoxSound2.setOnCheckedChangeListener(onCheckedChanged);
    }
}
