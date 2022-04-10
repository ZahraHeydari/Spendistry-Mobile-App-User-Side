package com.shashank.spendistry.Models;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@Entity(tableName = "dashboard")
public class Dashboard implements Parcelable {
    @SerializedName("_id")
    @ColumnInfo(name = "id")
    @PrimaryKey
    @NonNull
    private String id;

    public String email;
    @SerializedName("MonthlyTotalAll")
    public int monthlyTotalAll;
    @SerializedName("AllTimeTotal")
    public int allTimeTotal;
    public String encryptedQr;
    public UserDetails userDetails;
    public int reportCount;
    public ArrayList<BusinessDetail> businessDetails;


    public Dashboard(String email, int monthlyTotalAll, int allTimeTotal, String encryptedQr, UserDetails userDetails, ArrayList<BusinessDetail> businessDetails, int reportCount) {
        this.email = email;
        this.monthlyTotalAll = monthlyTotalAll;
        this.allTimeTotal = allTimeTotal;
        this.encryptedQr = encryptedQr;
        this.userDetails = userDetails;
        this.businessDetails = businessDetails;
        this.reportCount = reportCount;
    }

    protected Dashboard(Parcel in) {
        email = in.readString();
        monthlyTotalAll = in.readInt();
        allTimeTotal = in.readInt();
        encryptedQr = in.readString();
        reportCount = in.readInt();
    }

    public static final Creator<Dashboard> CREATOR = new Creator<Dashboard>() {
        @Override
        public Dashboard createFromParcel(Parcel in) {
            return new Dashboard(in);
        }

        @Override
        public Dashboard[] newArray(int size) {
            return new Dashboard[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getReportCount() {
        return reportCount;
    }

    public void setReportCount(int reportCount) {
        this.reportCount = reportCount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getMonthlyTotalAll() {
        return monthlyTotalAll;
    }

    public void setMonthlyTotalAll(int monthlyTotalAll) {
        this.monthlyTotalAll = monthlyTotalAll;
    }

    public int getAllTimeTotal() {
        return allTimeTotal;
    }

    public void setAllTimeTotal(int allTimeTotal) {
        this.allTimeTotal = allTimeTotal;
    }

    public String getEncryptedQr() {
        return encryptedQr;
    }

    public void setEncryptedQr(String encryptedQr) {
        this.encryptedQr = encryptedQr;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public ArrayList<BusinessDetail> getBusinessDetails() {
        return businessDetails;
    }

    public void setBusinessDetails(ArrayList<BusinessDetail> businessDetails) {
        this.businessDetails = businessDetails;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(email);
        parcel.writeInt(monthlyTotalAll);
        parcel.writeInt(allTimeTotal);
        parcel.writeString(encryptedQr);
        parcel.writeInt(reportCount);
    }
}




