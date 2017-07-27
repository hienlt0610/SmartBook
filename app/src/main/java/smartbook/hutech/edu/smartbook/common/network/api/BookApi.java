package smartbook.hutech.edu.smartbook.common.network.api;
/*
 * Created by Nhat Hoang on 30/06/2017.
 */

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BookApi {
    @GET("category")
    Call<JsonObject> getCategory();

    @GET("book")
    Call<JsonObject> getListBook(@Query("keyword") String keyword);

    @GET("book/{bookId}")
    Call<JsonObject> getBook(@Path("bookId") int bookId);
}
