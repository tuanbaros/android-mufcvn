package amb.mufcvn.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import amb.mufcvn.fragment.ReadingFragment;
import amb.mufcvn.model.DetailPost;


/**
 * Created by Tranhoa on 4/4/2016.
 */
public class AdapterVpReadingBookMark extends
        FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener {


    private ArrayList<DetailPost> arraydt;

    public AdapterVpReadingBookMark(FragmentManager fm,
                                    ArrayList<DetailPost> arraydt) {
        super(fm);

        this.arraydt = arraydt;

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public Fragment getItem(int position) {
        // TODO Auto-generated method stub
//        ReadingFragment fragment = new ReadingFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("content", String.valueOf(position));
//        fragment.setArguments(bundle);
//        String post_id=arraydt.get(position).getPost_id();
        DetailPost detailPost= arraydt.get(position);

        return ReadingFragment.newInstance(detailPost);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arraydt.size();
    }
}

