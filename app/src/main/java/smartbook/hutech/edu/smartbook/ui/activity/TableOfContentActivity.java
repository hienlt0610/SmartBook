package smartbook.hutech.edu.smartbook.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.adapter.TableOfContentAdapter;
import smartbook.hutech.edu.smartbook.common.App;
import smartbook.hutech.edu.smartbook.common.BaseActivity;
import smartbook.hutech.edu.smartbook.database.Bookmarked;
import smartbook.hutech.edu.smartbook.database.BookmarkedDao;
import smartbook.hutech.edu.smartbook.model.bookviewer.BookListPageModel;
import smartbook.hutech.edu.smartbook.model.bookviewer.BookPageModel;
import smartbook.hutech.edu.smartbook.utils.StringUtils;

/**
 * Created by hienl on 6/26/2017.
 */

public class TableOfContentActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, RecyclerArrayAdapter.OnItemClickListener {

    public static final String EXTRA_PAGE_SELECTED = "EXTRA_PAGE_SELECTED";
    public static final String EXTRA_BOOK_ID = "EXTRA_BOOK_ID";
    public static final String EXTRA_PAGE_LIST = "EXTRA_PAGE_LIST";

    @BindView(R.id.activityTableOfContent_tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.activityTableOfContent_recycler_view)
    EasyRecyclerView mRvPage;
    private int mBookId;
    private BookListPageModel mPageList;

    private TableOfContentAdapter mTableOfContentAdapter;
    private List<BookPageModel> mListBookmark;
    private boolean mIsShowAll = true;

    public static void start(Activity activity, int bookId, BookListPageModel listPage, int requestCode) {
        Intent starter = new Intent(activity, TableOfContentActivity.class);
        starter.putExtra(EXTRA_BOOK_ID, bookId);
        String jsonListPage = new Gson().toJson(listPage);
        starter.putExtra(EXTRA_PAGE_LIST, jsonListPage);
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
        initCommonData();
        initTab();
        initToolbar();
        initListPage();
        initListBookmark();
    }

    private void initListBookmark() {
        BookmarkedDao bookmarkedDao = App.getApp().getDaoSession().getBookmarkedDao();
        List<Bookmarked> bookmarkeds = bookmarkedDao.queryBuilder()
                .where(BookmarkedDao.Properties.Bid.eq(mBookId))
                .orderAsc(BookmarkedDao.Properties.Page).list();
        mListBookmark = new ArrayList<>();
        for (BookPageModel bookPageModel : mPageList) {
            for (Bookmarked bookmarked : bookmarkeds) {
                if (bookmarked.getPage() == bookPageModel.getPageIndex()) {
                    mListBookmark.add(bookPageModel);
                }
            }
        }
    }

    private void initCommonData() {
        Intent intent = getIntent();
        mBookId = intent.getIntExtra(EXTRA_BOOK_ID, 0);
        String jsonPageList = intent.getStringExtra(EXTRA_PAGE_LIST);
        if (StringUtils.isNotEmpty(jsonPageList)) {
            mPageList = new Gson().fromJson(jsonPageList, BookListPageModel.class);
        }
    }

    private void initListPage() {
        if (mPageList == null) {
            return;
        }
        mTableOfContentAdapter = new TableOfContentAdapter(this, mBookId, mPageList);
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
            mTableOfContentAdapter.addAll(mPageList);
            mIsShowAll = true;
        } else {
            mTableOfContentAdapter.addAll(mListBookmark);
            mIsShowAll = false;
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
        BookPageModel page;
        if (mIsShowAll) {
            page = mPageList.get(position);
        } else {
            page = mListBookmark.get(position);
        }
        intent.putExtra(EXTRA_PAGE_SELECTED, page.getPageIndex());
        setResult(RESULT_OK, intent);
        finish();
    }
}
