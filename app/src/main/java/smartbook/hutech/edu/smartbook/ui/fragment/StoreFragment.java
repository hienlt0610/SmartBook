package smartbook.hutech.edu.smartbook.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.jude.easyrecyclerview.EasyRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.adapter.CategoryAdapter;
import smartbook.hutech.edu.smartbook.common.BaseFragment;
import smartbook.hutech.edu.smartbook.model.Book;
import smartbook.hutech.edu.smartbook.model.Category;

/**
 * Created by hienl on 6/23/2017.
 */

public class StoreFragment extends BaseFragment {
    @BindView(R.id.fragStore_rvCategory)
    EasyRecyclerView rvCategory;

    private CategoryAdapter mCategoryAdapter;
    private List<Category> mCategoryList = new ArrayList<>();

    @Override
    protected int getResId() {
        return R.layout.fragment_store;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init(){
        mCategoryAdapter = new CategoryAdapter(getActivity(),mCategoryList);
        rvCategory.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));

        for (int i = 0; i < 10; i++) {
            Category category = new Category();
            for (int j = 0; j < 5; j++) {
                category.getListBooks().add(new Book());
            }
            mCategoryAdapter.add(category);
        }

        rvCategory.setAdapter(mCategoryAdapter);
    }
}
