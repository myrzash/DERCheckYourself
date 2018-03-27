package kz.nis.economykz.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by myrza on 8/14/15.
 */
public class FileAdapter {

    private static String PHOTO_NAME="photo";
    private static String PHOTO_EXTENSION=".png";

    public static void saveToInternalStorage(Context context, int partId, Bitmap bitmapImage) {
        File mypath = new File(context.getFilesDir(), PHOTO_NAME + partId + PHOTO_EXTENSION);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Bitmap getImageFromStorage(Context context, String fileName) {

        try {
            File f = new File(context.getFilesDir().getAbsolutePath(), fileName);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPhotoName(int partId){
        String name =  PHOTO_NAME+partId+PHOTO_EXTENSION;
        return name;
    }
}
