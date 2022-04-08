package com.shashank.spendistry.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class UserDetails implements Parcelable {
    public String _id;
    public String fname;
    public String lname;
    public String email;
    public String mobileNumber;
    public String address;
    @SerializedName("Date")
    public String date;

    public UserDetails(String _id, String fname, String lname, String email, String mobileNumber, String address, String date) {
        this._id = _id;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.date = date;
    }

    public UserDetails(String _id, String fname, String lname, String mobileNumber, String address) {
        this._id = _id;
        this.fname = fname;
        this.lname = lname;
        this.email = _id;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.date = date;
    }

    protected UserDetails(Parcel in) {
        _id = in.readString();
        fname = in.readString();
        lname = in.readString();
        email = in.readString();
        mobileNumber = in.readString();
        address = in.readString();
        date = in.readString();
    }

    public static final Creator<UserDetails> CREATOR = new Creator<UserDetails>() {
        @Override
        public UserDetails createFromParcel(Parcel in) {
            return new UserDetails(in);
        }

        @Override
        public UserDetails[] newArray(int size) {
            return new UserDetails[size];
        }
    };

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeString(fname);
        parcel.writeString(lname);
        parcel.writeString(email);
        parcel.writeString(mobileNumber);
        parcel.writeString(address);
        parcel.writeString(date);
    }
}
