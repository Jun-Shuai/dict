package com.example.dict.retrofit;

import com.example.dict.retrofit.json.DictJson;
import com.example.dict.retrofit.params.Dict;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IRetrofit {

    @POST("dict/")
    Call<DictJson> getDict(@Body Dict params);

}
