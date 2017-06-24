package smartbook.hutech.edu.smartbook.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.jude.easyrecyclerview.EasyRecyclerView;

import butterknife.BindView;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.common.BaseFragment;

/**
 * Created by hienl on 6/23/2017.
 */

public class StoreFragment extends BaseFragment {
    @BindView(R.id.fragStore_rvCategory)
    EasyRecyclerView rvCategory;


    @Override
    protected int getResId() {
        return R.layout.fragment_store;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
