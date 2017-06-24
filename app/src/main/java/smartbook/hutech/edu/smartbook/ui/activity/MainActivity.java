package smartbook.hutech.edu.smartbook.ui.activity;

import android.os.Bundle;

import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.common.BaseActivity;
import smartbook.hutech.edu.smartbook.ui.fragment.StoreFragment;

public class MainActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    /**
     * Init activity
     */
    private void init() {
        addFragment(new StoreFragment(), R.id.actMain_flContainer, true);
    }


    @Override
    protected int getResId() {
        return R.layout.activity_main;
    }

}
