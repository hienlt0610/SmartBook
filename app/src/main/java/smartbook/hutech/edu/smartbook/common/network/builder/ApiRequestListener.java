package smartbook.hutech.edu.smartbook.common.network.builder;
/*
 * Created by Nhat Hoang on 30/06/2017.
 */

import com.google.gson.JsonObject;

import java.lang.reflect.Type;

import retrofit2.Call;

public interface ApiRequestListener {
    void onRequestApi(int nCode, Type mType, Call<JsonObject> call);
}
