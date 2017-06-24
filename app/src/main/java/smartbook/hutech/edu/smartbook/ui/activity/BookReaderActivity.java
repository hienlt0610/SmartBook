package smartbook.hutech.edu.smartbook.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.google.gson.Gson;

import butterknife.BindView;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.adapter.BookReaderPagerAdapter;
import smartbook.hutech.edu.smartbook.common.BaseActivity;
import smartbook.hutech.edu.smartbook.common.view.ExtendedViewPager;
import smartbook.hutech.edu.smartbook.model.Book;
import smartbook.hutech.edu.smartbook.model.Page;
import smartbook.hutech.edu.smartbook.ui.fragment.PageFragment;
import timber.log.Timber;

/**
 * Created by hienl on 6/22/2017.
 */

public class BookReaderActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private static final String EXTRA_BOOK = "EXTRA_BOOK";
    @BindView(R.id.activityBookViewer_viewpager)
    ExtendedViewPager mViewPager;
    private Book mBook;

    private BookReaderPagerAdapter mBookDetailPageAdapter;

    /**
     * Start activity With parameter
     *
     * @param context Context
     * @param book    Book model
     */
    public static void start(Context context, Book book) {
        Intent starter = new Intent(context, BookReaderActivity.class);
        String value = new Gson().toJson(book);
        starter.putExtra(EXTRA_BOOK, value);
        context.startActivity(starter);
    }

    @Override
    protected int getResId() {
        return R.layout.activity_book_viewer;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = mBookDetailPageAdapter.getItem(mViewPager.getCurrentItem());
        if (fragment instanceof PageFragment) {
            PageFragment pageFragment = ((PageFragment) fragment);
            if (pageFragment.isZoomed()) {
                pageFragment.zoomToNormal();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }

    }

    /**
     * Init activity
     */
    private void init() {
        initBookData();
        initPager();

        //Init listener
        mViewPager.addOnPageChangeListener(this);
    }

    /**
     * Init pager data
     */
    private void initPager() {
        mBookDetailPageAdapter = new BookReaderPagerAdapter(getSupportFragmentManager());
        for (Page page : mBook.getPageList()) {
            mBookDetailPageAdapter.addPage(PageFragment.newInstance(page));
        }
        mViewPager.setAdapter(mBookDetailPageAdapter);
    }

    /**
     * Init book data
     */
    private void initBookData() {
        Intent intent = getIntent();
        boolean hasBookData = intent.hasExtra(EXTRA_BOOK);
        if (hasBookData) {
            String value = intent.getStringExtra(EXTRA_BOOK);
            mBook = new Gson().fromJson(value, Book.class);
        } else {
            Timber.w("Book is null, cannot load activity");
            this.finish();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Fragment fragment = mBookDetailPageAdapter.getItem(mViewPager.getCurrentItem());
        if (fragment instanceof PageFragment) {
            if (((PageFragment) fragment).isZoomed()) {
                ((PageFragment) fragment).zoomToNormal();
            }
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
