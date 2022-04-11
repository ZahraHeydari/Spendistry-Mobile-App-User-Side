package com.shashank.spendistry.ViewModels;

import android.app.Application;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.shashank.spendistry.Models.Dashboard;
import com.shashank.spendistry.Repositories.DashboardRepository;

public class DashboardViewModel extends AndroidViewModel {
    private DashboardRepository dashboardRepository;
    private MutableLiveData<Dashboard> dashboard;
    public DashboardViewModel(@NonNull Application application, LinearLayout linearLayout, String userId) {
        super(application);
        dashboardRepository = new DashboardRepository(application);
        dashboard = dashboardRepository.getDashboard(linearLayout, userId);
    }

    public MutableLiveData<Dashboard> getDashboard() {
        return dashboard;
    }
}
