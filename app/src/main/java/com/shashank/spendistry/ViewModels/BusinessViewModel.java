package com.shashank.spendistry.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.shashank.spendistry.Models.Vendor;
import com.shashank.spendistry.Repositories.BusinessRepository;

public class BusinessViewModel extends AndroidViewModel {
    private BusinessRepository businessRepository;
    MutableLiveData<Vendor> vendorMutableLiveData;
    public BusinessViewModel(@NonNull Application application, String businessId) {
        super(application);
        businessRepository = new BusinessRepository(application);
        vendorMutableLiveData = businessRepository.getVendor(businessId);
    }

    public MutableLiveData<Vendor> getVendor() {
        return vendorMutableLiveData;
    }
}
