
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

import amb.mufcvn.adapter.MyAdapter;
import amb.mufcvn.model.Posts;
import amb.mufcvn.task.ReadJson2;

/**
 * Created by hnc on 10/24/2015.
 */
public class EndScrollListenerCategory implements AbsListView.OnScrollListener {

    private int visibleThreshold = 0;

    private int previousTotal = 0;
    private boolean loading = true;
    private String url;
    private int checkPage;

    ArrayList<Posts> arrData;
    //ListAdapter adapter;
    ProgressDialog progressDialog;
    private Context context;
    private boolean checkBooPage;
    private ImageView ivUp;
    private String tableName;
    ObservableListView list;
    private int mLastFirstVisibleItem;
    private boolean mIsScrollingUp;
    ImageView imgLoad;
    View footer;
    Activity activity;
    private MyAdapter myAdapter;

    public EndScrollListenerCategory() {
    }

    public EndScrollListenerCategory(ArrayList<Posts> arrData, ImageView ivUp,
                                     Context context, String url, int checkPage, boolean checkBooPage,
                                     String tableName, ObservableListView list, ImageView imgLoad, View footer, Activity activity
            , MyAdapter myAdapter) {
        this.visibleThreshold = visibleThreshold;
        this.arrData = arrData;
        //this.adapter = adapter;
        this.context = context;
        this.url = url;
        this.checkPage = checkPage;
        this.imgLoad = imgLoad;
        this.list = list;
        this.activity = activity;
        this.footer = footer;
        this.checkBooPage = checkBooPage;
        this.tableName = tableName;
        this.ivUp = ivUp;
        this.myAdapter = myAdapter;
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

            new ReadJson2(arrData, context, checkPage, checkBooPage, tableName, list,
                    imgLoad, footer, activity, "listCategoryfrag").execute(url + ++checkPage);
//            }
            myAdapter.getAdapterListCategoryfrag().updateData();
            // adapter.updateData();
            loading = true;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == 0) {
            // Log.d("a", "scrolling stopped...");
        }

        if (view.getId() == list.getId()) {
            final int currentFirstVisibleItem = list
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
