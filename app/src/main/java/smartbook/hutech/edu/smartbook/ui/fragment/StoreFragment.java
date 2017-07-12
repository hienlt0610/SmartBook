package smartbook.hutech.edu.smartbook.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;

import butterknife.BindView;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.adapter.CategoryAdapter;
import smartbook.hutech.edu.smartbook.common.BaseFragment;
import smartbook.hutech.edu.smartbook.common.BaseModel;
import smartbook.hutech.edu.smartbook.common.Constant;
import smartbook.hutech.edu.smartbook.common.interfaces.BookListener;
import smartbook.hutech.edu.smartbook.model.Book;
import smartbook.hutech.edu.smartbook.model.Category;
import smartbook.hutech.edu.smartbook.model.CategoryList;
import smartbook.hutech.edu.smartbook.ui.activity.BookDetailActivity;
import smartbook.hutech.edu.smartbook.ui.activity.MoreBookActivity;
import smartbook.hutech.edu.smartbook.utils.SystemUtils;

/*
 * Created by hienl on 6/23/2017.
 */

public class StoreFragment extends BaseFragment implements BookListener {
    @BindView(R.id.fragStore_rvCategory)
    EasyRecyclerView rvCategory;
    @BindView(R.id.fragStore_tvOffline)
    TextView tvOffline;

    private CategoryAdapter mCategoryAdapter;

    @Override
    protected int getResId() {
        return R.layout.fragment_store;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
        getData();
    }

    @Override
    public void onDataResponse(int nCode, BaseModel nData) {
        if (nData instanceof CategoryList) {
            CategoryList model = (CategoryList) nData;
            mCategoryAdapter.addAll(model.getList());
        }
        dismissLoading();
    }

    private void initialize() {
        mCategoryAdapter = new CategoryAdapter(getActivity());
        rvCategory.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));
        rvCategory.getRecyclerView().hasFixedSize();
        rvCategory.setAdapter(mCategoryAdapter);
        mCategoryAdapter.setOnBookListener(this);
    }

    private void getData() {
        boolean isNetwork = SystemUtils.isNetworkAvailable(getActivity());
        if (!isNetwork) {
            tvOffline.setVisibility(View.VISIBLE);
            rvCategory.setVisibility(View.GONE);
            return;
        }
        showLoading();
        mApiClient.getCategory();
    }

    @Override
    public void onBookItemClick(int categoryPos, int bookPos) {
        Book book = mCategoryAdapter.getBook(categoryPos, bookPos);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.BOOK,book);
        Intent intent = new Intent(getActivity(), BookDetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onClickMore(int categoryPos) {
        Category category = mCategoryAdapter.getItem(categoryPos);
        Intent intent = MoreBookActivity.newIntent(getActivity(), category);
        startActivity(intent);
    }

}
