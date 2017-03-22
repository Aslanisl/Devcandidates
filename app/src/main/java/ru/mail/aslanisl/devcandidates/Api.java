package ru.mail.aslanisl.devcandidates;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    @GET("./")
    Call<ArrayList<String>> getList();
}
