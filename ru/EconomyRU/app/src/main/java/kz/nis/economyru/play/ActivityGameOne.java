package kz.nis.economyru.play;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import kz.nis.economyru.Main;
import kz.nis.economyru.R;
import kz.nis.economyru.db.DBAdapter;
import kz.nis.economyru.extra.FontFactory;

public class ActivityGameOne extends FragmentActivity {
    private static final short HALF = 10;
    private static final long durationTranslate = 800;
    private static final float deltaScaleHero = 0.08f;
    private static final float deltaScaleDino = 0.08f;
    private static final long duration_scale = 1000;
    private float scaleBo = 1.0f;
    private float scaleFlame = 1.0f;
    private static final String TAG_WIN = "win";
    private static final String TAG_LOSE = "lose";
    private static final float scale = 0.75f;
    private static int[] PASSED = new int[HALF];
    private static int partId;
    int[] idCards = new int[]{R.id.btn1_1, R.id.btn1_2, R.id.btn1_3,
            R.id.btn1_4, R.id.btn1_5, R.id.btn1_6, R.id.btn1_7, R.id.btn1_8,
            R.id.btn1_9, R.id.btn1_10, R.id.btn1_11, R.id.btn1_12,
            R.id.btn1_13, R.id.btn1_14, R.id.btn1_15, R.id.btn1_16,
            R.id.btn1_17, R.id.btn1_18, R.id.btn1_19, R.id.btn1_20};
    private View lastView;
    private short PROGRESS;
    private String[][] DATAS;
    private Card[] cards;
    private int CURRENT = 0;//new Random().nextInt(20);

    private MediaPlayer mediaPlayer = null;
    private ImageView imageHero;
    private ImageView imageDino;
    private ImageView translateImage1;
    private ImageView translateImage2;
    private ImageView imageBo;
    private ImageView imageFlame;
    private AnimationDrawable leftBoAnimation;
    private AnimationDrawable flameAnimation;
    private RelativeLayout containerLeftBo;
    private RelativeLayout containerFlame;
    private CheckBox checkBoxSound;
    private Typeface typeface;
    private DialogExit dialogExit;
    private DialogFinish dialogFinish;
    private FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_one);
        PROGRESS = 0;
        lastView = null;
        partId = getIntent().getIntExtra("partId", 1);
        DATAS = getDatas();
        if (DATAS.length < HALF * 2) {
            Toast.makeText(getApplicationContext(), R.string.not_enough, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        typeface = FontFactory.getFont1(getApplicationContext());
        findViewById(R.id.relativeLayoutContainer).setVisibility(View.INVISIBLE);
        initViews();
        timer();

        int[] tags = new int[idCards.length];
        for (int i = 0; i < idCards.length; i++) {
            tags[i] = i;
        }
        cards = initCards(tags);
        setDatasForViews(cards, tags);

        mediaStart();
        dialogExit = new DialogExit(mediaPlayer);
        dialogFinish = new DialogFinish();

    }

    @Override
    protected void onPause() {
        Log.d(Main.LOG, "onPause");
        if (mediaPlayer != null)
            mediaPlayer.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(Main.LOG, "onResume");
        if (mediaPlayer != null)
            mediaPlayer.start();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        dialogExit.show(fm, null);
    }

    private void showDialogFinish(String whoWin) {
        switch (whoWin) {
            case TAG_WIN:
                imageDino.setImageResource(R.mipmap.dinasour_down);
                break;
            case TAG_LOSE:
                imageHero.setImageResource(R.mipmap.left_hero_down);
                break;
        }
        if (mediaPlayer != null)
            mediaPlayer.stop();
        dialogFinish.show(fm, whoWin);
    }

    private void mediaStart() {
        mediaPlayer = MediaPlayer.create(ActivityGameOne.this, R.raw.music_play);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    private void timer() {
        CountDownTimer timer = new CountDownTimer(3200, 800) {
            int[] count = new int[]{R.mipmap.countdown_one, R.mipmap.countdown_two, R.mipmap.countdown_three};
            ImageView imageCount = (ImageView) findViewById(R.id.imageViewCounter);

            @Override
            public void onTick(long millisUntilFinished) {
                short i = (short) (millisUntilFinished/800  - 1);
                imageCount.setImageResource(count[i]);
            }

            @Override
            public void onFinish() {
                imageCount.setVisibility(View.INVISIBLE);
                TimeManager timeDialogHero = new TimeManager(getApplicationContext(), (TextView) findViewById(R.id.textViewDialogLeft),
                        (TextView) findViewById(R.id.textViewDialogRight));
                timeDialogHero.start();

                findViewById(R.id.relativeLayoutContainer).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_from_bottom));
            }
        };
        timer.start();
    }

    private String[][] getDatas() {
        DBAdapter dbAdapter = new DBAdapter(this);
        try {
            dbAdapter.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Cursor cursor = dbAdapter.getQuestAnswer(partId);
        String[][] datas = new String[cursor.getCount()][2];
        for (int i = 0; i < cursor.getCount(); i++) {
            for (int k = 0; k < 2; k++) {
                datas[i][k] = cursor.getString(k);
            }
            cursor.moveToPrevious();
        }
        Log.d(Main.LOG, "datas.length = " + datas.length);
        dbAdapter.close();
        return datas;
    }

    private void initViews() {
        imageHero = (ImageView) findViewById(R.id.imageHero);
        imageDino = (ImageView) findViewById(R.id.imageDino);
        translateImage1 = (ImageView) findViewById(R.id.buttonCarte1);
        translateImage2 = (ImageView) findViewById(R.id.buttonCarte2);
        containerLeftBo = (RelativeLayout) findViewById(R.id.containerLeftBo);
        containerFlame = (RelativeLayout) findViewById(R.id.containerFlame);
        imageBo = (ImageView) findViewById(R.id.imageViewBo);
        imageFlame = (ImageView) findViewById(R.id.imageViewFlame);
//        ANIMATIONS
//        imageHero.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_left));
//        imageDino.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right));
        if (leftBoAnimation == null) leftBoAnimation = (AnimationDrawable) imageBo.getBackground();
        leftBoAnimation.start();
        if (flameAnimation == null) flameAnimation = (AnimationDrawable) imageFlame.getBackground();
        flameAnimation.start();

        findViewById(R.id.buttonPause).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        CheckBox checkBoxSound = (CheckBox) findViewById(R.id.checkBoxSound);
        checkBoxSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
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
        });
    }

    public static int[] shuffle(int[] ar) {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
        return ar;
    }

    private Card[] initCards(int[] tags) {
        tags = shuffle(tags);
        Card[] cards = new Card[idCards.length];
        for (int i = 0; i < idCards.length; i++) {
            cards[i] = (Card) findViewById(idCards[i]);
            cards[i].setTag(tags[i]);
            cards[i].setTypeface(typeface);
            cards[i].setOnClickListener(new onClickListener());
        }
        return cards;
    }

    private void setDatasForViews(Card[] cards, int[] tags) {
        Log.d(Main.LOG, "CURRENT BEFORE: " + CURRENT);
        for (short i = 0; i < tags.length; i++) {
            short k = 0;
            for (short j = 0; j < cards.length; j++)
                if (((int) cards[j].getTag()) % HALF == tags[i]) {
                    cards[j].setText(DATAS[CURRENT % DATAS.length][k++]);
                    Log.d(Main.LOG, "->DATAS: ("+CURRENT+","+k+")");
                    if (k == 2) { CURRENT++; break;}
                }

        }

        Log.d(Main.LOG, "CURRENT AFTER: " + CURRENT);
    }

    private void addTrash(View v) {
        v.setEnabled(false);
        int tag = (int) v.getTag();
        Log.d(Main.LOG, "addTrash: " + "PROGRESS=" + PROGRESS + ",PASSED:" + tag);

        PASSED[PROGRESS++] = tag;
        if (PROGRESS == HALF) {
            PASSED = shuffle(PASSED);
            updateDatas(PASSED);
            PROGRESS = 0;
        }
    }

    private void updateDatas(int[] passed) {
        short k = 0;
        Card[] newCards = new Card[passed.length];
        for (int i = 0; i < cards.length; i++)
            if (!cards[i].isEnabled()) {
                cards[i].setTag(passed[k]);
                cards[i].setEnabled(true);
                newCards[k] = cards[i];
                k++;
            }
        setDatasForViews(newCards, passed);
    }

    @SuppressLint("NewApi")
    private void animStar(final View v, final View carte) {
        carte.setBackgroundResource(R.mipmap.star_score);

        AnimationSet as = new AnimationSet(true);
        as.setFillEnabled(true);
        as.setFillAfter(true);
        as.setInterpolator(new AccelerateDecelerateInterpolator());
        float startX = v.getX() + v.getWidth() / 3;
        float startY = v.getY();
        float finalX = imageHero.getX() + imageHero.getWidth() / 2;
        float finalY = imageHero.getY() + imageHero.getHeight() / 2;
        TranslateAnimation ta = new TranslateAnimation(startX, finalX, startY, finalY);
        ta.setDuration(durationTranslate);
        as.addAnimation(ta);
        ScaleAnimation scale = new ScaleAnimation(1f, 0f, 1f, 0f, ScaleAnimation.ZORDER_NORMAL, finalX, ScaleAnimation.ZORDER_NORMAL, finalY);
        scale.setDuration(durationTranslate);
        as.addAnimation(scale);

        carte.startAnimation(as);
    }


    @SuppressLint("NewApi")
    private void animFire(final View v, final View carte) {

        carte.setBackgroundResource(R.mipmap.wrong_cross);

        AnimationSet as = new AnimationSet(true);
        as.setFillEnabled(true);
        as.setFillAfter(true);
        as.setInterpolator(new AccelerateDecelerateInterpolator());
        float startX = v.getX() + v.getWidth() / 3;
        float startY = v.getY();
        float finalX = imageDino.getX() + imageDino.getWidth() / 2;
        float finalY = imageDino.getY() + imageDino.getHeight() / 2;
        TranslateAnimation ta = new TranslateAnimation(startX, finalX, startY, finalY);
        ta.setDuration(durationTranslate);
        as.addAnimation(ta);
        ScaleAnimation scale = new ScaleAnimation(1f, 0f, 1f, 0f, ScaleAnimation.ZORDER_NORMAL, finalX, ScaleAnimation.ZORDER_NORMAL, finalY);
        scale.setDuration(durationTranslate);
        as.addAnimation(scale);

        as.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                v.setSelected(false);
                CountDownTimer timer = new CountDownTimer(200, 200) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        carte.setBackgroundResource(R.mipmap.flame);
                    }
                };
                timer.start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                carte.setBackground(null);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        carte.startAnimation(as);
    }

    private float scaleBo(final View v, final float startScale, final float deltaScale) {
        float endScale = startScale + deltaScale;
        Animation anim = new ScaleAnimation(
                startScale, endScale, // Start and end values for the X axis scaling
                startScale * scale, endScale * scale,// Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDetachWallpaper(true);
        anim.setDuration(duration_scale);
        v.startAnimation(anim);
//        if (endScale < 0.1) showDialogHero();
        if (endScale < 0.05) showDialogFinish(TAG_LOSE);
        return endScale;
    }

    //
    private float scaleFlame(final View v, final float startScale, final float deltaScale) {
        float endScale = startScale + deltaScale;
        Animation anim = new ScaleAnimation(
                startScale, endScale,// Start and end values for the X axis scaling
                startScale * scale, endScale * scale,// Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 1f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(duration_scale);
        v.startAnimation(anim);
//        if (endScale < 0.1) showDialogDino();
        if (endScale < 0.05) showDialogFinish(TAG_WIN);
        return endScale;
    }

    class onClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (lastView == v) {
                lastView = null;
                v.setSelected(false);
                return;
            }
            if (lastView == null) {
                lastView = v;
                v.setSelected(true);
            } else {
                short tag1 = (short) (Short.parseShort(lastView
                        .getTag().toString()));
                short tag2 = (short) (Short.parseShort(v.getTag()
                        .toString()));
                lastView.setSelected(false);
                if (tag1 % HALF == tag2 % HALF) {
                    addTrash(lastView);
                    addTrash(v);
                    animStar(lastView, translateImage1);
                    animStar(v, translateImage2);
                    scaleBo = scaleBo(containerLeftBo, scaleBo, deltaScaleHero);
                    scaleFlame = scaleFlame(containerFlame, scaleFlame, -deltaScaleDino);
                } else if ((tag1 < 10 && tag2 >= 10) || (tag1 >= 10 && tag2 < 10)) {
                    animFire(lastView, translateImage1);
                    animFire(v, translateImage2);
                    scaleBo = scaleBo(containerLeftBo, scaleBo, -deltaScaleHero);
                    scaleFlame = scaleFlame(containerFlame, scaleFlame, deltaScaleDino);
                }
                lastView = null;
            }
        }
    }
}
