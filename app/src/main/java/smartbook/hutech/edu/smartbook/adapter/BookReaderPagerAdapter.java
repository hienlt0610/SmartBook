package smartbook.hutech.edu.smartbook.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hienl on 6/24/2017.
 */

public class BookReaderPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;

    public BookReaderPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    /**
     * Add new page
     */
    public void addPage(Fragment fragment) {
        if (fragment != null) {
            mFragments.add(fragment);
            this.notifyDataSetChanged();
        }
    }

    public void removePage(int position) {
        if (position < mFragments.size() && position >= 0) {
            mFragments.remove(position);
            notifyDataSetChanged();
        }
    }
}
