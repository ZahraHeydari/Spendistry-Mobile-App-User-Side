package com.shashank.spendistry.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.shashank.spendistry.Models.Dashboard;
import com.shashank.spendistry.Repositories.DashboardRepository;

public class DashboardViewModel extends AndroidViewModel {
    DashboardRepository dashboardRepository;
    public DashboardViewModel(@NonNull Application application) {
        super(application);
        dashboardRepository = new DashboardRepository(application);
    }
     public LiveData<Dashboard> getDashboard(String userId){
        return dashboardRepository.getDashboard(userId);
    }
}
