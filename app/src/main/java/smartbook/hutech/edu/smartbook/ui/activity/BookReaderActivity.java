package smartbook.hutech.edu.smartbook.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.util.SparseArray;
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
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.gson.Gson;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsButton;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.adapter.BookReaderPagerAdapter;
import smartbook.hutech.edu.smartbook.common.App;
import smartbook.hutech.edu.smartbook.common.BaseActivity;
import smartbook.hutech.edu.smartbook.common.Common;
import smartbook.hutech.edu.smartbook.common.Constant;
import smartbook.hutech.edu.smartbook.common.interfaces.IAudioAction;
import smartbook.hutech.edu.smartbook.common.interfaces.ISaveHighlight;
import smartbook.hutech.edu.smartbook.common.interfaces.ISwipePage;
import smartbook.hutech.edu.smartbook.common.network.api.TranslateApi;
import smartbook.hutech.edu.smartbook.common.network.builder.ApiGenerator;
import smartbook.hutech.edu.smartbook.common.network.model.TranslateModel;
import smartbook.hutech.edu.smartbook.common.view.ExtendedViewPager;
import smartbook.hutech.edu.smartbook.common.view.bookview.BookImageView;
import smartbook.hutech.edu.smartbook.common.view.bookview.HighlightConfigDialog;
import smartbook.hutech.edu.smartbook.common.view.bookview.TranslateView;
import smartbook.hutech.edu.smartbook.database.Bookmarked;
import smartbook.hutech.edu.smartbook.database.BookmarkedDao;
import smartbook.hutech.edu.smartbook.database.LatestPage;
import smartbook.hutech.edu.smartbook.database.LatestPageDao;
import smartbook.hutech.edu.smartbook.model.Book;
import smartbook.hutech.edu.smartbook.model.HighlightConfig;
import smartbook.hutech.edu.smartbook.model.bookviewer.BookInfoModel;
import smartbook.hutech.edu.smartbook.model.bookviewer.BookListPageModel;
import smartbook.hutech.edu.smartbook.model.bookviewer.BookPageModel;
import smartbook.hutech.edu.smartbook.model.bookviewer.Word;
import smartbook.hutech.edu.smartbook.task.TaskSaveBitmap;
import smartbook.hutech.edu.smartbook.ui.fragment.PageFragment;
import smartbook.hutech.edu.smartbook.utils.FileUtils;
import smartbook.hutech.edu.smartbook.utils.StringUtils;
import smartbook.hutech.edu.smartbook.utils.SystemUtils;
import timber.log.Timber;

/**
 * Created by hienl on 6/22/2017.
 */

@RuntimePermissions
public class BookReaderActivity extends BaseActivity implements ViewPager.OnPageChangeListener,
        ISwipePage, ISaveHighlight, IAudioAction, TranslateView.OnTextTranslateSelectListener {

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
    @BindView(R.id.activityBookViewer_btn_action_translate)
    Button mBtnActionTranslate;
    @BindView(R.id.activityBookViewer_btn_audio_menu)
    FloatingActionMenu mFamAudio;
    @BindView(R.id.activityBookViewer_btn_play_pause_audio)
    FloatingActionButton mFabPlayPause;
    @BindView(R.id.activityBookViewer_translate_view)
    TranslateView mTranslateView;

    private Book mBook;

    private BookInfoModel mBookInfo;
    File mPathBookResource;

    private BookReaderPagerAdapter mBookDetailPageAdapter;
    private boolean mIsBookmark;
    private boolean mIsToolBarShow = false;
    private boolean mIsEmptyPage = true;
    private boolean mIsTranslateMode = false;
    private MediaPlayer mPlayer;
    private int[] mPagePlaying;
    private boolean mIsPause;
    private TextRecognizer mTextRecognizer;
    MaterialDialog mMaterialDialog;

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
        initMediaPlayer();
        mPlayer.reset();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mFamAudio.getMenuIconView().setImageResource(R.drawable.ic_volume);
        mFamAudio.hideMenu(false);
        mTranslateView.setTranslateSelectListener(this);
        mTextRecognizer = new TextRecognizer.Builder(this).build();
    }

    private void initMediaPlayer() {
        mPagePlaying = new int[2];
        mPlayer = new MediaPlayer();
        initPlayerListener();
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
    @NeedsPermission(value = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void init() {
        initBookData();
        initPager();
        checkBookMark();
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

    private void checkBookMark() {
        BookmarkedDao bookmarkedDao = App.getApp().getDaoSession().getBookmarkedDao();
        String bookId = String.valueOf(mBook.getBookId());
        int currentPage = mViewPager.getCurrentItem();
        Bookmarked bookmarked = bookmarkedDao.queryBuilder()
                .where(BookmarkedDao.Properties.Bid.eq(bookId),
                        BookmarkedDao.Properties.Page.eq(currentPage))
                .unique();
        mIsBookmark = bookmarked != null;
        invalidateOptionsMenu();
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
            mBookDetailPageAdapter.addPage(PageFragment.newInstance(page, mBook.getBookId() + "",
                    mPathBookResource.getAbsolutePath()));
        }
        mViewPager.setAdapter(mBookDetailPageAdapter);
        LatestPageDao latestPageDao = App.getApp().getDaoSession().getLatestPageDao();
        LatestPage latestPage = latestPageDao.queryBuilder()
                .where(LatestPageDao.Properties.Bid.eq(String.valueOf(mBook.getBookId())))
                .unique();
        if (latestPage != null) {
            mViewPager.setCurrentItem(latestPage.getPage());
        }
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
        String bookId = String.valueOf(mBook.getBookId());
        mPathBookResource = Common.getFolderOfBook(bookId);

        BookInfoModel bookInfo = Common.getInfoOfBook(bookId);
        BookListPageModel listPage = Common.getListPageOfBook(bookId);

        Timber.d("Resource book: " + mPathBookResource);
        if (bookInfo == null || listPage == null) {
            Toast.makeText(this, "Không tìm thấy dữ liệu của sách, vui lòng kiểm tra lại, hoặc download mới", Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        }

        for (BookPageModel bookPageModel : listPage) {
            mBook.getPageList().add(bookPageModel);
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
        checkBookMark();
        resetAudioIcon();
        if (mPagePlaying[0] == mViewPager.getCurrentItem()) {
            setPlaying(mPagePlaying[1]);
        } else {
            setPlaying(-1);
        }
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
                String bookId = String.valueOf(mBook.getBookId());
                TableOfContentActivity.start(this, bookId, mBook.getPageList(), REQ_SELECT_PAGE_CODE);
                break;
            case MENU_PAGE_INDEX:
                if (mIsEmptyPage) {
                    Toast.makeText(this, R.string.book_reader_empty_page_error, Toast.LENGTH_SHORT).show();
                    return false;
                }
                mMaterialDialog = new MaterialDialog.Builder(this)
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
                BookmarkedDao bookmarkedDao = App.getApp().getDaoSession().getBookmarkedDao();
                bookId = String.valueOf(mBook.getBookId());
                int currentPage = mViewPager.getCurrentItem();

                Bookmarked bookmarked = bookmarkedDao.queryBuilder()
                        .where(BookmarkedDao.Properties.Bid.eq(bookId),
                                BookmarkedDao.Properties.Page.eq(currentPage))
                        .unique();
                if (bookmarked != null) {
                    bookmarkedDao.delete(bookmarked);
                    mIsBookmark = false;
                } else {
                    bookmarked = new Bookmarked(null, bookId, currentPage);
                    bookmarkedDao.insert(bookmarked);
                    mIsBookmark = true;
                }
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
                mViewPager.setCurrentItem(pageNum, true);
                break;
        }
    }

    /**
     * Set view display current page
     *
     * @param position Current position of ViewPager
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
        mIsTranslateMode = false;
        PageFragment pageFragment = getFocusPageFragment();
        if (pageFragment != null) {
            BookImageView bookImageView = pageFragment.getBookImageView();
            if (bookImageView != null) {
                //Disable Earse mode if it available
                mBtnActionEarse.setTextColor(Color.WHITE);
                mBtnActionTranslate.setTextColor(Color.WHITE);
                //Enable or disable highlight mode
                if (bookImageView.isHighlightMode()) {
                    mBtnActionHighlight.setTextColor(ContextCompat.getColor(this, R.color.material_color_white));
                    bookImageView.setBrushType(BookImageView.BrushType.NONE);
                } else {
                    bookImageView.setBrushType(BookImageView.BrushType.HIGHLIGHT);
                    mBtnActionHighlight.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                }
                //Disable swipe in viewpager when highlight mode enable
                mViewPager.setTouchesAllowed(!bookImageView.isHighlightMode());
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
        mIsTranslateMode = false;
        PageFragment pageFragment = getFocusPageFragment();
        if (pageFragment != null) {
            BookImageView bookImageView = pageFragment.getBookImageView();
            if (bookImageView != null) {

                //Disable Highlight mode if it available
                mBtnActionHighlight.setTextColor(Color.WHITE);
                mBtnActionTranslate.setTextColor(Color.WHITE);

                //Enable or disable earse mode
                if (bookImageView.isEarseMode()) {
                    bookImageView.setBrushType(BookImageView.BrushType.NONE);
                    mBtnActionEarse.setTextColor(ContextCompat.getColor(this, R.color.material_color_white));
                } else {
                    bookImageView.setBrushType(BookImageView.BrushType.EARSE);
                    mBtnActionEarse.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

                }

                //Disable swipe in viewpager when highlight mode enable
                mViewPager.setTouchesAllowed(!bookImageView.isEarseMode());
            }
        }

    }

    @OnLongClick(R.id.activityBookViewer_btn_acion_clear)
    boolean onActionClearLongClick(View view) {
        mMaterialDialog = new MaterialDialog.Builder(this)
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
        super.onDestroy();
        //Release MediaPlayer
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer.release();
            mPlayer = null;
        }

        PageFragment focusPageFragment = getFocusPageFragment();
        if (focusPageFragment != null) {
            if (focusPageFragment.getBookImageView() != null) {
                boolean isEmpty = focusPageFragment.getBookImageView().isEmptyHighlight();
                Toast.makeText(this, "empty highlight: " + isEmpty, Toast.LENGTH_SHORT).show();
            }
        }
        //Save last page
        String bookId = String.valueOf(mBook.getBookId());
        int currentPage = mViewPager.getCurrentItem();
        LatestPageDao latestPageDao = App.getApp().getDaoSession().getLatestPageDao();
        LatestPage latestPage = new LatestPage(null, bookId, currentPage);
        long data = latestPageDao.insertOrReplace(latestPage);
        Timber.d(data + "");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BookReaderActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnPermissionDenied(value = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
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

    @Override
    public void saveCurrentHighlight(Bitmap bitmap, int pageIndex) {
        String fileName = StringUtils.leftPad(String.valueOf(pageIndex), 3, '0');
        fileName += ".png";
        File highlightFile = FileUtils.separatorWith(mPathBookResource, Constant.HIGHLIGHT_FOLDER_NAME);
        if (FileUtils.isFileExists(highlightFile)) {
            Timber.d(FileUtils.separatorWith(highlightFile, fileName).getAbsolutePath());
            File fileDraw = FileUtils.separatorWith(highlightFile, fileName);
            TaskSaveBitmap taskSaveBitmap = new TaskSaveBitmap(fileDraw);
            taskSaveBitmap.execute(bitmap);
        }
    }

    @Override
    public void onPlayAudio(String audioPath, int itemPos) {
        if (mPlayer == null) return;
        //Start audio
        if (!mPlayer.isPlaying() && !mIsPause) {
            //Play new audio
            playAudio(audioPath, itemPos);
            mFabPlayPause.setImageResource(R.drawable.ic_action_play);
            mFabPlayPause.setLabelText(getString(R.string.book_reader_pause_audio));
        } else {
            //Stop audio
            if (mPagePlaying[0] == mViewPager.getCurrentItem() && mPagePlaying[1] == itemPos) {
                stopAudio();
                resetAudioIcon();
                mPagePlaying[0] = -1;
                mPagePlaying[1] = -1;
                mFabPlayPause.setImageResource(R.drawable.ic_action_pause);
                mFabPlayPause.setLabelText(getString(R.string.book_reader_resume_audio));
            } else {
                //Play new audio
                playAudio(audioPath, itemPos);
                mFabPlayPause.setImageResource(R.drawable.ic_action_play);
                mFabPlayPause.setLabelText(getString(R.string.book_reader_pause_audio));
            }
        }
    }

    private void initPlayerListener() {
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopAudio();
                resetAudioIcon();
            }
        });

        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                //Invoked when there has been an error during an asynchronous operation
                switch (what) {
                    case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                        Timber.d("MediaPlayer Error", "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK " + extra);
                        break;
                    case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                        Timber.d("MediaPlayer Error", "MEDIA ERROR SERVER DIED " + extra);
                        break;
                    case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                        Timber.d("MediaPlayer Error", "MEDIA ERROR UNKNOWN " + extra);
                        break;
                }
                return false;
            }
        });

        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                playAudio();
                mFamAudio.showMenu(true);
            }
        });
    }

    private void playAudio() {
        if (mPlayer != null && !mPlayer.isPlaying()) {
            mPlayer.start();
        }
    }

    private void stopAudio() {
        mPagePlaying[0] = -1;
        mPagePlaying[1] = -1;
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
        }
        mFamAudio.hideMenu(true);
    }

    private void pauseAudio() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
    }

    private void resetAudio() {
        if (mPlayer != null) {
            stopAudio();
            mPlayer.reset();
        }
    }

    private void resetAudioIcon() {
        for (int i = 0; i < mBookDetailPageAdapter.getCount(); i++) {
            PageFragment pageFragment = (PageFragment) mBookDetailPageAdapter.getItem(i);
            if (pageFragment != null) {
                pageFragment.setPlaying(-1);
            }
        }
    }

    private void setPlaying(int position) {
        PageFragment pageFragment = getFocusPageFragment();
        if (pageFragment != null) {
            pageFragment.setPlaying(position);
        }
    }

    private void playAudio(String path, int pos) {
        resetAudio();
        File fileAudio = FileUtils.separatorWith(mPathBookResource, path);
        if (FileUtils.isFileExists(fileAudio)) {
            try {
                mPlayer.setDataSource(fileAudio.getAbsolutePath());
                resetAudioIcon();
                PageFragment pageFragment = getFocusPageFragment();
                if (pageFragment != null) {
                    pageFragment.setPlaying(pos);
                }
                mPagePlaying[0] = mViewPager.getCurrentItem();
                mPagePlaying[1] = pos;
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPlayer.prepareAsync();
        }
    }

    @OnClick(R.id.activityBookViewer_btn_stop_audio)
    void onActionStopAudioClick() {
        stopAudio();
        resetAudioIcon();
    }

    @OnClick(R.id.activityBookViewer_btn_play_pause_audio)
    void onActionPlayPauseAudioClick() {
        if (mPlayer.isPlaying()) {
            pauseAudio();
            mFabPlayPause.setImageResource(R.drawable.ic_action_pause);
            mFabPlayPause.setLabelText(getString(R.string.book_reader_resume_audio));
            mIsPause = true;
        } else {
            playAudio();
            mFabPlayPause.setImageResource(R.drawable.ic_action_play);
            mFabPlayPause.setLabelText(getString(R.string.book_reader_pause_audio));
            mIsPause = false;
        }
    }

    @OnClick(R.id.activityBookViewer_btn_back_playing_page)
    void onActionBackPlayingPage() {
        mViewPager.setCurrentItem(mPagePlaying[0]);
        mFamAudio.close(true);
    }

    @OnClick(R.id.activityBookViewer_btn_action_translate)
    void onActionTranslateClick(View view) {
        if (!SystemUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, R.string.book_reader_translate_network_require, Toast.LENGTH_SHORT).show();
        }
        if (!mTextRecognizer.isOperational()) {
            // Note: The first time that an app using a Vision API is installed on a
            // device, GMS will download a native libraries to the device in order to do detection.
            // Usually this completes before the app is run for the first time.  But if that
            // download has not yet completed, then the above call will not detect any text,
            // barcodes, or faces.
            // isOperational() can be used to check if the required native libraries are currently
            // available.  The detectors will automatically become operational once the library
            // downloads complete on device.
            Timber.w("Detector dependencies are not yet available.");

            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
                Timber.w("Low Storage");
            }
        }
        //Reset all button state
        mIsTranslateMode = !mIsTranslateMode;
        //reset button
        mBtnActionHighlight.setTextColor(Color.WHITE);
        mBtnActionEarse.setTextColor(Color.WHITE);
        mBtnActionTranslate.setTextColor(Color.WHITE);
        //Default hide translate view
        mTranslateView.setVisibility(View.GONE);
        if (mIsTranslateMode) {
            PageFragment pageFragment = getFocusPageFragment();
            if (pageFragment != null) {
                pageFragment.getBookImageView().setBrushType(BookImageView.BrushType.NONE);
                Bitmap bitmap = pageFragment.getBookImageView().getBitmap();
                mTranslateView.setImageBitmap(bitmap);
                mTranslateView.setVisibility(mIsTranslateMode ? View.VISIBLE : View.GONE);
                recognitionText(bitmap);
            }
            //Set selected button
            mBtnActionTranslate.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        mViewPager.setTouchesAllowed(!mIsTranslateMode);
    }

    /**
     * Recognition text from bitmap and send to translate view
     *
     * @param bitmap
     */
    private void recognitionText(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<TextBlock> textBlocks = mTextRecognizer.detect(frame);
        ArrayList<Word> wordList = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < textBlocks.size(); i++) {
            TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));
            List<? extends Text> textComponents = textBlock.getComponents();
            for (Text textLine : textComponents) {
                List<? extends Text> words = textLine.getComponents();
                for (Text word : words) {
                    Word w = new Word();
                    w.setIndex(index);
                    w.setValue(word.getValue());
                    w.setRect(word.getBoundingBox());
                    wordList.add(w);
                    index++;
                }
            }
        }
        mTranslateView.setListWord(wordList);
    }

    /**
     * Call when get text from translate mode
     *
     * @param text Text value
     */
    @Override
    public void onTextSelected(final String text) {
        mMaterialDialog = new MaterialDialog.Builder(this)
                .title(R.string.book_reader_translate_progress_title)
                .content(R.string.book_reader_translate_loading)
                .progress(true, 0)
                .show();
        TranslateApi translateApi = ApiGenerator.getInstance().createTranslateRetrofit().create(TranslateApi.class);
        translateApi.translate(Constant.TRANSLATE_API_URL, text, "en-vi", Constant.YANDEX_TRANSLATE_API_KEY)
                .enqueue(new Callback<TranslateModel>() {
                    @Override
                    public void onResponse(Call<TranslateModel> call, Response<TranslateModel> response) {
                        mMaterialDialog.cancel();
                        if (response.isSuccessful()) {
                            String content = StringUtils.join(response.body().getText(), "\n");
                            mMaterialDialog = new MaterialDialog.Builder(BookReaderActivity.this)
                                    .title(R.string.book_reader_translate_result)
                                    .content(content)
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TranslateModel> call, Throwable t) {
                        Timber.e(t);
                        mMaterialDialog.cancel();
                    }
                });
    }
}
