package smartbook.hutech.edu.smartbook.ui.activity;

import android.content.Context;
import android.content.Intent;

import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.common.BaseActivity;

/**
 * Created by hienl on 7/12/2017.
 */

public class MoreBookActivity extends BaseActivity {

    private static final String EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID";

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

    @Override
    protected int getResId() {
        return R.layout.activity_more_book;
    }
}
