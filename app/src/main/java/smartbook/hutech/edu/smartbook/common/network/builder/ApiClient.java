package smartbook.hutech.edu.smartbook.common.network.builder;
/*
 * Created by Nhat Hoang on 30/06/2017.
 */

import com.google.gson.JsonObject;

import java.lang.reflect.Type;

import retrofit2.Call;
import smartbook.hutech.edu.smartbook.common.Constant;
import smartbook.hutech.edu.smartbook.common.network.api.HomeApi;
import smartbook.hutech.edu.smartbook.model.Category;

public class ApiClient {
    private ApiRequestListener mListener;

    public ApiClient(ApiRequestListener listener) {
        this.mListener = listener;
    }

    private void requestApi(Type nType, Call<JsonObject> call) {
        if (mListener != null) {
            mListener.onRequestApi(Constant.CODE_REQUEST_DEFAULT, nType, call);
        }
    }

    public void getCategory() {
        ApiParams params = new ApiParams();
        HomeApi api = ApiGenerator.getInstance().createService(HomeApi.class);
        requestApi((Type) new Category(), api.getCategory(params.getParams()));
    }
}
