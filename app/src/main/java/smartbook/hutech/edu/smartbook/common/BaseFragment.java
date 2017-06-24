package smartbook.hutech.edu.smartbook.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.ui.activity.MainActivity;

/**
 * Created by hienlt0610 on 5/14/2017.
 */

public abstract class BaseFragment extends Fragment {

    Unbinder unbinder;

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

}
