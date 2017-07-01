package smartbook.hutech.edu.smartbook.common.network.builder;
/*
 * Created by Nhat Hoang on 30/06/2017.
 */

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ApiCustomInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        if (response.code() != 200) {
            throw new IOException("Error");
        }
        String stringResponse = response.body().string();

        try {
            JSONObject root = new JSONObject(stringResponse);
            if (root.has("error")) {
                throw new IOException(root.opt("error").toString());
            }
            if (root.has("data")) {
                Log.d("DATA",root.opt("data").toString());
                return response.newBuilder()
                        .message("Successful")
                        .body(ResponseBody.create(response.body().contentType(),
                                root.opt("data").toString()))
                        .build();
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
        return response;
    }
}
