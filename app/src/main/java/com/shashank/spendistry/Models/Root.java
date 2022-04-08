package com.shashank.spendistry.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Root {
    private String _id;

    private ArrayList<BusinessName> businessName;

    public Root(String _id, ArrayList<BusinessName> businessName) {
        this._id = _id;
        this.businessName = businessName;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public ArrayList<BusinessName> getBusinessName() {
        return businessName;
    }

    public void setBusinessName(ArrayList<BusinessName> businessName) {
        this.businessName = businessName;
    }
}
