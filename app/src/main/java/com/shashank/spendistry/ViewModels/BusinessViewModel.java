package com.shashank.spendistry.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.shashank.spendistry.Models.Vendor;
import com.shashank.spendistry.Repositories.BusinessRepository;

public class BusinessViewModel extends AndroidViewModel {
    private BusinessRepository businessRepository;
    public BusinessViewModel(@NonNull Application application) {
        super(application);
        businessRepository = new BusinessRepository(application);
    }

    public MutableLiveData<Vendor> getVendor(String businessId) {
        return businessRepository.getVendor(businessId);
    }
}
