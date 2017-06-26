package smartbook.hutech.edu.smartbook.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.MenuItem;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.adapter.TableOfContentAdapter;
import smartbook.hutech.edu.smartbook.common.BaseActivity;
import smartbook.hutech.edu.smartbook.model.Page;

/**
 * Created by hienl on 6/26/2017.
 */

public class TableOfContentActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, RecyclerArrayAdapter.OnItemClickListener {

    public static final String EXTRA_PAGE_SELECTED = "EXTRA_PAGE_SELECTED";

    @BindView(R.id.activityTableOfContent_tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.activityTableOfContent_recycler_view)
    EasyRecyclerView mRvPage;

    TableOfContentAdapter mTableOfContentAdapter;

    public static void start(Activity activity, int requestCode) {
        Intent starter = new Intent(activity, TableOfContentActivity.class);
        activity.startActivityForResult(starter, requestCode);
    }

    @Override
    protected int getResId() {
        return R.layout.activity_table_of_content;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        initTab();
        initToolbar();
        initListPage();
    }

    private void initListPage() {
        List<Page> pages = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            pages.add(new Page());
        }
        mTableOfContentAdapter = new TableOfContentAdapter(this, pages);
        mRvPage.setLayoutManager(new GridLayoutManager(this, 3));
        mRvPage.getRecyclerView().setHasFixedSize(true);
        mRvPage.setAdapter(mTableOfContentAdapter);
        mTableOfContentAdapter.setOnItemClickListener(this);
    }

    private void initToolbar() {
        showHomeIcon(true);
        setTitle(null);
    }

    private void initTab() {
        TabLayout.Tab tabAll = mTabLayout.newTab();
        tabAll.setText(R.string.table_of_content_tab_all);

        TabLayout.Tab tabBookmark = mTabLayout.newTab();
        tabBookmark.setText(R.string.table_of_content_tab_bookmark);

        mTabLayout.addTab(tabAll);
        mTabLayout.addTab(tabBookmark);

        //Set listener for tablayout
        mTabLayout.addOnTabSelectedListener(this);
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

    /**
     * Call when tab changed
     *
     * @param tab Tab
     */
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int index = mTabLayout.getSelectedTabPosition();
        mTableOfContentAdapter.clear();
        if (index == 0) {
            for (int i = 0; i < 20; i++) {
                mTableOfContentAdapter.add(new Page());
            }
        } else {
            for (int i = 0; i < 5; i++) {
                mTableOfContentAdapter.add(new Page());
            }
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    /**
     * Call when page item clicked
     *
     * @param position Position of item
     */
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PAGE_SELECTED, position);
        setResult(RESULT_OK, intent);
        finish();
    }
}
