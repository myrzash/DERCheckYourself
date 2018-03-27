//package kz.nis.economy.extra;
//
//import android.content.Context;
//import android.media.MediaPlayer;
//import android.util.Log;
//
//import kz.nis.economy.Main;
//import kz.nis.economy.R;
//
//public class SoundManager {
//
//    //	private static int soundError = R.raw.error;
////	private static int soundGood = R.raw.right;
//    private static int musicPlay = R.raw.music_play;
//
//    private static MediaPlayer mediaMusic;
//
//
////    	public static void playError(Context context) {
////			mediaPlayer = MediaPlayer.create(context, soundError);
////		mediaPlayer.start();
////	}
////
////	public static void playGood(Context context) {
////		mediaPlayer = MediaPlayer.create(context, soundGood);
////		mediaPlayer.start();
////	}
//    public static void playMusic(Context context) {
//        mediaMusic = MediaPlayer.create(context, musicPlay);
//        mediaMusic.start();
//        Log.d(Main.LOG, " mediaMusic.getDuration()=" + mediaMusic.getDuration());
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////                try {
//////                    TimeUnit.MILLISECONDS.sleep(mediaMusic.getDuration());
////                    TimeUnit.MILLISECONDS.sleep(10000);
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
////                if (mediaMusic.isPlaying()) {
////                    mediaMusic.stop();
////                    mediaMusic.start();
////                }
////
////            }
////        }).start();
//    }
//
//    private void reset(){
//
//    }
//
//    public static void stopMusic() {
//        if (mediaMusic.isPlaying())
//            mediaMusic.stop();
//    }
//}
