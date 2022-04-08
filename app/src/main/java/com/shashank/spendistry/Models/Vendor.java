package com.shashank.spendistry.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Vendor implements Parcelable {

    @SerializedName("fname")
    private String firstName;

    @SerializedName("lname")
    private String lastName;
    private String email;

    @SerializedName("_id")
    private String vendorId;

    @SerializedName("vendorName")
    private String businessName;

    private String mobileNumber;
    private int currentInvoiceNumber;
    private String panNumber;
    private String gstNumber;
    private String address;
    private String city;
    private String state;
    private String tollFreeNumber;
    private String website;
    private String lat;
    private String lng;
    private String vendorDescription;

    public Vendor(String firstName, String lastName, String email, String vendorId, String businessName, String mobileNumber, int currentInvoiceNumber, String panNumber, String gstNumber, String address, String city, String state, String tollFreeNumber, String website, String description) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.vendorId = vendorId;
        this.businessName = businessName;
        this.mobileNumber = mobileNumber;
        this.currentInvoiceNumber = currentInvoiceNumber;
        this.panNumber = panNumber;
        this.gstNumber = gstNumber;
        this.address = address;
        this.city = city;
        this.state = state;
        this.tollFreeNumber = tollFreeNumber;
        this.website = website;
        vendorDescription = description;
    }

    public Vendor(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Vendor(int currentInvoiceNumber) {
        this.currentInvoiceNumber = currentInvoiceNumber;
    }

    public Vendor(String firstName, String lastName, String email, String businessName, String mobileNumber, String panNumber, String gstNumber, String address, String city, String state, String tollFreeNumber, String website, String description) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.vendorId = email;
        this.businessName = businessName;
        this.mobileNumber = mobileNumber;
        this.panNumber = panNumber;
        this.gstNumber = gstNumber;
        this.address = address;
        this.city = city;
        this.state = state;
        this.tollFreeNumber = tollFreeNumber;
        this.website = website;
        vendorDescription = description;
    }


    protected Vendor(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        vendorId = in.readString();
        businessName = in.readString();
        mobileNumber = in.readString();
        currentInvoiceNumber = in.readInt();
        panNumber = in.readString();
        gstNumber = in.readString();
        address = in.readString();
        city = in.readString();
        state = in.readString();
        tollFreeNumber = in.readString();
        website = in.readString();
        lat = in.readString();
        lng = in.readString();
        vendorDescription = in.readString();
    }

    public static final Creator<Vendor> CREATOR = new Creator<Vendor>() {
        @Override
        public Vendor createFromParcel(Parcel in) {
            return new Vendor(in);
        }

        @Override
        public Vendor[] newArray(int size) {
            return new Vendor[size];
        }
    };

    public String getBusinessName() {
        return businessName;
    }

    public String getState() {
        return state;
    }

    public String getDescription() {
        return vendorDescription;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public int getCurrentInvoiceNumber() {
        return currentInvoiceNumber;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getTollFreeNumber() {
        return tollFreeNumber;
    }

    public String getWebsite() {
        return website;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setCurrentInvoiceNumber(int currentInvoiceNumber) {
        this.currentInvoiceNumber = currentInvoiceNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(email);
        parcel.writeString(vendorId);
        parcel.writeString(businessName);
        parcel.writeString(mobileNumber);
        parcel.writeInt(currentInvoiceNumber);
        parcel.writeString(panNumber);
        parcel.writeString(gstNumber);
        parcel.writeString(address);
        parcel.writeString(city);
        parcel.writeString(state);
        parcel.writeString(tollFreeNumber);
        parcel.writeString(website);
        parcel.writeString(lat);
        parcel.writeString(lng);
        parcel.writeString(vendorDescription);
    }
}
