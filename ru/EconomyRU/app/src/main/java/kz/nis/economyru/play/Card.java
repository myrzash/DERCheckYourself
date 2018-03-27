package kz.nis.economyru.play;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import kz.nis.economyru.R;

/**
 * Created by myrza on 10/29/15.
 */
public class Card extends Button {

    private static final float minScale = 0.8f;
    private static final long durationDecrease = 1000;
    ImageView imageRes = null;
    private RelativeLayout.LayoutParams lParams = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT);

    public Card(Context context) {
        super(context);
    }

    public Card(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressLint("NewApi")
    @Override
    public void setEnabled(boolean enabled) {
        if (enabled) {
            if (imageRes != null)
                ((RelativeLayout) this.getParent()).removeView(imageRes);
            this.setScaleX(1f);
            this.setScaleY(1f);
            Animation animSet = AnimationUtils.loadAnimation(getContext(), R.anim.anim_decrease_scale);
            this.startAnimation(animSet);
        } else {
            imageRes = new ImageView(getContext());
            imageRes.setImageDrawable(getResources().getDrawable(R.mipmap.checkmark));
            imageRes.setLayoutParams(lParams);
            imageRes.setX(this.getX() + getWidth() / 5);
            imageRes.setY(this.getY());
            ((RelativeLayout) this.getParent()).addView(imageRes);
            this.setScaleX(minScale);
            this.setScaleY(minScale);
        }
        super.setEnabled(enabled);
    }


    @SuppressLint("NewApi")
    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        float scale = (selected == true) ? minScale : 1.0f;
        this.setScaleX(scale);
        this.setScaleY(scale);
    }
}
