package smartbook.hutech.edu.smartbook.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.ButterKnife;
import smartbook.hutech.edu.smartbook.R;

/**
 * Created by hienlt0610 on 5/14/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public Toolbar mToolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResId());
        ButterKnife.bind(this);
        setupToolbar();
    }

    /**
     * Setup toolbar
     */
    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    /**
     * Enable show actionbar icon
     *
     * @param enable true if enable actionbar icon
     */
    protected void showHomeIcon(boolean enable) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(enable);
        }
    }

    /**
     * Set title for Actionbar
     *
     * @param title Title
     */
    protected void setTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    /**
     * Get toolbar
     *
     * @return
     */
    protected Toolbar getToolbar() {
        return mToolbar;
    }

    /**
     * Resouce layout id
     *
     * @return
     */
    protected abstract int getResId();


    private void addReplaceFragment(BaseFragment fragment, int containerViewId, boolean isReplace, boolean isAddToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null && fragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (isReplace)
                fragmentTransaction.replace(containerViewId, fragment);
            else
                fragmentTransaction.add(containerViewId, fragment, fragment.getClass().getSimpleName());
            if (isAddToBackStack) {
                fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
            }
            fragmentTransaction.commit();
        }
    }

    public void replaceFragment(BaseFragment fragment, int containerViewId, boolean isAddToBackStack) {

        addReplaceFragment(fragment, containerViewId, true, isAddToBackStack);
    }

    public void addFragment(BaseFragment fragment, int containerViewId, boolean isAddToBackStack) {
        addReplaceFragment(fragment, containerViewId, false, isAddToBackStack);
    }

    public void clearAllBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        int count = fm.getBackStackEntryCount();
        for (int i = 0; i < count; ++i) {
            fm.popBackStack();
        }
    }
}
