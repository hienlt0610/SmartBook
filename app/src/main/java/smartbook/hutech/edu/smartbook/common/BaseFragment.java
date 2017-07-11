package smartbook.hutech.edu.smartbook.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.common.network.builder.ApiClient;
import smartbook.hutech.edu.smartbook.common.network.builder.ApiRequestListener;
import smartbook.hutech.edu.smartbook.common.network.builder.ApiResponseListener;
import smartbook.hutech.edu.smartbook.ui.activity.MainActivity;
import smartbook.hutech.edu.smartbook.utils.SystemUtils;
import timber.log.Timber;

/**
 * Created by hienlt0610 on 5/14/2017.
 */

public abstract class BaseFragment extends Fragment implements ApiResponseListener {

    Unbinder unbinder;
    public ApiClient mApiClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiClient = new ApiClient(requestListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getResId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    protected abstract int getResId();

    public void replaceFragment(BaseFragment fragment, int containerViewId, boolean isAddToBackStack) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).replaceFragment(fragment, containerViewId, isAddToBackStack);
        }
    }

    public void replaceFragment(BaseFragment fragment, boolean isAddToBackStack) {
        if (getActivity() instanceof BaseActivity) {
            if (getActivity() instanceof MainActivity)
                ((BaseActivity) getActivity()).replaceFragment(fragment, R.id.actMain_flContainer, isAddToBackStack);
        }
    }

    public void addFragment(BaseFragment fragment, boolean isAddToBackStack) {
        if (getActivity() instanceof BaseActivity) {
            if (getActivity() instanceof MainActivity)
                ((BaseActivity) getActivity()).replaceFragment(fragment, R.id.actMain_flContainer, isAddToBackStack);
        }
    }

    public void clearAllBackStack() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).clearAllBackStack();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    ApiRequestListener requestListener = new ApiRequestListener() {
        @Override
        public void onRequestApi(final int nCode, final Class<? extends BaseModel> _class, final Call<JsonObject> call) {
            boolean isNetwork = SystemUtils.isNetworkAvailable(getActivity());
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

    public void showLoading() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showLoading();
        }
    }

    public void dismissLoading() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).dismissLoading();
        }
    }

    @Override
    public void onDataResponse(int nCode, BaseModel nData) {

    }

    public void setTitle(View v, String title) {
        TextView view = (TextView) v.findViewById(R.id.actionbar_tvTitle);
        if (view != null) {
            view.setText(title);
        }
    }

    public void setupBackButton(View v) {
        View view = v.findViewById(R.id.actionbar_imgBack);
        if (view != null) {
            view.setOnClickListener(onBackClick);
        }
    }

    View.OnClickListener onBackClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentManager fm = getFragmentManager();
            if (getActivity() instanceof MainActivity && fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
            } else {
                getActivity().onBackPressed();
            }
        }
    };
}
