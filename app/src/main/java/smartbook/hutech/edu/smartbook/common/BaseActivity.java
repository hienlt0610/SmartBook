package smartbook.hutech.edu.smartbook.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.common.network.builder.ApiClient;
import smartbook.hutech.edu.smartbook.common.network.builder.ApiRequestListener;
import smartbook.hutech.edu.smartbook.common.network.builder.ApiResponseListener;
import smartbook.hutech.edu.smartbook.common.view.LoadingDialogView;
import smartbook.hutech.edu.smartbook.utils.SystemUtils;
import timber.log.Timber;

/**
 * Created by hienlt0610 on 5/14/2017.
 */

public abstract class BaseActivity extends AppCompatActivity implements ApiResponseListener {

    public Toolbar mToolbar;
    private LoadingDialogView mDialogView;

    public ApiClient getApiClient() {
        return mApiClient;
    }

    public ApiClient mApiClient;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResId());
        mApiClient = new ApiClient(requestListener);
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

    public void showLoading() {
        if(SystemUtils.isNetworkAvailable(this))
            if (mDialogView != null) {
                mDialogView.show();
            } else {
                mDialogView = new LoadingDialogView(this);
                mDialogView.setCanceledOnTouchOutside(false);
                mDialogView.show();
            }
    }

    public void dismissLoading() {
        if (mDialogView != null) {
            mDialogView.dismiss();
        }
    }

    ApiRequestListener requestListener = new ApiRequestListener() {
        @Override
        public void onRequestApi(final int nCode, final Class<? extends BaseModel> _class, final Call<JsonObject> call) {
            boolean isNetwork = SystemUtils.isNetworkAvailable(BaseActivity.this);
            if (call != null && isNetwork) {
                if (call.isExecuted()) {
                    call.cancel();
                }
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        BaseModel mData = null;
                        if (response.body() != null) {
                            Gson gson = new Gson();
                            mData = gson.fromJson(response.body(), _class);
                        }
                        onDataResponse(nCode, mData);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Timber.e(t);
                        dismissLoading();
                    }
                });
            }
        }
    };

    @Override
    public void onDataResponse(int nCode, BaseModel nData) {
        //Nothing
    }
}
