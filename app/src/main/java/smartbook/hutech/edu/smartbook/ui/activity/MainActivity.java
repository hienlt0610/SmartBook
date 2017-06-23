package smartbook.hutech.edu.smartbook.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import butterknife.BindView;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.adapter.TabPageAdapter;
import smartbook.hutech.edu.smartbook.common.BaseActivity;
import smartbook.hutech.edu.smartbook.ui.fragment.HomeFragment;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private TabPageAdapter mTabPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    /**
     * Init activity
     */
    private void init() {
        initTabLayoutAndPager();
    }

    private void initTabLayoutAndPager() {
        mTabPageAdapter = new TabPageAdapter(getSupportFragmentManager());
        mTabPageAdapter.addPage("test", new HomeFragment());
        mTabPageAdapter.addPage("test", new HomeFragment());
        mTabPageAdapter.addPage("test", new HomeFragment());
        mViewPager.setAdapter(mTabPageAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected int getResId() {
        return R.layout.activity_main;
    }

}
