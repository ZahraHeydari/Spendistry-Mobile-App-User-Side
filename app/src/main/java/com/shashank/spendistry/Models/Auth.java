package com.shashank.spendistry.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Auth implements Parcelable {

    @SerializedName("_id")
    private String email;

    private String password;

    private String message;

    public Auth(String email, String password, String message) {
        this.email = email;
        this.password = password;
        this.message = message;
    }

    public Auth(String email) {
        this.email = email;
    }

    public Auth(){

    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static final Creator<Auth> CREATOR = new Creator<Auth>() {
        @Override
        public Auth createFromParcel(Parcel in) {
            return new Auth(in);
        }

        @Override
        public Auth[] newArray(int size) {
            return new Auth[size];
        }
    };

    public Auth(Parcel in) {
        this.email = in.readString();
        this.password = in.readString();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


    public Auth(String email, String password) {
        this.email = email;
        this.password = password;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(email);
        parcel.writeString(password);
    }
}
