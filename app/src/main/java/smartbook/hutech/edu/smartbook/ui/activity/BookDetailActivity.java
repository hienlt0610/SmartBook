package smartbook.hutech.edu.smartbook.ui.activity;
/*
 * Created by Nhat Hoang on 09/07/2017.
 */

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SnapHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import butterknife.BindView;
import butterknife.OnClick;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.adapter.ImageDemoAdapter;
import smartbook.hutech.edu.smartbook.common.BaseActivity;
import smartbook.hutech.edu.smartbook.common.Constant;
import smartbook.hutech.edu.smartbook.common.helper.ResizableViewHelper;
import smartbook.hutech.edu.smartbook.common.helper.StartSnapHelper;
import smartbook.hutech.edu.smartbook.model.Book;

public class BookDetailActivity extends BaseActivity implements RecyclerArrayAdapter.OnItemClickListener {

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
        initialize();
    }

    private void initialize() {
        //Init toolbar
        showHomeIcon(true);
        setTitle(null);
        //Load cover image
        Glide.with(this).load(bookModel.getCover()).into(imgBookCover);
        //Init description
        ResizableViewHelper.doResizeTextView(mTvDescription, 3, "Xem thêm", true);
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

    @Override
    public void onItemClick(int position) {
        String imgUrl = adapter.getItem(position);
        Intent intent = PagePreviewActivity.newIntent(this, imgUrl);
        startActivity(intent);
    }

    private void getBundle() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            bookModel = bundle.containsKey(Constant.BOOK) ? (Book) bundle.getSerializable(Constant.BOOK) : new Book();
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
}
