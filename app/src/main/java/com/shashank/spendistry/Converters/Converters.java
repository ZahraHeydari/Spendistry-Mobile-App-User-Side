package com.shashank.spendistry.Converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shashank.spendistry.Models.BusinessDetail;
import com.shashank.spendistry.Models.UserDetails;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Converters {

    @TypeConverter
    public static String fromUserDetails(UserDetails userDetails) {
        if (userDetails == null) {
            return (null);
        }
        return (new Gson().toJson(userDetails));
    }

    @TypeConverter
    public static UserDetails toUserDetails(String userDetailsString) {
        if (userDetailsString == null) {
            return (null);
        }
        return (new Gson().fromJson(userDetailsString, UserDetails.class));
    }

    @TypeConverter
    public static String fromBusinessDetails(ArrayList<BusinessDetail> businessDetails) {
        if (businessDetails == null) {
            return (null);
        }
        return (new Gson().toJson(businessDetails));
    }

    @TypeConverter
    public static ArrayList<BusinessDetail> toBusinessDetails(String businessDetailsString) {
        Type typeMyType = new TypeToken<ArrayList<BusinessDetail>>(){}.getType();
        Gson gson = new Gson();
        return gson.fromJson(businessDetailsString, typeMyType);
    }
}
