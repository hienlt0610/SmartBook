package smartbook.hutech.edu.smartbook.common.network.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;
import smartbook.hutech.edu.smartbook.common.network.model.TranslateModel;

/**
 * Created by hienl on 7/8/2017.
 */

public interface TranslateApi {
    @POST
    @FormUrlEncoded
    Call<TranslateModel> translate(@Url String url, @Field("text") String text,
                                   @Query("lang") String lang,
                                   @Query("key") String key);
}
