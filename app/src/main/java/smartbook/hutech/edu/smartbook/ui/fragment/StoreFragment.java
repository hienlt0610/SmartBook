package smartbook.hutech.edu.smartbook.ui.fragment;

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
import smartbook.hutech.edu.smartbook.common.interfaces.BookListener;
import smartbook.hutech.edu.smartbook.model.Book;
import smartbook.hutech.edu.smartbook.model.CategoryList;
import smartbook.hutech.edu.smartbook.ui.activity.BookReaderActivity;
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
        init();
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

    private void init() {
        mCategoryAdapter = new CategoryAdapter(getActivity());
        rvCategory.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));
        rvCategory.setAdapter(mCategoryAdapter);
        mCategoryAdapter.setOnBookListener(this);
    }

    private void getData() {
        boolean isNetwork = SystemUtils.isNetworkAvailable(getActivity());
        if(!isNetwork){
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
        BookReaderActivity.start(getActivity(), book);
    }

    @Override
    public void onClickMore(int categoryPos) {

    }

}
