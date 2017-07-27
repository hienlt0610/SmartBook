package smartbook.hutech.edu.smartbook.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import butterknife.BindView;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.adapter.SearchPagerAdapter;
import smartbook.hutech.edu.smartbook.common.BaseActivity;
import smartbook.hutech.edu.smartbook.ui.fragment.SearchBookFragment;

/**
 * Created by hienl on 7/27/2017.
 */

public class SearchActivity extends BaseActivity {
    private static final String EXTRA_KEYWORD = "EXTRA_KEYWORD";

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    private String mKeyword;

    public static Intent newIntent(Context context, String keyword) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(EXTRA_KEYWORD, keyword);
        return intent;
    }

    @Override
    protected int getResId() {
        return R.layout.activity_search;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBundle();
        initActionBar();
        initPage();
    }

    private void initPage() {
        SearchPagerAdapter searchPagerAdapter = new SearchPagerAdapter(getSupportFragmentManager());
        searchPagerAdapter.addPage("Trực tuyến", SearchBookFragment.newInstance(mKeyword, SearchBookFragment.TypeMode.ONLINE));
        searchPagerAdapter.addPage("Cá nhân", SearchBookFragment.newInstance(mKeyword, SearchBookFragment.TypeMode.OFFLINE));
        mViewPager.setAdapter(searchPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initActionBar() {
        setTitle(mKeyword);
        showHomeIcon(true);
    }

    private void initBundle() {
        Intent intent = getIntent();
        mKeyword = intent.getStringExtra(EXTRA_KEYWORD);
    }
}
