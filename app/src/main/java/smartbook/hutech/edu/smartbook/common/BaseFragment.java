package smartbook.hutech.edu.smartbook.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

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

/**
 * Created by hienlt0610 on 5/14/2017.
 */

public abstract class BaseFragment extends Fragment implements ApiResponseListener {

    public ApiClient mApiClient;
    Unbinder unbinder;
    ApiRequestListener requestListener = new ApiRequestListener() {
        @Override
        public void onRequestApi(final int nCode, final Type nType, final Call<JsonObject> call) {
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
                            mData = gson.fromJson(response.body(), nType);
                        }
                        onDataResponse(nCode, mData);

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        dismissLoading();
                    }
                });
            } else {
                if (!isNetwork)
                    SystemUtils.showAlert(getActivity(), getActivity().getString(R.string.error_network),
                            getActivity().getString(R.string.error_no_internet), null);
            }
        }
    };

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
}
