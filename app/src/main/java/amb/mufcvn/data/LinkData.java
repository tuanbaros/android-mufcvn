package amb.mufcvn.data;

import amb.mufcvn.activity.MainActivity;

/**
 * Created by Administrator on 12/23/2015.
 */
public class LinkData {
    public static final String APP_ID = "475689825949977";
    public static final String HubName = "bongda";
    public static final String SENDER_ID = "724297418588";
    public static final String HubListenConnectionString = "Endpoint=sb://amobipushnotice.servicebus.windows.net/;SharedAccessKeyName=DefaultListenSharedAccessSignature;SharedAccessKey=3vnDOpCqLWZpjxPyMWlxO7wwchkgBxbmViLXw0aBQOg=";

    public static final String HOME = "http://content.amobi.vn/api/bizlive?act=latest&page=";
    public static final String NEW = "http://content.amobi.vn/api/apiall?app_id=" + LinkData.APP_ID + "&type=latest&page=&limit=10&screen_size=" + MainActivity.height + "x" + MainActivity.width + "&last_id=";
    public static final String HOT = "http://content.amobi.vn/api/apiall?app_id=" + LinkData.APP_ID + "&type=topview&page=&limit=10&screen_size=" + MainActivity.height + "x" + MainActivity.width + "&last_id=";

    public static final String Follow = "http://content.amobi.vn/api/comment/follow";


    public static final String CLIENT_ID = "1915f2edb4254b4c806929cebdbf6c49";
    public static final String SECRET_KEY = "e9c0597568b36e6b";
    public static final String HOST_GET = "http://content.amobi.vn/api/apiall";

}
