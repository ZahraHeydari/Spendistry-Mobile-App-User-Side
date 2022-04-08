package com.shashank.spendistry.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BusinessName{
    private String _id;

    @SerializedName("invoices")
    private ArrayList<Invoice> invoices;

    public BusinessName(String _id, ArrayList<Invoice> invoices) {
        this._id = _id;
        this.invoices = invoices;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public ArrayList<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(ArrayList<Invoice> invoices) {
        this.invoices = invoices;
    }
}


