package smartbook.hutech.edu.smartbook.ui.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.widget.RadioGroup;

import java.util.HashMap;

import butterknife.BindView;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.common.BaseActivity;
import smartbook.hutech.edu.smartbook.common.BaseFragment;
import smartbook.hutech.edu.smartbook.ui.fragment.BookShelfFragment;
import smartbook.hutech.edu.smartbook.ui.fragment.StoreFragment;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    public static final String KEY_ONLINE = "fragment_online";
    public static final String KEY_OFFLINE = "fragment_offline";

    @BindView(R.id.act_main_rg_tab)
    RadioGroup mRgTab;

    private HashMap<String, Fragment> mMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        //Set listener
        mRgTab.setOnCheckedChangeListener(this);
    }

    /**
     * Init activity
     */
    private void init() {
        initFragmentList();
        //Set default tab online
        mRgTab.check(R.id.act_main_rd_online);
        onCheckedChanged(mRgTab, R.id.act_main_rd_online);

    }

    private void initFragmentList() {
        mMapFragment = new HashMap<>();
        mMapFragment.put(KEY_ONLINE, new StoreFragment());
        mMapFragment.put(KEY_OFFLINE, new BookShelfFragment());
    }


    @Override
    protected int getResId() {
        return R.layout.activity_main;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.act_main_rd_online:
                Fragment fragment = mMapFragment.get(KEY_ONLINE);
                replaceFragment((BaseFragment) fragment, R.id.actMain_flContainer, false);
                break;
            case R.id.act_main_rd_offline:
                fragment = mMapFragment.get(KEY_OFFLINE);
                replaceFragment((BaseFragment) fragment, R.id.actMain_flContainer, false);
                break;
        }
    }
}
