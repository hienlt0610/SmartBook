package smartbook.hutech.edu.smartbook.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.adapter.BookAdapter;
import smartbook.hutech.edu.smartbook.common.BaseActivity;
import smartbook.hutech.edu.smartbook.common.Constant;
import smartbook.hutech.edu.smartbook.model.Book;
import smartbook.hutech.edu.smartbook.model.Category;

/*
 * Created by hienl on 7/12/2017.
 */

public class MoreBookActivity extends BaseActivity implements RecyclerArrayAdapter.OnItemClickListener {

    private static final String EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID";
    private static final String EXTRA_CATEGORY = "EXTRA_CATEGORY";

    @BindView(R.id.actMoreBook_rvMoreBook)
    EasyRecyclerView rvMoreBook;

    BookAdapter mAdapter;
    @BindView(R.id.actionbar_tvTitle)
    TextView tvTitle;

    private List<Book> mBookList = new ArrayList<>();
    private Category mCategory;

    /**
     * Factory method to create new intent for MoreBookActivity
     *
     * @param context    Context
     * @param categoryId Category id for activity
     * @return
     */
    public static Intent newIntent(Context context, int categoryId) {
        Intent intent = new Intent(context, MoreBookActivity.class);
        intent.putExtra(EXTRA_CATEGORY_ID, categoryId);
        return intent;
    }

    public static Intent newIntent(Context context, Category category) {
        Intent intent = new Intent(context, MoreBookActivity.class);
        intent.putExtra(EXTRA_CATEGORY, category);
        return intent;
    }

    @Override
    protected int getResId() {
        return R.layout.activity_more_book;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBundle();
        initialize();
    }

    private void getBundle() {
        Intent intent = getIntent();
        mCategory = (Category) intent.getExtras().get(EXTRA_CATEGORY);
        mBookList = mCategory.getListBooks();
    }

    private void initialize() {
        tvTitle.setText(mCategory.getTitle());
        mAdapter = new BookAdapter(this, mBookList);
        rvMoreBook.setLayoutManager(new GridLayoutManager(this, 3));
        rvMoreBook.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(int position) {
        Book book = mAdapter.getItem(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.BOOK, book);
        Intent intent = new Intent(this, BookDetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @OnClick(R.id.actionbar_imgBack)
    public void onViewClicked() {
        onBackPressed();
    }
}
