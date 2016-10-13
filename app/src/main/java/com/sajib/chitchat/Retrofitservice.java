package com.sajib.chitchat;

import com.sajib.chitchat.model.drawmapmodel.DRAWMAP;

import java.util.Map;

import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by sajib on 3/9/16.
 */
public interface Retrofitservice {

    @GET("json")
    Observable<DRAWMAP> getMapdata(@QueryMap Map<String,String> query);


    class Factory {
        public static Retrofitservice create() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com/maps/api/directions/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(Retrofitservice.class);
        }
    }
}
