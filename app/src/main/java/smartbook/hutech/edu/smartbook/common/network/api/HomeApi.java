package smartbook.hutech.edu.smartbook.common.network.api;
/*
 * Created by Nhat Hoang on 30/06/2017.
 */

import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface HomeApi {
    @GET("api/category")
    Call<JsonObject> getCategory(@QueryMap Map<String, Object> body);
}
