package com.shashank.spendistry.ViewModels;

import android.app.Application;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.shashank.spendistry.Models.Dashboard;
import com.shashank.spendistry.Repositories.DashboardRepository;

public class DashboardFragmentViewModel extends AndroidViewModel {
    private DashboardRepository dashboardRepository;
    private String userId;
    public DashboardFragmentViewModel(@NonNull Application application, LinearLayout linearLayout, String userId) {
        super(application);
        dashboardRepository = new DashboardRepository(application);
        this.userId = userId;
        dashboardRepository.fetchDashboardData(linearLayout,userId);
    }


    public LiveData<Dashboard> getDashboard() {
        return dashboardRepository.getDashboard(userId);
    }

    public void refreshData(LinearLayout linearLayout, String email){
        dashboardRepository.fetchDashboardData(linearLayout,email);
    }
}
