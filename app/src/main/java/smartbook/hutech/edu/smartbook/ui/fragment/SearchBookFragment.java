package smartbook.hutech.edu.smartbook.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.adapter.LocalBookAdapter;
import smartbook.hutech.edu.smartbook.common.App;
import smartbook.hutech.edu.smartbook.common.BaseFragment;
import smartbook.hutech.edu.smartbook.common.BaseModel;
import smartbook.hutech.edu.smartbook.database.Download;
import smartbook.hutech.edu.smartbook.database.DownloadDao;
import smartbook.hutech.edu.smartbook.model.Book;
import smartbook.hutech.edu.smartbook.model.BookList;
import smartbook.hutech.edu.smartbook.ui.activity.BookDetailActivity;
import smartbook.hutech.edu.smartbook.ui.activity.BookReaderActivity;

/**
 * Created by hienl on 7/27/2017.
 */

public class SearchBookFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnItemClickListener {
    private static final String EXTRA_KEYWORD = "EXTRA_KEYWORD";
    private static final String EXTRA_TYPE_MODE = "EXTRA_TYPE_MODE";
    @BindView(R.id.rv_book)
    EasyRecyclerView mRvBook;

    private LocalBookAdapter mAdapter;
    private String mKeyword;
    private TypeMode mTypeMode;

    public static SearchBookFragment newInstance(String keyword, TypeMode typeMode) {
        SearchBookFragment fragment = new SearchBookFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_KEYWORD, keyword);
        bundle.putSerializable(EXTRA_TYPE_MODE, typeMode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getResId() {
        return R.layout.fragment_search_book;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set default type mode
        mTypeMode = TypeMode.OFFLINE;
        if (getArguments() != null) {
            mKeyword = getArguments().getString(EXTRA_KEYWORD);
            mTypeMode = (TypeMode) getArguments().getSerializable(EXTRA_TYPE_MODE);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList();
    }

    /**
     * Init recyclerview
     */
    private void initList() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRvBook.setLayoutManager(mLayoutManager);
        mAdapter = new LocalBookAdapter(getActivity());
        mAdapter.setOnItemClickListener(this);
        mRvBook.setAdapter(mAdapter);
        mRvBook.setProgressView(R.layout.view_loading);
        TextView tvEmpty = new TextView(getActivity());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        tvEmpty.setLayoutParams(params);
        tvEmpty.setGravity(Gravity.CENTER);
        tvEmpty.setText("Không có sách nào được tìm thấy");
        mRvBook.setEmptyView(tvEmpty);

        mRvBook.setRefreshListener(this);
        //Get list book
        getListBook();
    }

    @Override
    public void onRefresh() {
        mRvBook.showProgress();
        getListBook();
        mRvBook.setRefreshing(false);
    }

    @Override
    public void onDataResponse(int nCode, BaseModel nData) {
        super.onDataResponse(nCode, nData);
        mAdapter.clear();
        if (nData instanceof BookList) {
            List<Book> books = ((BookList) nData).getBooks();
            List<Download> downloads = new ArrayList<>();
            for (Book book : books) {
                Download item = new Download();
                item.setBid(book.getBookId());
                item.setAuthor(book.getAuthor());
                item.setDescription(book.getDescription());
                item.setTitle(book.getTitle());
                item.setTotalSize(book.getFileSize());
                item.setImgPath(book.getCover());
                downloads.add(item);
            }
            mAdapter.addAll(downloads);
        }
    }

    private void getListBook() {
        mRvBook.showProgress();
        if (mTypeMode == TypeMode.ONLINE) {
            callApiGetListBook();
        } else if (mTypeMode == TypeMode.OFFLINE) {
            getListBookFromDatabase();
        }
    }

    private void getListBookFromDatabase() {
        DownloadDao downloadDao = App.getApp().getDaoSession().getDownloadDao();
        QueryBuilder<Download> queryBuilder = downloadDao.queryBuilder().whereOr(DownloadDao.Properties.Title.like("%" + mKeyword + "%"),
                DownloadDao.Properties.Author.like("%" + mKeyword + "%"));
        List<Download> list = queryBuilder.list();
        mAdapter.clear();
        mAdapter.addAll(list);
    }

    private void callApiGetListBook() {
        mApiClient.getListBook(mKeyword);
    }

    @Override
    public void onItemClick(int position) {
        Download item = mAdapter.getItem(position);
        if (mTypeMode == TypeMode.OFFLINE) {
            Book book = new Book();
            book.setBookId(item.getBid());
            BookReaderActivity.start(getActivity(), book);
        } else if (mTypeMode == TypeMode.ONLINE) {
            Intent intent = BookDetailActivity.newIntent(getActivity(), item.getBid());
            startActivity(intent);
        }
    }

    public enum TypeMode {
        ONLINE, OFFLINE
    }
}
