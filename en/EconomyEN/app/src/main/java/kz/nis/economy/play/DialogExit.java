package kz.nis.economy.play;

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
import kz.nis.economy.Main;
import kz.nis.economy.R;

/**
 * Created by myrza on 10/27/15.
 */
@SuppressLint("ValidFragment")
public class DialogExit extends DialogFragment implements View.OnTouchListener {

    private final MediaPlayer mediaPlayer;
    public DialogExit(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_exit_game, container, false);

        view.findViewById(R.id.imageContinue).setOnTouchListener(this);
        view.findViewById(R.id.imageRestart).setOnTouchListener(this);
        view.findViewById(R.id.imageMainMenu).setOnTouchListener(this);

        Dialog dialog = getDialog();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        return view;
    }


    @Override
    public void onResume() {
        if(mediaPlayer!=null)
        mediaPlayer.setVolume(0.4f,0.4f);
        super.onResume();
    }

    @Override
    public void onPause() {
        if(mediaPlayer!=null)
        mediaPlayer.setVolume(1f, 1f);
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
                                startActivity(new Intent(getActivity(),Main.class));
                                break;
                            case R.id.imageRestart:
                                getActivity().finish();
                                startActivity(getActivity().getIntent());
                                break;
                            case R.id.imageContinue:
                                getDialog().cancel();
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
