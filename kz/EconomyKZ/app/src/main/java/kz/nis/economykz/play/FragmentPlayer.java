package kz.nis.economykz.play;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import kz.nis.economykz.Main;
import kz.nis.economykz.R;
import kz.nis.economykz.db.DBAdapter;
import kz.nis.economykz.extra.FontFactory;

/**
 * Created by Myrza on 11/10/15.
 */

public class FragmentPlayer extends Fragment {
    private static final short HALF = 6;

    protected static final int RESULT_CODE_RESTART = 1;
    protected static final int RESULT_CODE_MAIN = 2;
    private static final int REQUEST_CODE_DIALOG = 3;
    private static final long durationTranslate = 800;
    private int[] PASSED = new int[HALF];
    private static int partId;

    int[] idCards = new int[]{R.id.btn1_1, R.id.btn1_2, R.id.btn1_3,
            R.id.btn1_4, R.id.btn1_6, R.id.btn1_7, R.id.btn1_8,
            R.id.btn1_9, R.id.btn1_11, R.id.btn1_12,
            R.id.btn1_13, R.id.btn1_14};
    Animation.AnimationListener animListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            ActivityGameTwo.translate(getTag());
            Intent intent = new Intent(getActivity(), ActivityDialogFinish.class);
            intent.setAction(getTag());
            startActivityForResult(intent, REQUEST_CODE_DIALOG);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    };
    private View lastView;
    private short PROGRESS;
    private String[][] DATAS;
    private Card[] cards;
    private int CURRENT = 0;//new Random().nextInt(20);
    private ImageView translateImage1;
    private ImageView translateImage2;
    private Typeface typeface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_2, container, false);
        PROGRESS = 0;
        lastView = null;
        Log.d(Main.LOG, "onCreateView FragmentPlayer");
        partId = getActivity().getIntent().getIntExtra("partId", 1);
        DATAS = getDatas();
        if (DATAS.length < HALF * 2) {
            Toast.makeText(getActivity(), R.string.not_enough, Toast.LENGTH_SHORT).show();
            getActivity().finish();
            return view;
        }

        typeface = FontFactory.getFont1(getActivity());
        view.findViewById(R.id.relativeLayoutContainer).setVisibility(View.INVISIBLE);
        translateImage1 = (ImageView) view.findViewById(R.id.buttonCarte1);
        translateImage2 = (ImageView) view.findViewById(R.id.buttonCarte2);

        timer(view);

        int[] tags = new int[idCards.length];
        for (int i = 0; i < idCards.length; i++) {
            tags[i] = i;
        }
        cards = initCards(tags, view);
        setDatasForViews(cards, tags);
        view.findViewById(R.id.buttonPause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityDialog.class);
                startActivityForResult(intent, REQUEST_CODE_DIALOG);
            }
        });
        return view;
    }

    private String[][] getDatas() {
        DBAdapter dbAdapter = new DBAdapter(getActivity());
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

    private Card[] initCards(int[] tags, View view) {
        tags = ActivityGameOne.shuffle(tags);
        Card[] cards = new Card[idCards.length];
        for (int i = 0; i < idCards.length; i++) {
            cards[i] = (Card) view.findViewById(idCards[i]);
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



    private void timer(final View v) {
        CountDownTimer timer = new CountDownTimer(3200, 800) {
            int[] count = new int[]{R.mipmap.countdown_one, R.mipmap.countdown_two, R.mipmap.countdown_three};
            ImageView imageCount = (ImageView) v.findViewById(R.id.imageViewCounter);

            @Override
            public void onTick(long millisUntilFinished) {
                short i = (short) (millisUntilFinished/800  - 1);
                imageCount.setImageResource(count[i]);
            }

            @Override
            public void onFinish() {
                imageCount.setVisibility(View.INVISIBLE);
                v.findViewById(R.id.relativeLayoutContainer).startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_from_bottom));
            }
        };
        timer.start();
    }

    private void animStar(final View v, final View carte) {
        carte.setBackgroundResource(R.mipmap.star_score);
        AnimationSet as = getAnimTranslate(v);
        carte.startAnimation(as);
    }

    private void animFire(final View v, final View carte) {
        carte.setBackgroundResource(R.mipmap.flame);
        AnimationSet as = getAnimTranslate(v);
        carte.startAnimation(as);
    }

    private void animStar2(final View v, final View carte) {
        carte.setBackgroundResource(R.mipmap.star_score);
        AnimationSet as = getAnimTranslate(v);
        as.setAnimationListener(animListener);
        carte.startAnimation(as);
    }

    private void animFire2(final View v, final View carte) {
        carte.setBackgroundResource(R.mipmap.flame);
        AnimationSet as = getAnimTranslate(v);
        as.setAnimationListener(animListener);
        carte.startAnimation(as);
    }

    @SuppressLint("NewApi")
    private AnimationSet getAnimTranslate(final View v) {
        AnimationSet as = new AnimationSet(true);
        as.setFillEnabled(true);
        as.setFillAfter(true);
        as.setInterpolator(new AccelerateDecelerateInterpolator());
        float startX = v.getX() + v.getWidth() / 3;
        float startY = v.getY();
        float finalX = 100f;//imageHero.getX() + imageHero.getWidth() / 2;
        float finalY = 50f;//imageHero.getY() + imageHero.getHeight() / 2;
        TranslateAnimation ta = new TranslateAnimation(startX, finalX, startY, finalY);
        ta.setDuration(durationTranslate);
        as.addAnimation(ta);
        ScaleAnimation scale = new ScaleAnimation(1f, 0f, 1f, 0f, ScaleAnimation.ZORDER_NORMAL, finalX, ScaleAnimation.ZORDER_NORMAL, finalY);
        scale.setDuration(durationTranslate);
        as.addAnimation(scale);
        return as;
    }

    private void addTrash(View v) {
        v.setEnabled(false);
        int tag = (int) v.getTag();
        Log.d(Main.LOG, "addTrash: " + "PROGRESS=" + PROGRESS + ",PASSED:" + tag);

        PASSED[PROGRESS++] = tag;
        if (PROGRESS == HALF) {
            PASSED = ActivityGameOne.shuffle(PASSED);
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

    class onClickListener implements View.OnClickListener {

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
                    ActivityGameTwo.animFlameBo(getTag());
                    if (getTag().equals("top")) {
                        animFire(lastView, translateImage1);

                        if (ActivityGameTwo.scaleBo < 0.1f) {
//                            Log.d(Main.LOG, "Yehoo scaleBo<0.1f");
                            animFire2(v, translateImage2);

                        } else {
                            animFire(v, translateImage2);
                        }
                    }
                    if (getTag().equals("bottom")) {
                        animStar(lastView, translateImage1);
                        animStar(v, translateImage2);
                        if (ActivityGameTwo.scaleFlame < 0.1f) {
                            animStar2(v, translateImage2);
//                            Log.d(Main.LOG, "Yehoo scaleFlame<0.1f");
                        } else {
                            animStar(v, translateImage2);
                        }
                    }
                }
                lastView = null;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(Main.LOG,"requestCode:"+requestCode+", resultCode:"+resultCode+", data:"+data);
        if(data==null) return;
        if (REQUEST_CODE_DIALOG == requestCode) {

            if (RESULT_CODE_MAIN == resultCode) {
                getActivity().finish();
                startActivity(new Intent(getActivity(),Main.class));
            }
            if(RESULT_CODE_RESTART == resultCode){
                getActivity().finish();
                startActivity(getActivity().getIntent());
            }
        }

    }
}
