package com.shashank.spendistry.SpendistryApi;

import com.shashank.spendistry.Models.Auth;
import com.shashank.spendistry.Models.Dashboard;
import com.shashank.spendistry.Models.Report;
import com.shashank.spendistry.Models.Users;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SpendistryApi {

    //GET
    @GET("user/{email}")
    Call<Users> getUser(@Path("email") String email);

    @GET("mud/{email}")
    Call<Dashboard> getDashboard(@Path("email") String email);

    @GET("report/reportBy/{email}")
    Call<List<Report>> getReportedInvoices(@Path("email") String email);

    @POST("user")
    Call<Users> createUser( @Body Users user);

    @POST("auth/userLogin")
    Call<String> getAuth(@Body Auth auth);

    @PATCH("auth/{email}")
    Call<Auth> updateAccount(@Path("email") String id, @Body Auth auth);

    @POST("auth")
    Call<Auth> createAccount( @Body Auth auth);


    //Delete
    @DELETE("auth/{email}")
    Call<Auth> deleteAccount(@Path("email") String email);

    @DELETE("report/{id}")
    Call<Report> deleteReportRequest(@Path("id") String id);

}
