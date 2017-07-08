package smartbook.hutech.edu.smartbook.common.network.builder;
/*
 * Created by Nhat Hoang on 30/06/2017.
 */

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import smartbook.hutech.edu.smartbook.common.network.connect.ApiConnectConfig;

public class ApiGenerator {
    private static ApiGenerator mInstance = new ApiGenerator();
    private Retrofit mRetrofit;
    private Retrofit mTranslateRetrofit;

    public static synchronized ApiGenerator getInstance() {
        if (mInstance == null)
            mInstance = new ApiGenerator();
        return mInstance;
    }

    private ApiGenerator() {
        mRetrofit = createRetrofit();
        mTranslateRetrofit = createTranslateRetrofit();
    }

    public Retrofit createTranslateRetrofit() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        return new Retrofit.Builder()
                .baseUrl(ApiConnectConfig.createConnectionDetail().getBaseURL())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }

    public Retrofit createRetrofit() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        httpClient.addInterceptor(new ApiCustomInterceptor());
        return new Retrofit.Builder()
                .baseUrl(ApiConnectConfig.createConnectionDetail().getBaseURL())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }

    public <S> S createService(Class<S> serviceClass) {
        return mRetrofit.create(serviceClass);
    }
}
