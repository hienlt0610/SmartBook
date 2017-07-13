package smartbook.hutech.edu.smartbook.ui.activity;
/*
 * Created by Nhat Hoang on 09/07/2017.
 */

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SnapHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dd.CircularProgressButton;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.model.FileDownloadStatus;

import java.io.File;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.adapter.ImageDemoAdapter;
import smartbook.hutech.edu.smartbook.common.BaseActivity;
import smartbook.hutech.edu.smartbook.common.Common;
import smartbook.hutech.edu.smartbook.common.Constant;
import smartbook.hutech.edu.smartbook.common.DownloadManager;
import smartbook.hutech.edu.smartbook.common.helper.ResizableViewHelper;
import smartbook.hutech.edu.smartbook.common.helper.StartSnapHelper;
import smartbook.hutech.edu.smartbook.model.Book;
import smartbook.hutech.edu.smartbook.utils.FileUtils;

@RuntimePermissions
public class BookDetailActivity extends BaseActivity implements RecyclerArrayAdapter.OnItemClickListener, DownloadManager.DownloadStatusUpdater {

    @BindView(R.id.fragBookDetail_imgBookCover)
    ImageView imgBookCover;
    @BindView(R.id.fragBookDetail_tvBookName)
    TextView tvBookName;
    @BindView(R.id.fragBookDetail_tv_capacity)
    TextView tvCapacity;
    @BindView(R.id.fragBookDetail_rvImageDemo)
    EasyRecyclerView rvImageDemo;
    @BindView(R.id.fragBookDetail_tv_description)
    TextView mTvDescription;
    @BindView(R.id.act_book_detail_btn_download)
    CircularProgressButton mBtnDownload;
    @BindView(R.id.act_book_detail_btn_open)
    Button mBtnOpenBook;
    private String mTagName;

    private boolean mIsAvailable;

    Book bookModel;
    ImageDemoAdapter adapter;

    @Override
    protected int getResId() {
        return R.layout.activity_book_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBundle();
        BookDetailActivityPermissionsDispatcher.initializeWithCheck(this);
    }

    @NeedsPermission(value = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void initialize() {
        //Init download button
        initDownloadButton();
        //Init toolbar
        showHomeIcon(true);
        setTitle(null);
        //Load cover image
        Glide.with(this).load(bookModel.getCover()).into(imgBookCover);
        //Init description
        mTvDescription.setText(bookModel.getDescription());
        ResizableViewHelper.doResizeTextView(mTvDescription, 3, "Xem thêm", true);
        //Init file size
        String strFileSize = FileUtils.humanReadableByteCount(bookModel.getFileSize(), true, Locale.US);
        tvCapacity.setText(getString(R.string.book_detail_file_size, strFileSize));
        //Init title
        tvBookName.setText(bookModel.getTitle());
        adapter = new ImageDemoAdapter(this);
        adapter.addAll(bookModel.getmDemoPage());
        //Init recyclerview
        SnapHelper startSnapHelper = new StartSnapHelper();
        startSnapHelper.attachToRecyclerView(rvImageDemo.getRecyclerView());
        rvImageDemo.getRecyclerView().setHorizontalScrollBarEnabled(false);
        rvImageDemo.getRecyclerView().setHasFixedSize(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rvImageDemo.getRecyclerView().setNestedScrollingEnabled(false);
        }
        rvImageDemo.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvImageDemo.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    private void initDownloadButton() {
        boolean isBookAvailable = Common.checkBookAvailable(String.valueOf(bookModel.getBookId()));
        if (isBookAvailable) {
            mIsAvailable = true;
            mBtnDownload.setVisibility(View.GONE);
            mBtnOpenBook.setVisibility(View.VISIBLE);
        } else {
            mIsAvailable = false;
            mBtnDownload.setVisibility(View.VISIBLE);
            mBtnOpenBook.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        DownloadManager.getImpl().addUpdater(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        DownloadManager.getImpl().removeUpdater(this);
    }

    /**
     * Call when page image click
     *
     * @param position position of image
     */
    @Override
    public void onItemClick(int position) {
//        String imgUrl = adapter.getItem(position);
//        Intent intent = PagePreviewActivity.newIntent(this, imgUrl);
//        startActivity(intent);
        BookReaderActivity.start(this, bookModel);
    }

    /**
     * Get data from intent
     */
    private void getBundle() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            bookModel = bundle.containsKey(Constant.BOOK) ? (Book) bundle.getSerializable(Constant.BOOK) : new Book();
            mTagName = "download_book_" + bookModel.getBookId();
        }
    }

    @OnClick(R.id.fragBookDetail_imgBookCover)
    void onCoverClick(View view) {
        String imgUrl = bookModel.getCover();
        Intent intent = PagePreviewActivity.newIntent(this, imgUrl);
        startActivity(intent);
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

    @OnClick(R.id.act_book_detail_btn_download)
    void onDownloadButtonClick(CircularProgressButton button) {
        File rootFolder = Common.getFolderOfRootBook();
        File bookFile = FileUtils.separatorWith(rootFolder, bookModel.getBookId() + "");
        DownloadManager.getImpl()
                .startDownload(bookModel.getDownload(),
                        bookFile.getAbsolutePath(), false, "download_book_" + bookModel.getBookId());
    }

    @OnClick(R.id.act_book_detail_btn_open)
    void onOpenBookButtonClick(View button) {
        BookReaderActivity.start(this, bookModel);
    }

    @Override
    public void update(final BaseDownloadTask task) {
        boolean isSameTag = task.getTag().equals(mTagName);
        if (!isSameTag) {
            return;
        }
        switch (task.getStatus()) {
            case FileDownloadStatus.progress:
                int percent = (int) ((task.getSmallFileSoFarBytes() / (float) task.getSmallFileTotalBytes()) * 100);
                mBtnDownload.setProgress(percent);
                break;
            case FileDownloadStatus.error:
                mBtnDownload.setProgress(-1);
                break;
            case FileDownloadStatus.completed:

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIsAvailable = true;
                        mBtnDownload.setVisibility(View.GONE);
                        mBtnOpenBook.setVisibility(View.VISIBLE);
                    }
                });
                break;
        }
    }

    @Override
    public void blockCompleted(BaseDownloadTask task) {
        boolean isSameTag = task.getTag().equals(mTagName);
        if (!isSameTag) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBtnDownload.setProgress(100);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        BookDetailActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
