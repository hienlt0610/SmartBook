package smartbook.hutech.edu.smartbook.ui.fragment;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.adapter.LocalBookAdapter;
import smartbook.hutech.edu.smartbook.common.App;
import smartbook.hutech.edu.smartbook.common.BaseFragment;
import smartbook.hutech.edu.smartbook.common.Common;
import smartbook.hutech.edu.smartbook.database.DaoSession;
import smartbook.hutech.edu.smartbook.database.Download;
import smartbook.hutech.edu.smartbook.database.DownloadDao;
import smartbook.hutech.edu.smartbook.database.MyRepository;
import smartbook.hutech.edu.smartbook.model.Book;
import smartbook.hutech.edu.smartbook.ui.activity.BookReaderActivity;
import smartbook.hutech.edu.smartbook.utils.FileUtils;
import timber.log.Timber;

/**
 * Created by hienl on 7/11/2017.
 */

@RuntimePermissions
public class BookShelfFragment extends BaseFragment implements RecyclerArrayAdapter.OnItemLongClickListener, RecyclerArrayAdapter.OnItemClickListener, android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener {

    public static final String EXTRA_TYPE_SHOW = "EXTRA_TYPE_SHOW";

    @BindView(R.id.frag_local_rv_book_shelf)
    EasyRecyclerView mRvBook;
    @BindView(R.id.frag_local_tv_sort_type)
    TextView mTvSortType;

    private DownloadDao mDownloadDao;
    private SortType mSortType = SortType.NEWEST;
    private LocalBookAdapter.TypeShow mTypeShow = LocalBookAdapter.TypeShow.Grid;

    @Override
    protected int getResId() {
        return R.layout.fragment_book_shelf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDownloadDao = App.getApp().getDaoSession().getDownloadDao();
        //Init reyclerview with check permission
        BookShelfFragmentPermissionsDispatcher.initRecyclerViewWithCheck(this);
    }

    /**
     * Init recycler view
     */
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager;
        if (mTypeShow == LocalBookAdapter.TypeShow.Grid) {
            layoutManager = new GridLayoutManager(getActivity(), 3);
        } else {
            layoutManager = new LinearLayoutManager(getActivity());
        }
        mRvBook.setLayoutManager(layoutManager);
        LocalBookAdapter adapter = new LocalBookAdapter(getActivity(), mTypeShow);
        adapter.setTypeShow(mTypeShow);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemLongClickListener(this);
        mRvBook.setRefreshListener(this);
        mRvBook.setAdapter(adapter);

        //Get list book with sort
        getAndSortListBook();
    }

    /**
     * Event call when long click book item
     *
     * @param bookPos Position of item
     * @return true if want to skip single click
     */
    @Override
    public boolean onItemLongClick(final int bookPos) {
        String[] options = new String[]{getString(R.string.book_shelf_view_detail),
                getString(R.string.book_shelf_delete_book)};
        final int OPTION_DETAIL = 0;
        final int OPTION_DELETE = 1;
        //When long click book, show dialog with list option
        new MaterialDialog.Builder(getActivity())
                .items(options)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        switch (position) {
                            case OPTION_DETAIL:
                                //Show detail of book
                                dialog.dismiss();
                                showDialogDetail(bookPos);
                                break;
                            case OPTION_DELETE:
                                //Handle delete book from storage and database
                                dialog.dismiss();
                                showConfirmDelete(bookPos);
                                break;
                        }
                    }
                })
                .show();
        return false;
    }

    /**
     * Show detail of book
     *
     * @param bookPos Position of book in list
     */
    private void showDialogDetail(int bookPos) {
        LocalBookAdapter adapter = (LocalBookAdapter) mRvBook.getAdapter();
        Download download = adapter.getItem(bookPos);
        new MaterialDialog.Builder(getActivity())
                .title(download.getTitle())
                .content(download.getDescription())
                .positiveText(android.R.string.ok)
                .show();
    }

    /**
     * Show confirm to delete book item
     */
    private void showConfirmDelete(final int bookPos) {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.dialog_confirm_delete_title)
                .content(R.string.dialog_confirm_delete_content)
                .positiveText(R.string.dialog_confirm_agree)
                .negativeText(R.string.dialog_confirm_cancle)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //Delete from database
                        LocalBookAdapter adapter = (LocalBookAdapter) mRvBook.getAdapter();
                        Download download = adapter.getItem(bookPos);
                        mDownloadDao.delete(download);
                        Timber.d("Delete row with bookId = " + download.getBid() + " from Database");
                        //Delete from storage
                        FileUtils.deleteDir(Common.getFolderOfBook(String.valueOf(download.getBid())));
                        Timber.d("Delete folder with bookId = " + download.getBid() + " from Storage");
                        ((LocalBookAdapter) mRvBook.getAdapter()).remove(bookPos);
                        DaoSession daoSession = App.getApp().getDaoSession();
                        //Delete all bookmark
                        MyRepository.deleteBookmart(download.getBid());
                        MyRepository.deleteAnswer(download.getBid());
                        MyRepository.deleteLatestPage(download.getBid());
                    }
                })
                .show();
    }

    /**
     * Event call when single click book item
     *
     * @param position Position of item
     */
    @Override
    public void onItemClick(int position) {
        Download downloadItem = ((LocalBookAdapter) mRvBook.getAdapter()).getItem(position);
        Book book = new Book(downloadItem);
        //Open reader book screen
        BookReaderActivity.start(getActivity(), book);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        BookShelfFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * Call when click button sort
     *
     * @param view View
     */
    @OnClick(R.id.frag_local_ll_sort)
    void onSortButtonClick(View view) {
        showDialogChoiseSortOption(mSortType.getValue());
    }

    /**
     * Call when click button change list style
     *
     * @param view View
     */
    @OnClick(R.id.frag_local_btn_list_style)
    void onChangeListStyleClick(View view) {
        LocalBookAdapter adapter = ((LocalBookAdapter) mRvBook.getAdapter());
        RecyclerView.LayoutManager layoutManager;
        if (adapter.getTypeShow() == LocalBookAdapter.TypeShow.Grid) {
            //Change from Grid to List
            mTypeShow = LocalBookAdapter.TypeShow.List;
            layoutManager = new LinearLayoutManager(getActivity());
        } else {
            //Change from List to Grid
            mTypeShow = LocalBookAdapter.TypeShow.Grid;
            layoutManager = new GridLayoutManager(getActivity(), 3);
        }
        adapter.setTypeShow(mTypeShow);
        mRvBook.setLayoutManager(layoutManager);
        mRvBook.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Timber.d(mTypeShow.toString());
    }

    /**
     * Show dialog to choise sort option
     *
     * @param selectedPos
     */
    private void showDialogChoiseSortOption(int selectedPos) {
        final List<SortType> options = Arrays.asList(SortType.values());
        new MaterialDialog.Builder(getActivity())
                .title("Hiển thị theo")
                .items(options)
                .itemsCallbackSingleChoice(selectedPos, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        switch (options.get(which)) {
                            case NEWEST:
                                mSortType = SortType.NEWEST;
                                break;
                            case ALPHABET:
                                mSortType = SortType.ALPHABET;
                                break;
                            case LAST_OPENED:
                                mSortType = SortType.LAST_OPENED;
                                break;
                        }
                        getAndSortListBook();
                        return true;
                    }
                })
                .show();
    }

    /**
     * Action sort list book
     */
    private void getAndSortListBook() {
        mRvBook.setRefreshing(true);
        LocalBookAdapter adapter = ((LocalBookAdapter) mRvBook.getAdapter());
        adapter.clear();
        mTvSortType.setText(getString(R.string.book_shelf_sort_title, mSortType.getText()));
        QueryBuilder<Download> queryBuilder = mDownloadDao.queryBuilder();
        switch (mSortType) {
            case NEWEST:
                queryBuilder = queryBuilder.orderDesc(DownloadDao.Properties.Created);
                break;
            case LAST_OPENED:
                queryBuilder = queryBuilder.orderDesc(DownloadDao.Properties.LastRead);
                break;
            case ALPHABET:
                queryBuilder = queryBuilder.orderAsc(DownloadDao.Properties.Title);
                break;
        }
        List<Download> listLocal = queryBuilder.list();
        adapter.addAll(listLocal);
        mRvBook.setRefreshing(false);
    }

    /**
     * Call when swipe refresh
     */
    @Override
    public void onRefresh() {
        getAndSortListBook();
    }


    private enum SortType {
        NEWEST("Mới nhất", 0),
        LAST_OPENED("Mới xem", 1),
        ALPHABET("Theo chữ cái", 2);
        private String text;
        private int value;

        private SortType(String text, int value) {
            this.text = text;
            this.value = value;
        }

        public String getText() {
            return text;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return getText();
        }

    }
}
