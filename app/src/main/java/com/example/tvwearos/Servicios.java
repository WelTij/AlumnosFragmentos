package com.example.tvwearos;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Servicios {
    @GET("alumnos")
    Call<List<Alumnos>> getData();
}
