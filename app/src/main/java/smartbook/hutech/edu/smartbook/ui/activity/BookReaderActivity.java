package smartbook.hutech.edu.smartbook.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsButton;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

import org.apache.commons.lang3.math.NumberUtils;

import butterknife.BindView;
import butterknife.OnClick;
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

    public static final int MENU_SHOW_TABLE_OF_CONTENT = 1;
    public static final int MENU_PAGE_INDEX = 2;
    public static final int MENU_PAGE_BOOKMARK = 3;
    private static final String EXTRA_BOOK = "EXTRA_BOOK";
    private static final int REQ_SELECT_PAGE_CODE = 1;
    @BindView(R.id.activityBookViewer_viewpager)
    ExtendedViewPager mViewPager;
    @BindView(R.id.activityBookViewer_ll_bottom_action)
    LinearLayout mLlBottomAction;
    @BindView(R.id.activityBookViewer_btn_show_hide_action)
    IconicsButton mBtnShowHideAction;

    private Book mBook;

    private BookReaderPagerAdapter mBookDetailPageAdapter;
    private boolean mIsBookmark;
    private boolean mIsToolBarShow = true;

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
        initActionBar();
    }

    private void initActionBar() {
        showHomeIcon(true);
        setTitle(null);
        //Action bar is hide
        if (!mIsToolBarShow) {
            mBtnShowHideAction.setText("{gmi-chevron-up}");
            //Hide toolbar
            getToolbar().post(new Runnable() {
                @Override
                public void run() {
                    getToolbar().setTranslationY(-getToolbar().getBottom());
                }
            });
            //Hide bottom action bar
            mLlBottomAction.post(new Runnable() {
                @Override
                public void run() {
                    mLlBottomAction.setTranslationY(mLlBottomAction.getHeight() - mBtnShowHideAction.getHeight());
                }
            });
        } else {
            mBtnShowHideAction.setText("{gmi-chevron-down}");
        }
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

        setCurrentPageDisplay(1);

        //Init listener
        mViewPager.addOnPageChangeListener(this);
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
        setCurrentPageDisplay(position + 1);
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Menu page index
        MenuItem menuPage = menu.add(0, MENU_PAGE_INDEX, 0, "Page index");
        menuPage.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        //Menu bookmark page
        MenuItem menuBookmark = menu.add(0, MENU_PAGE_BOOKMARK, 0, "Bookmark");
        menuBookmark.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        //Menu table of content
        MenuItem menuTableOfContent = menu.add(0, MENU_SHOW_TABLE_OF_CONTENT, 0, "Table of content");
        Drawable tableContentIcon = new IconicsDrawable(this)
                .sizeRes(R.dimen.material_app_bar_icon_action_icon_size)
                .color(Color.WHITE)
                .icon(MaterialDesignIconic.Icon.gmi_view_dashboard);
        menuTableOfContent.setIcon(tableContentIcon);
        menuTableOfContent.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //Prepare page index menu
        MenuItem menuPageIndex = menu.findItem(MENU_PAGE_INDEX);
        menuPageIndex.setTitle((mViewPager.getCurrentItem() + 1) + "/" + mBook.getPageList().size());

        //Prepare bookmark menu
        MenuItem menuBookmark = menu.findItem(MENU_PAGE_BOOKMARK);
        IconicsDrawable bookmarkIcon = new IconicsDrawable(this)
                .sizeRes(R.dimen.material_app_bar_icon_action_icon_size)
                .icon(MaterialDesignIconic.Icon.gmi_book);
        if (!mIsBookmark) {
            bookmarkIcon.color(Color.WHITE);
        } else {
            bookmarkIcon.color(ContextCompat.getColor(this, R.color.material_color_amber_500));
        }
        menuBookmark.setIcon(bookmarkIcon);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case MENU_SHOW_TABLE_OF_CONTENT:
                TableOfContentActivity.start(this, REQ_SELECT_PAGE_CODE);
                break;
            case MENU_PAGE_INDEX:
                new MaterialDialog.Builder(this)
                        .title(R.string.book_reader_page_move)
                        .content(R.string.book_reader_input_page_to_move)
                        .inputType(InputType.TYPE_CLASS_NUMBER)
                        .inputRange(1, 3, ContextCompat.getColor(this, R.color.material_color_red_500))
                        .input(null, null, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                int page = NumberUtils.toInt(input.toString(), 0);
                                if (page != 0) {
                                    //Move to input page
                                    mViewPager.setCurrentItem(page - 1, true);
                                } else {
                                    Toast.makeText(BookReaderActivity.this, R.string.book_reader_cannot_move, Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .show();
                break;
            case MENU_PAGE_BOOKMARK:
                mIsBookmark = !mIsBookmark;
                invalidateOptionsMenu();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQ_SELECT_PAGE_CODE:
                int pageNum = data.getIntExtra(TableOfContentActivity.EXTRA_PAGE_SELECTED, 0);
                Toast.makeText(this, "Select page number: " + pageNum, Toast.LENGTH_SHORT).show();
                mViewPager.setCurrentItem(pageNum, true);
                break;
        }
    }

    /**
     * Set view display current page
     *
     * @param position
     */
    private void setCurrentPageDisplay(int position) {
        invalidateOptionsMenu();
    }

    @OnClick(R.id.activityBookViewer_btn_show_hide_action)
    void showHideActionClick() {
        if (mIsToolBarShow) {
            //Hide action bar
            getToolbar().animate()
                    .translationY(-getToolbar().getBottom())
                    .setInterpolator(new AccelerateInterpolator())
                    .start();
            mLlBottomAction.animate()
                    .setDuration(300)
                    .translationY(mLlBottomAction.getHeight() - mBtnShowHideAction.getHeight())
                    .setInterpolator(new BounceInterpolator())
                    .start();
            //Change icon
            mBtnShowHideAction.setText("{gmi-chevron-up}");
        } else {
            //Show action bar
            getToolbar().animate().setDuration(100).translationY(0).setInterpolator(new AccelerateInterpolator()).start();
            mLlBottomAction.animate().setDuration(100).translationY(0).setInterpolator(new AccelerateInterpolator()).start();
            mBtnShowHideAction.setText("{gmi-chevron-down}");
        }
        mIsToolBarShow = !mIsToolBarShow;
    }


    @OnClick(R.id.activityBookViewer_btn_acion_cancel)
    void actionCancelClick(View view) {
        Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.activityBookViewer_btn_acion_highlight)
    void actionHighLightClick(View view) {
        Toast.makeText(this, "Run highlight", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.activityBookViewer_btn_acion_clear)
    void actionClearClick(View view) {
        Toast.makeText(this, "Clear draw", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.activityBookViewer_btn_acion_show_result)
    void actionShowResultClick(View view) {
        Toast.makeText(this, "Show result", Toast.LENGTH_SHORT).show();
    }

}
