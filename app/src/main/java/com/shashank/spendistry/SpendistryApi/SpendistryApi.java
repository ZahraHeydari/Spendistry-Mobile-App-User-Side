package com.shashank.spendistry.SpendistryApi;

import com.google.gson.JsonElement;
import com.shashank.spendistry.Models.Auth;
import com.shashank.spendistry.Models.Dashboard;
import com.shashank.spendistry.Models.Invoice;
import com.shashank.spendistry.Models.OTP;
import com.shashank.spendistry.Models.Report;
import com.shashank.spendistry.Models.Root;
import com.shashank.spendistry.Models.UserDetails;
import com.shashank.spendistry.Models.Users;
import com.shashank.spendistry.Models.Vendor;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface SpendistryApi {

    //GET
    @GET("user/{email}")
    Call<Users> getUser(@Path("email") String email);

    @GET("mud/{email}")
    Call<Dashboard> getDashboard(@Path("email") String email);

    @GET("report/reportBy/{email}")
    Call<List<Report>> getReportedInvoices(@Path("email") String email);

    @GET("vendor/{email}")
    Call<Vendor> getVendor(@Path("email") String email);

    @GET("pdf/{email}/{businessEmail}/{invoiceId}")
    Call<ResponseBody> getPDF(@Path("email") String email, @Path("businessEmail") String businessEmail, @Path("invoiceId") String invoiceId);

    @GET("invoice/findEle/{email}/{businessEmail}")
    Call<ArrayList<Root>> getBusinessInvoices(@Path("email") String email, @Path("businessEmail") String businessEmail);

    @GET("invoice/{email}")
    Call<Root> getAllInvoices(@Path("email") String email);

    @GET("return/useremail/{email}")
    Call<ArrayList<Invoice>> getReturnedInvoices(@Path("email") String email);

    @GET("invoice/findEle/{email}/{businessEmail}/{invoiceId}")
    Call<Invoice> getSingleReportedInvoice(@Path("email") String email, @Path("businessEmail") String businessEmail, @Path("invoiceId") String invoiceId);


    //POST
    @POST("user")
    Call<UserDetails> createUser(@Body UserDetails user);

    @POST("auth/userLogin")
    Call<JsonElement> getAuth(@Body Auth auth);

    @POST("auth")
    Call<Auth> createAccount( @Body Auth auth);

    @POST("invoice")
    Call<Auth> createInvoice(@Body Auth auth);

    @POST("otp/forgotPassword")
    Call<String> sendOTP(@Body OTP otp);

    @POST("otp/createAccount")
    Call<String> newOTP(@Body OTP otp);

    @POST("otp/verifyOTP")
    Call<String> verifyOTP(@Body OTP otp);

    @Multipart
    @POST("user/uploadImage/{email}")
    Call<okhttp3.ResponseBody> setNewProfilePic(@Path("email") String email, @Part MultipartBody.Part image  );

    @POST("report")
    Call<Report> reportInvoice(@Body Report report);


    //PATCH
    @PATCH("auth/{email}")
    Call<Auth> updateAccount(@Path("email") String id, @Body Auth auth);

    @PATCH("user/{email}")
    Call<UserDetails> updateUser(@Path("email") String id, @Body UserDetails user);

    @PATCH("auth/{email}")
    Call<Auth> setNewPassword(@Path("email") String email, @Body Auth auth);


    //Delete
    @DELETE("auth/{email}")
    Call<Auth> deleteAccount(@Path("email") String email);

    @DELETE("report/{id}")
    Call<Report> deleteReportRequest(@Path("id") String id);

    @DELETE("user/deleteImage/{id}")
    Call<String> deleteProfilePic(@Path("id") String id);

}
