package smartbook.hutech.edu.smartbook.common.network.builder;
/*
 * Created by Nhat Hoang on 30/06/2017.
 */

import com.google.gson.JsonObject;

import retrofit2.Call;
import smartbook.hutech.edu.smartbook.common.BaseModel;

public interface ApiRequestListener {
    void onRequestApi(int nCode, Class<? extends BaseModel> _class, Call<JsonObject> call);
}
