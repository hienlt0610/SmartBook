package smartbook.hutech.edu.smartbook.common.network.api;
/*
 * Created by Nhat Hoang on 30/06/2017.
 */

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

public interface HomeApi {
    @GET("category")
    Call<JsonObject> getCategory();
}
