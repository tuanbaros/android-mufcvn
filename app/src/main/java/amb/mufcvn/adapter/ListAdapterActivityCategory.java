package amb.mufcvn.adapter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

import amb.mufcvn.activity.ListCategoryActivity;
import amb.mufcvn.activity.MainActivity;
import amb.mufcvn.activity.R;
import amb.mufcvn.activity.ReadingActivity;
import amb.mufcvn.custom.CustomImageListView;
import amb.mufcvn.data.DatabaseHelper;
import amb.mufcvn.data.LinkData;
import amb.mufcvn.helper.CustomHttpClient;
import amb.mufcvn.helper.GetData;
import amb.mufcvn.mediasever.PlayerConstants;
import amb.mufcvn.mediasever.SongService;
import amb.mufcvn.mediasever.UtilFunctions;
import amb.mufcvn.model.DetailPost;
import amb.mufcvn.model.PostSeria;
import amb.mufcvn.model.Posts;

/**
 * Created by Tuan on 10/5/2015.
 */

public class ListAdapterActivityCategory extends ArrayAdapter<Posts> {
    Activity context;
    int resource;
    ArrayList<Posts> objects;
    LinearLayout linear;
    ArrayList<String> listPostMarked = new ArrayList<>();


    private String tableName;
    private String strlink;
    Typeface tf;
    String category;
    private DetailPost detailPost;


    public ListAdapterActivityCategory(Activity context, int resource, ArrayList<Posts> objects, String tableName,
                                       String strlink, String category) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        this.tableName = tableName;


        this.strlink = strlink;
        this.category = category;
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/SanFranciscoDisplay-Bold.otf");
        getListMarked();

    }



    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View customRow = inflater.inflate(this.resource, null);
        final ImageView imgHead = (ImageView) customRow.findViewById(R.id.imgHead);
        CustomImageListView ivAvatar = (CustomImageListView) customRow.findViewById(R.id.ivAvater);
        TextView tvTitle = (TextView) customRow.findViewById(R.id.tvTitle);
        linear = (LinearLayout) customRow.findViewById(R.id.linear);
        ivAvatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
        final Posts post = this.objects.get(position);
        loadImage(post.getAvatar(), ivAvatar);
        //new ImageDownloaderTask(ivAvatar).execute(post.getAvatar());
        tvTitle.setText(post.getTitle());
        tvTitle.setTypeface(tf);
        if (checkPost(post)) {
            linear.setVisibility(View.VISIBLE);
        }
        boolean isServiceRunning = UtilFunctions.isServiceRunning(SongService.class.getName(), context);
        if (post.getLink_speech_from_text().length()>0 || post.getLink_speech_from_title_des().length()>0) {
            imgHead.setVisibility(View.VISIBLE);
        } else {
            imgHead.setVisibility(View.GONE);
        }
        if(isServiceRunning&&position== PlayerConstants.SONG_NUMBER&&PlayerConstants.PAGE_NUMBER==1){
            imgHead.setImageResource(R.drawable.icon_headed);
        }
        customRow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(context, v);
                if (post.getLink_speech_from_text().equals("") || post.getLink_speech_from_text().equals(null)) {
                    if (checkPost(post)) {
                        Log.d("True", "true");
                        popupMenu.getMenuInflater().inflate(R.menu.item_remove_popup, popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                            @Override
                            public boolean onMenuItemClick(MenuItem arg0) {
                                if (arg0.getTitle().equals("Xóa")) {
                                    linear.setVisibility(View.INVISIBLE);
                                    deleteProduct(post);
                                    getListMarked();
                                    notifyDataSetChanged();
                                    return false;
                                }
                                return true;
                            }

                        });
                    } else {
                        popupMenu.getMenuInflater().inflate(R.menu.item_p_menu, popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                            @Override
                            public boolean onMenuItemClick(MenuItem arg0) {
                                if (arg0.getTitle().equals("Lưu")) {
                                    GetDetail(post.getPost_id());
                                    return false;
                                }
                                return true;
                            }
                        });
                    }
                    popupMenu.show();
                    return true;
                } else {
                    if (checkPost(post)) {
                        Log.d("True", "true");
                        popupMenu.getMenuInflater().inflate(R.menu.item_remove_popup_link, popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                            @Override
                            public boolean onMenuItemClick(MenuItem arg0) {
                                if (arg0.getTitle().equals("Xóa")) {
                                    linear.setVisibility(View.INVISIBLE);
                                    deleteProduct(post);
                                    getListMarked();
                                    notifyDataSetChanged();
                                    return false;
                                }
                                if (arg0.getTitle().equals("Phát thanh")) {
                                    ((ListCategoryActivity) context).PlayAudioContent(objects, position);
                                    imgHead.setImageResource(R.drawable.icon_headed);
                                    PlayerConstants.CHECK_HOME = false;
                                    notifyDataSetChanged();
                                    popupMenu.dismiss();
                                    return false;
                                }
                                if (arg0.getTitle().equals("Điểm tin")) {
                                    ((ListCategoryActivity) context).PlayAudioDes(objects, position);
                                    imgHead.setImageResource(R.drawable.icon_headed);
                                    PlayerConstants.CHECK_HOME = false;
                                    notifyDataSetChanged();
                                    popupMenu.dismiss();
                                    return false;
                                }
                                return true;
                            }

                        });
                    } else {
                        Log.d("False", "False");
                        popupMenu.getMenuInflater().inflate(R.menu.item_p_menu_link, popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                            @Override
                            public boolean onMenuItemClick(MenuItem arg0) {
                                if (arg0.getTitle().equals("Lưu")) {
                                    GetDetail(post.getPost_id());
                                    return false;
                                }
                                if (arg0.getTitle().equals("Phát thanh")) {
                                    ((ListCategoryActivity) context).PlayAudioContent(objects, position);
                                    imgHead.setImageResource(R.drawable.icon_headed);
                                    PlayerConstants.CHECK_HOME = false;
                                    notifyDataSetChanged();
                                    popupMenu.dismiss();
                                    return false;
                                }
                                if (arg0.getTitle().equals("Điểm tin")) {
//                                    popupMenu.dismiss();
//                                    playAudioStream.playAudio(post.getTitle(), post.getLink_speech_from_text());
                                    ((ListCategoryActivity) context).PlayAudioDes(objects, position);
                                    imgHead.setImageResource(R.drawable.icon_headed);
                                    PlayerConstants.CHECK_HOME = false;
                                    notifyDataSetChanged();
                                    popupMenu.dismiss();
                                    return false;
                                }
                                return true;
                            }
                        });
                    }
                    popupMenu.show();
                    return true;
                }

            }
        });
        customRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                playAudioStream.killMedia();
                Intent intent = new Intent(context, ReadingActivity.class);

                intent.putExtra("position", String.valueOf(position));
                intent.putExtra("data", new PostSeria(objects));

//
//                if (tableName.equals(TableName.HOME_TABLE)) {
//                    intent.putExtra("position", position + 10);
//                } else {
//                    intent.putExtra("position", position);
//                }
//                intent.putExtra("tableName", tableName);
//                intent.putExtra("url", s);
//                intent.putExtra("category", category);
                context.startActivityForResult(intent, 0);
            }
        });
        return customRow;
    }


    //    public void getListMarked() {
//        listPostMarked.clear();
//        DatabaseAdapter db = new DatabaseAdapter(context, TableName.BOOKMARK_TABLE);
//        db.open();
//        Cursor c = db.getAllPost();
//        if(c != null && c.getCount()>0) {
//            if (c.moveToFirst()) {
//                do {
//                    listPostMarked.add(c.getString(1));
//                } while (c.moveToNext());
//            }
//        }
//        db.close();
//    }
    public void deleteProduct(Posts position) {
        DatabaseHelper myDd = new DatabaseHelper(context);
        myDd.deletePost(position.getPost_id());
    }

    public void getListMarked() {
        DatabaseHelper myDb = new DatabaseHelper(context);
        Cursor c = myDb.getAllData();
        listPostMarked.removeAll(listPostMarked);
        if (c.moveToLast()) {
            do {
                String s = "";
                s = c.getString(1);

                listPostMarked.add(s);
            } while (c.moveToPrevious());
        }
        myDb.close();


    }


    public Boolean checkPost(Posts post) {
        for (String po : listPostMarked) {
            if (po.equals(post.getPost_id())) {
                return true;
            }
        }
        return false;
    }

    public void updateData() {
        getListMarked();
        this.notifyDataSetChanged();
    }

    public void updateReceiptsList(List<Posts> newlist) {

        objects.clear();
        objects.addAll(newlist);
        this.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {

        super.notifyDataSetChanged();
    }

    public void loadImage(String url, ImageView img) {
//		Log.w("TerraBookChannel", "load image:" + url);
        ImageLoader.getInstance()
                .displayImage(url, img, getDisplayImageOptions());
    }

    private DisplayImageOptions getDisplayImageOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.nomage)
                .showImageForEmptyUri(R.drawable.nomage)
                .showImageOnLoading(R.drawable.nomage)
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
        return options;
    }

    public void GetDetail(final String postId) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                String scontent = null;
                CustomHttpClient http = new CustomHttpClient(LinkData.HOST_GET);
                http.addParam("app_id", LinkData.APP_ID);
                http.addParam("type", "detail");
                http.addParam("screen_size", MainActivity.height + "x" + MainActivity.width);
                http.addParam("post_id", postId);


                try {
                    scontent = http.request();
                    Log.d("http detail", http.getUrl() + "");
                    detailPost = GetData.getDetailPost(scontent);
                    Log.d("detailPost", detailPost.getPost_id());
                } catch (Exception e) {
                    e.printStackTrace();
//                    dialogint.show();
                }
                if (scontent != null) {
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!detailPost.getCategory_name().equals("")) {
                                // TODO Auto-generated method stub
//                                Methods.savePostData(context, TableName.BOOKMARK_TABLE, detailPost);

                            }
                            DatabaseHelper myDb = new DatabaseHelper(context);
                            myDb.insertData(detailPost.getPost_id(), detailPost.getTitle(),
                                    detailPost.getAvatar(), detailPost.getAvatardescription(),
                                    detailPost.getDescription(), detailPost.getLink(),
                                    detailPost.getAuthor(), detailPost.getPublished_time(),
                                    detailPost.getCategory_name(), detailPost.getCategory(),
                                    detailPost.getLevel(), detailPost.getContent(), detailPost.getNum_view(),
                                    detailPost.getNum_like(), detailPost.getTag(), detailPost.getLink_speech_from_text(),detailPost.getLink_speech_from_title_des());

                            getListMarked();
                            linear.setVisibility(View.VISIBLE);
                            notifyDataSetChanged();
                        }
                    });
                }
            }
        });
        thread.start();

    }

    public interface PlayAudioStream {
        public void playAudio(String name, String link);

        public void killMedia();
    }
}

