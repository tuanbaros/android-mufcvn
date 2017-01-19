//package tuannt.bizlive.helper;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.util.Log;
//import android.view.View;
//import android.widget.AbsListView;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//
//import com.github.ksoichiro.android.observablescrollview.ObservableListView;
//
//import java.util.ArrayList;
//
//import tuannt.bizlive.data.DatabaseAdapter;
//import tuannt.bizlive.model.Post;
//import tuannt.bizlive.task.ReadJson2;
//
///**
// * Created by Administrator on 11/18/2015.
// */
//public class EndScrollHot {
//}
package amb.mufcvn.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;

import java.util.ArrayList;

import amb.mufcvn.model.Posts;
import amb.mufcvn.task.ReadJson2;

/**
 * Created by hnc on 10/24/2015.
 */
public class EndScrollHot implements AbsListView.OnScrollListener {

    private int visibleThreshold = 0;

    private int previousTotal = 0;
    private boolean loading = true;
    private String url;
    private int checkPage;

    ArrayList<Posts> arrData;
    //BaseAdapter adapter;
    ProgressDialog progressDialog;
    private Context context;
    private boolean checkBooPage;
    private ImageView ivUp;
    private String tableName;
    ObservableListView listView;
    ImageView imgLoad;
    View footer;
    Activity activity;
    private int mLastFirstVisibleItem;
    private boolean mIsScrollingUp;
    String name;
    public EndScrollHot() {
    }

    public EndScrollHot(ArrayList<Posts> arrData, ImageView ivUp,  Context context,
                        String url, int checkPage, boolean checkBooPage, String tableName,
                        ObservableListView list, ImageView imgLoad, View footer,Activity activity,String name) {
        this.visibleThreshold = visibleThreshold;
        this.arrData = arrData;
this.name=name;
        this.context = context;
        this.url = url;
        this.checkPage = checkPage;
        this.listView = list;
        this.checkBooPage = checkBooPage;
        this.tableName = tableName;
        this.ivUp = ivUp;
        this.imgLoad = imgLoad;
        this.footer = footer;
        this.activity=activity;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
//
        if (loading) {
            if (totalItemCount > previousTotal) {

                loading = false;
                previousTotal = totalItemCount;

            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            Log.d("LOGTAG", "Loadmore");

            new ReadJson2(arrData, context, checkPage, checkBooPage, tableName, listView,
                    imgLoad, footer,activity,name).execute(url + arrData.get(arrData.size()-1).getPost_id());
//            }

            loading = true;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == 0) {
            // Log.d("a", "scrolling stopped...");
        }

        if (view.getId() == listView.getId()) {
            final int currentFirstVisibleItem = listView
                    .getFirstVisiblePosition();
            if (currentFirstVisibleItem > mLastFirstVisibleItem) {
                mIsScrollingUp = false;

                ivUp.setVisibility(View.INVISIBLE);
                // Log.d("a", "scrolling down...");
            } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
                mIsScrollingUp = true;

                ivUp.setVisibility(View.VISIBLE);
                // Log.d("a", "scrolling up...");
            }

            mLastFirstVisibleItem = currentFirstVisibleItem;
        }

        // Log.w("Cafe24", "onScrollStateChanged: " ListView.);
    }

//    public int getSizeTable(String tableName) {
//        DatabaseAdapter da = new DatabaseAdapter(context, tableName);
//        da.open();
//        int numpage = da.getAllPost().getCount();
//        da.close();
//        return numpage;
//    }
}
