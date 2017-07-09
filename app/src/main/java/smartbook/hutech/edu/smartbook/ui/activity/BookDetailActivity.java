package smartbook.hutech.edu.smartbook.ui.activity;
/*
 * Created by Nhat Hoang on 09/07/2017.
 */

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
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
import smartbook.hutech.edu.smartbook.model.Book;

import static smartbook.hutech.edu.smartbook.R.layout.activity_book_detail;

public class BookDetailActivity extends BaseActivity implements RecyclerArrayAdapter.OnItemClickListener {

    @BindView(R.id.fragBookDetail_imgBookCover)
    ImageView imgBookCover;
    @BindView(R.id.fragBookDetail_tvBookName)
    TextView tvBookName;
    @BindView(R.id.fragBookDetail_rvImageDemo)
    EasyRecyclerView rvImageDemo;

    Book bookModel;
    ImageDemoAdapter adapter;

    @Override
    protected int getResId() {
        return activity_book_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBundle();
        initialize();
    }

    private void initialize() {
        Glide.with(this).load(bookModel.getCover()).into(imgBookCover);
        tvBookName.setText(bookModel.getTitle());
        adapter = new ImageDemoAdapter(this);
        adapter.addAll(bookModel.getmDemoPage());
        rvImageDemo.setHorizontalScrollBarEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rvImageDemo.setNestedScrollingEnabled(false);
        }
        rvImageDemo.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvImageDemo.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(int position) {
        BookReaderActivity.start(this, bookModel);
    }

    @OnClick(R.id.fragBookDetail_imgBack)
    public void onViewClicked() {
        onBackPressed();
    }

    private void getBundle(){
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            bookModel = bundle.containsKey(Constant.BOOK) ? (Book) bundle.getSerializable(Constant.BOOK) : new Book();
        }
    }

}
