package amb.mufcvn.mediasever;

import android.content.Context;
import android.util.Log;

import amb.mufcvn.activity.ListCategoryActivity;
import amb.mufcvn.activity.MainActivity;
import amb.mufcvn.activity.R;
import amb.mufcvn.activity.SearchActivity;

/**
 * Created by An Viet Computer on 7/12/2016.
 */
public class Controls {
    static String LOG_CLASS = "Controls";
    public static void playControl(Context context) {
        sendMessage(context.getResources().getString(R.string.play));
    }

    public static void pauseControl(Context context) {
        sendMessage(context.getResources().getString(R.string.pause));
    }

    public static void nextControl(Context context) {
        MainActivity.ShowProgess();
        if(PlayerConstants.CHECK_SEARCH){
            SearchActivity.ShowProgess();
        }
        if(PlayerConstants.CHECK_LISTCATEGORY){
            ListCategoryActivity.ShowProgess();
        }
//        SearchActivity.ShowProgess();
//        Log.d("nextcontrol","nextControl"+PlayerConstants.SONG_NUMBER);
        boolean isServiceRunning = UtilFunctions.isServiceRunning(SongService.class.getName(), context);
        if (!isServiceRunning)
            return;
        if(PlayerConstants.SONGS_LIST.size() > 0 ){
            if(PlayerConstants.SONG_NUMBER < (PlayerConstants.SONGS_LIST.size()-1)){
                PlayerConstants.SONG_NUMBER++;
//                Log.d("PlSONG_NUMBER",PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getLink());
                if(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getLink().length()>0&&!PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getLink().equals(null)){
                    PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
                }else{

                    nextControl(context);
                }

            }else{
//                Log.d("nextcontrol","nextContro111l");
                PlayerConstants.SONG_NUMBER = 0;
                if(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getLink().length()>0&&!PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getLink().equals(null)){
                    PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
                }else{

                    nextControl(context);
                }
            }
        }
        PlayerConstants.SONG_PAUSED = false;
    }

    public static void previousControl(Context context) {
        boolean isServiceRunning = UtilFunctions.isServiceRunning(SongService.class.getName(), context);
        if (!isServiceRunning)
            return;
        if(PlayerConstants.SONGS_LIST.size() > 0 ){
            if(PlayerConstants.SONG_NUMBER > 0){
                PlayerConstants.SONG_NUMBER--;
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            }else{
                PlayerConstants.SONG_NUMBER = PlayerConstants.SONGS_LIST.size() - 1;
//                Log.d("SONG_NUMBER",PlayerConstants.SONG_NUMBER+"");
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            }
        }
        PlayerConstants.SONG_PAUSED = false;
    }

    private static void sendMessage(String message) {
        try{
            PlayerConstants.PLAY_PAUSE_HANDLER.sendMessage(PlayerConstants.PLAY_PAUSE_HANDLER.obtainMessage(0, message));
        }catch(Exception e){}
    }
}
