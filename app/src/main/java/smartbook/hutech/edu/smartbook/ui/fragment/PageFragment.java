package smartbook.hutech.edu.smartbook.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import butterknife.BindView;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.common.BaseFragment;
import smartbook.hutech.edu.smartbook.common.view.BookImageView;

/**
 * Created by hienl on 6/24/2017.
 */

public class PageFragment extends BaseFragment {
    @BindView(R.id.fragmentPage_imgBook)
    BookImageView mBookImageView;

    @Override
    protected int getResId() {
        return R.layout.fragment_page;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBookImageView.setImageResource(R.drawable.gai);
    }
}
