package kz.nis.economykz.play;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import kz.nis.economykz.Main;
import kz.nis.economykz.extra.FontFactory;

/**
 * Created by myrza on 8/26/15.
 */
public class TimeManager extends CountDownTimer {


    private static final long millisInFuture = 15000;
    private static final long countDownInterval = 2500;
    private TextView first;
    private TextView second;
    private static String[] texts = new String[]{"Айдағарды жеңуге\n көмектес!","Карталарды\nсәйкестендірсең,","менің күшім \n көбейеді","ХАХАХА!","Сен мені\nжеңе алмайсың!!!"};

    public TimeManager(Context context, TextView first, TextView second) {
        super(millisInFuture, countDownInterval);
        this.first = first;
        this.second = second;
        first.setTypeface(FontFactory.getFont1(context));
        second.setTypeface(FontFactory.getFont1(context));
        first.setVisibility(View.VISIBLE);
    }

//    public TimeManager(TextView first, TextView second) {
//        super(millisInFuture, countDownInterval);
//        this.first = first;
//        this.second = second;
//        first.setVisibility(View.VISIBLE);
//    }

    @Override
    public void onTick(long millisUntilFinished) {
        int pos = (int) (millisUntilFinished/countDownInterval);
        pos = texts.length - pos;
        if(pos>=3){
            first.setVisibility(View.INVISIBLE);
            second.setVisibility(View.VISIBLE);
            second.setText(texts[pos]);
        }else first.setText(texts[pos]);
        Log.d(Main.LOG,"pos:"+pos+", millisUntilFinished:"+millisUntilFinished);

    }

    @Override
    public void onFinish() {
        second.setVisibility(View.INVISIBLE);
    }
}
