package smartbook.hutech.edu.smartbook.ui.fragment;
/*
 * Created by Nhat Hoang on 07/07/2017.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.adapter.ImageDemoAdapter;
import smartbook.hutech.edu.smartbook.common.BaseFragment;
import smartbook.hutech.edu.smartbook.common.Constant;
import smartbook.hutech.edu.smartbook.model.Book;

public class BookDetailFragment extends BaseFragment implements RecyclerArrayAdapter.OnItemClickListener {
    @BindView(R.id.fragBookDetail_imgBookCover)
    ImageView imgBookCover;
    @BindView(R.id.fragBookDetail_tvBookName)
    TextView tvBookName;
    @BindView(R.id.fragBookDetail_rvImageDemo)
    EasyRecyclerView rvImageDemo;

    Book bookModel;
    List<String> imageDemoList = new ArrayList<>();
    ImageDemoAdapter adapter;

    public static BookDetailFragment newInstance(Book model) {
        Bundle args = new Bundle();
        args.putSerializable(Constant.BOOK, model);
        BookDetailFragment fragment = new BookDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getResId() {
        return R.layout.fragment_book_detail;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
        addImages();
    }

    private void initialize(){
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            bookModel = bundle.containsKey(Constant.BOOK) ? (Book) bundle.getSerializable(Constant.BOOK) : new Book();
        }
        Glide.with(getContext()).load(bookModel.getCover()).into(imgBookCover);
        tvBookName.setText(bookModel.getTitle());
        adapter = new ImageDemoAdapter(getActivity());
        rvImageDemo.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL, false));
        rvImageDemo.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    private void addImages(){
        imageDemoList.addAll(bookModel.getmDemoPage());
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.fragBookDetail_imgBack)
    public void onViewClicked() {
        getActivity().onBackPressed();
    }

    @Override
    public void onItemClick(int position) {

    }
}
