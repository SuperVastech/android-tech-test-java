package com.bridge.androidtechnicaltest.network;

import com.bridge.androidtechnicaltest.db.Pupil;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PupilService {
    @GET("pupils")
    Single<PupilResponse> getAllPupils(
            @Query("page") int page,
            @Header("X-Request-ID") String requestId,
            @Header("User-Agent") String userAgent
    );

    @GET("pupils/{id}")
    Single<Pupil> getPupilById(
            @Path("id") int id,
            @Header("X-Request-ID") String requestId,
            @Header("User-Agent") String userAgent
    );

    @POST("pupils")
    Completable addPupil(
            @Body PupilRequest pupilRequest,
            @Header("X-Request-ID") String requestId,
            @Header("User-Agent") String userAgent
    );

    @PUT("pupils/{id}")
    Completable updatePupil(
            @Path("id") int id,
            @Body Pupil pupil,
            @Header("X-Request-ID") String requestId,
            @Header("User-Agent") String userAgent
    );

    @DELETE("pupils/{id}")
    Completable deletePupil(
            @Path("id") int id,
            @Header("X-Request-ID") String requestId,
            @Header("User-Agent") String userAgent
    );
}
