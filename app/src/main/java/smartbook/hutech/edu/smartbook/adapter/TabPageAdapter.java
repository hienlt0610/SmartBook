package smartbook.hutech.edu.smartbook.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hienl on 6/23/2017.
 */

public class TabPageAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;
    private List<String> mTitles;

    public TabPageAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new ArrayList<>();
        mTitles = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    /**
     * Add new page
     */
    public void addPage(String title, Fragment fragment) {
        if (title != null && fragment != null) {
            mTitles.add(title);
            mFragments.add(fragment);
            this.notifyDataSetChanged();
        }
    }

    public void removePage(int position) {
        if (position < mFragments.size() && position >= 0) {
            mTitles.remove(position);
            mFragments.remove(position);
            notifyDataSetChanged();
        }
    }
}
