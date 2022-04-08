package com.shashank.spendistry.Constants;

import androidx.lifecycle.MutableLiveData;

import com.shashank.spendistry.Models.ItemPrices;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static final String DATABASE_NAME = "Spendistry";
    public static final int DATABASE_VERSION = 1;
    public static final String API_URL = "https://cdbd-18-212-22-122.ngrok.io/";
    public static ArrayList<ItemPrices> itemPricesArrayList = new ArrayList<>();
    public static MutableLiveData<List<ItemPrices>> data = new MutableLiveData<>();
}
