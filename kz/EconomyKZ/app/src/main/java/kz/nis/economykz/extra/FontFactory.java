package kz.nis.economykz.extra;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by myrza on 10/19/15.
 */
public class FontFactory {

    private final static String FONT1 = "vAcade.ttf";
    private static Typeface font1 = null;

    public static Typeface getFont1(Context context){
        if(font1==null) {
            font1 = Typeface.createFromAsset(context.getAssets(), FONT1);
        }
        return font1;
    }

}
