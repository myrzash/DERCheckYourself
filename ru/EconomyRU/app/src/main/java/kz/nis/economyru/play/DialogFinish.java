package kz.nis.economyru.play;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import kz.nis.economyru.Main;
import kz.nis.economyru.R;

/**
 * Created by myrza on 10/27/15.
 */
public class DialogFinish extends DialogFragment implements View.OnTouchListener {

    private MediaPlayer mediaPlayer;

    private static final String TAG_WIN = "win";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_finish_game, container, false);

        Dialog dialog = getDialog();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        dialog.setCancelable(false);
        String tag = this.getTag();
        int resRaw = (tag==TAG_WIN)?R.raw.battle_win:R.raw.battle_lose;
        int img = (tag==TAG_WIN)?R.mipmap.eng_win:R.mipmap.eng_game_over;
        ImageView title = (ImageView) view.findViewById(R.id.imageDialogTitle);
        title.setImageResource(img);
        mediaStart(resRaw);

        view.findViewById(R.id.imageRestart).setOnTouchListener(this);
        view.findViewById(R.id.imageMainMenu).setOnTouchListener(this);

        return view;
    }

    private void mediaStart(int resRaw) {
        mediaPlayer = MediaPlayer.create(getActivity(), resRaw);
        mediaPlayer.start();
    }


    @Override
    public void onPause() {
        if(mediaPlayer!=null)
            mediaPlayer.pause();
        super.onPause();
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
                            getActivity().finish();
                            startActivity(new Intent(getActivity(), Main.class));
                            break;
                        case R.id.imageRestart:
                            getActivity().finish();
                            startActivity(getActivity().getIntent());
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
