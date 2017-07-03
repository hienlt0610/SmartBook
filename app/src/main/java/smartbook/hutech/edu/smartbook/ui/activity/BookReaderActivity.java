package smartbook.hutech.edu.smartbook.ui.activity;

import android.Manifest;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsButton;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.adapter.BookReaderPagerAdapter;
import smartbook.hutech.edu.smartbook.common.BaseActivity;
import smartbook.hutech.edu.smartbook.common.Constant;
import smartbook.hutech.edu.smartbook.common.interfaces.ISwipePage;
import smartbook.hutech.edu.smartbook.common.view.ExtendedViewPager;
import smartbook.hutech.edu.smartbook.common.view.bookview.BookImageView;
import smartbook.hutech.edu.smartbook.common.view.bookview.HighlightConfigDialog;
import smartbook.hutech.edu.smartbook.model.Book;
import smartbook.hutech.edu.smartbook.model.HighlightConfig;
import smartbook.hutech.edu.smartbook.model.bookviewer.BookInfoModel;
import smartbook.hutech.edu.smartbook.model.bookviewer.BookListPageModel;
import smartbook.hutech.edu.smartbook.model.bookviewer.BookPageModel;
import smartbook.hutech.edu.smartbook.ui.fragment.PageFragment;
import smartbook.hutech.edu.smartbook.utils.FileUtils;
import timber.log.Timber;

/**
 * Created by hienl on 6/22/2017.
 */

@RuntimePermissions
public class BookReaderActivity extends BaseActivity implements ViewPager.OnPageChangeListener, ISwipePage {

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
    @BindView(R.id.activityBookViewer_tv_empty_page)
    TextView mTvEmptyPage;
    @BindView(R.id.activityBookViewer_btn_action_highlight)
    Button mBtnActionHighlight;
    @BindView(R.id.activityBookViewer_btn_acion_clear)
    Button mBtnActionEarse;


    private Book mBook;
    private BookInfoModel mBookInfo;
    private BookListPageModel mBookListPage;
    File mPathBookResource;

    private BookReaderPagerAdapter mBookDetailPageAdapter;
    private boolean mIsBookmark;
    private boolean mIsToolBarShow = false;
    private boolean mIsEmptyPage = true;

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
        BookReaderActivityPermissionsDispatcher.initWithCheck(this);
        initActionBar();
    }

    @Override
    public void onBackPressed() {
        if (mBook != null && mBook.getPageList().size() > 0) {
            Fragment fragment = mBookDetailPageAdapter.getItem(mViewPager.getCurrentItem());
            if (fragment instanceof PageFragment) {
                PageFragment pageFragment = ((PageFragment) fragment);
                if (pageFragment.isZoomed()) {
                    pageFragment.zoomToNormal();
                    return;
                }
            }
        }
        super.onBackPressed();
    }

    /**
     * Init activity
     */
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    void init() {
        initBookData();
        initPager();
    }

    private void initActionBar() {
        //Hide action bar when empty page
        if (mIsEmptyPage) {
            //Hide bottom action bar
            mLlBottomAction.post(new Runnable() {
                @Override
                public void run() {
                    mBtnShowHideAction.setText("{gmi-chevron-up}");
                    mLlBottomAction.setTranslationY(mLlBottomAction.getHeight() - mBtnShowHideAction.getHeight());
                }
            });
        }
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
        if (mIsEmptyPage) {
            return;
        }
        mBookDetailPageAdapter = new BookReaderPagerAdapter(getSupportFragmentManager());
        for (BookPageModel page : mBook.getPageList()) {
            mBookDetailPageAdapter.addPage(PageFragment.newInstance(page, mPathBookResource.getAbsolutePath()));
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
        mBook.setBookId(2);
        String bookId = String.valueOf(mBook.getBookId());
        File externalFilesDir = FileUtils.getExternalFilesDir(this);
        mPathBookResource = FileUtils.separatorWith(externalFilesDir, bookId);
        File pathBookInfo = FileUtils.separatorWith(mPathBookResource, Constant.BOOK_INFO_FILE_NAME);
        File pathBookItem = FileUtils.separatorWith(mPathBookResource, Constant.BOOK_ITEMS_FILE_NAME);
        boolean isHasBookDir = FileUtils.isFileExists(mPathBookResource);
        boolean isHasInfoFile = FileUtils.isFileExists(pathBookInfo);
        boolean isHasItemFile = FileUtils.isFileExists(pathBookItem);

        Timber.d("Resource book: " + mPathBookResource);
        if (!(isHasBookDir && isHasInfoFile && isHasItemFile)) {
            Toast.makeText(this, "Không tìm thấy dữ liệu của sách, vui lòng kiểm tra lại, hoặc download mới", Toast.LENGTH_SHORT).show();
            this.finish();
        }


        Gson gson = new Gson();
        try {
            mBookInfo = gson.fromJson(FileUtils.readFileToString(pathBookInfo, "utf-8"), BookInfoModel.class);
            mBookListPage = gson.fromJson(FileUtils.readFileToString(pathBookItem, "utf-8"), BookListPageModel.class);
            if (mBookListPage != null) {
                for (BookPageModel bookPageModel : mBookListPage) {
                    mBook.getPageList().add(bookPageModel);
                }
            }
        } catch (JsonParseException e) {
            Timber.e(e);
        }

        mIsEmptyPage = (mBook.getPageList().size() == 0);
        if (!mIsEmptyPage) {
            mIsToolBarShow = true;
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
        setCurrentPageDisplay(position);
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Menu page index
        MenuItem menuPage = menu.add(0, MENU_PAGE_INDEX, 0, "Page index");
        menuPage.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        if (!mIsEmptyPage) {
            //Menu bookmark page
            MenuItem menuBookmark = menu.add(0, MENU_PAGE_BOOKMARK, 0, "Bookmark");
            menuBookmark.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

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
        if (menuPageIndex != null) {
            if (mBook != null) {
                menuPageIndex.setTitle((mViewPager.getCurrentItem()) + "/" + (mBook.getPageList().size() - 1));
            }
        }

        //Prepare bookmark menu
        MenuItem menuBookmark = menu.findItem(MENU_PAGE_BOOKMARK);
        if (menuBookmark != null) {
            IconicsDrawable bookmarkIcon = new IconicsDrawable(this)
                    .sizeRes(R.dimen.material_app_bar_icon_action_icon_size)
                    .icon(MaterialDesignIconic.Icon.gmi_book);
            if (!mIsBookmark) {
                bookmarkIcon.color(Color.WHITE);
            } else {
                bookmarkIcon.color(ContextCompat.getColor(this, R.color.material_color_amber_500));
            }
            menuBookmark.setIcon(bookmarkIcon);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case MENU_SHOW_TABLE_OF_CONTENT:
                if (mIsEmptyPage) {
                    Toast.makeText(this, R.string.book_reader_empty_page_error, Toast.LENGTH_SHORT).show();
                    return false;
                }
                TableOfContentActivity.start(this, REQ_SELECT_PAGE_CODE);
                break;
            case MENU_PAGE_INDEX:
                if (mIsEmptyPage) {
                    Toast.makeText(this, R.string.book_reader_empty_page_error, Toast.LENGTH_SHORT).show();
                    return false;
                }
                new MaterialDialog.Builder(this)
                        .title(R.string.book_reader_page_move)
                        .content(R.string.book_reader_input_page_to_move)
                        .inputType(InputType.TYPE_CLASS_NUMBER)
                        .inputRange(1, 3, ContextCompat.getColor(this, R.color.material_color_red_500))
                        .input(null, null, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                int page = NumberUtils.toInt(input.toString(), 0);
                                mViewPager.setCurrentItem(page, true);
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

    /**
     * Action click when click action show or hide
     */
    @OnClick(R.id.activityBookViewer_btn_show_hide_action)
    void onActionShowHideActionClick() {
        if (mIsEmptyPage) {
            Toast.makeText(this, R.string.book_reader_empty_page_error, Toast.LENGTH_SHORT).show();
            return;
        }
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
            PageFragment focusPageFragment = getFocusPageFragment();
        } else {
            //Show action bar
            getToolbar().animate().setDuration(100).translationY(0).setInterpolator(new AccelerateInterpolator()).start();
            mLlBottomAction.animate().setDuration(100).translationY(0).setInterpolator(new AccelerateInterpolator()).start();
            mBtnShowHideAction.setText("{gmi-chevron-down}");
        }
        mIsToolBarShow = !mIsToolBarShow;
    }


    /**
     * Action click when click cancel in action bar
     *
     * @param view
     */
    @OnClick(R.id.activityBookViewer_btn_acion_cancel)
    void onActionCancelClick(View view) {
        Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
    }

    /**
     * Action click for highlight action button
     *
     * @param view
     */
    @OnClick(R.id.activityBookViewer_btn_action_highlight)
    void onActionHighLightClick(View view) {

        PageFragment pageFragment = getFocusPageFragment();
        if (pageFragment != null) {
            BookImageView bookImageView = pageFragment.getBookImageView();
            if (bookImageView != null) {

                //Disable Earse mode if it available
                mBtnActionEarse.setTextColor(Color.WHITE);

                //Enable or disable highlight mode
                if (!bookImageView.isHighlightMode()) {
                    bookImageView.setBrushType(BookImageView.BrushType.HIGHLIGHT);
                } else {
                    bookImageView.setBrushType(BookImageView.BrushType.NONE);
                }
                //Disable swipe in viewpager when highlight mode enable
                mViewPager.setTouchesAllowed(!bookImageView.isHighlightMode());

                //Update UI
                if (bookImageView.isHighlightMode()) {
                    mBtnActionHighlight.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                } else {
                    mBtnActionHighlight.setTextColor(ContextCompat.getColor(this, R.color.material_color_white));
                }
            }
        }
    }

    @OnLongClick(R.id.activityBookViewer_btn_action_highlight)
    boolean onActionHighlightLongClick(View view) {
        HighlightConfigDialog dialog = new HighlightConfigDialog(this);
        final PageFragment focusPageFragment = getFocusPageFragment();
        dialog.setOkCallback(new HighlightConfigDialog.OnClickListener() {
            @Override
            public void onClick(HighlightConfigDialog dialog) {
                HighlightConfig config = new HighlightConfig()
                        .color(dialog.getSelectedColor())
                        .storeWidth(dialog.getStrokeWidth());
                if (focusPageFragment != null) {
                    focusPageFragment.getBookImageView().setHighlightConfig(config);
                }
            }
        });
        if (focusPageFragment != null) {
            HighlightConfig config = focusPageFragment.getBookImageView().getHighlightConfig();
            dialog.setDefaultConfig(config);
        }
        dialog.show();
        return true;
    }

    /**
     * Action click when click Clear action in Action bar
     *
     * @param view
     */
    @OnClick(R.id.activityBookViewer_btn_acion_clear)
    void onActionClearClick(View view) {
        PageFragment pageFragment = getFocusPageFragment();
        if (pageFragment != null) {
            BookImageView bookImageView = pageFragment.getBookImageView();
            if (bookImageView != null) {

                //Disable Highlight mode if it available
                mBtnActionHighlight.setTextColor(Color.WHITE);

                //Enable or disable earse mode
                if (!bookImageView.isEarseMode()) {
                    bookImageView.setBrushType(BookImageView.BrushType.EARSE);
                } else {
                    bookImageView.setBrushType(BookImageView.BrushType.NONE);
                }
                //Disable swipe in viewpager when highlight mode enable
                mViewPager.setTouchesAllowed(!bookImageView.isEarseMode());

                //Update UI
                if (bookImageView.isEarseMode()) {
                    mBtnActionEarse.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                } else {
                    mBtnActionEarse.setTextColor(ContextCompat.getColor(this, R.color.material_color_white));
                }
            }
        }

    }

    @OnLongClick(R.id.activityBookViewer_btn_acion_clear)
    boolean onActionClearLongClick(View view) {
        new MaterialDialog.Builder(this)
                .content(R.string.book_clear_highlight)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        PageFragment pageFragment = getFocusPageFragment();
                        if (pageFragment != null) {
                            pageFragment.getBookImageView().clearHighlight();
                        }
                    }
                }).show();
        return true;
    }

    /**
     * Action click when click show result in Action bar
     *
     * @param view
     */
    @OnClick(R.id.activityBookViewer_btn_acion_show_result)
    void onActionShowResultClick(View view) {
        Toast.makeText(this, "Show result", Toast.LENGTH_SHORT).show();
    }

    /**
     * Get current PageFragment from Viewpager
     *
     * @return Current PageFragment was focus in ViewPager
     */
    private PageFragment getFocusPageFragment() {
        if (mIsEmptyPage) {
            return null;
        }
        Fragment fragment = mBookDetailPageAdapter.getItem(mViewPager.getCurrentItem());
        if (fragment instanceof PageFragment) {
            return (PageFragment) fragment;
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        PageFragment focusPageFragment = getFocusPageFragment();
        if (focusPageFragment != null) {
            if (focusPageFragment.getBookImageView() != null) {
                boolean isEmpty = focusPageFragment.getBookImageView().isEmptyHighlight();
                Toast.makeText(this, "empty highlight: " + isEmpty, Toast.LENGTH_SHORT).show();
            }
        }
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BookReaderActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void onExtenalDenny() {
        mIsEmptyPage = true;
        mTvEmptyPage.setVisibility(View.VISIBLE);
        invalidateOptionsMenu();
    }

    /**
     * Call when has request swipe to special page
     *
     * @param position
     */
    @Override
    public void swipePage(int position) {
        if (position < mBookDetailPageAdapter.getCount()) {
            mViewPager.setCurrentItem(position, true);
        }
    }
}
