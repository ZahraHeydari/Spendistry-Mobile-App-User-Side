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
    public DashboardViewModel(@NonNull Application application) {
        super(application);
        dashboardRepository = new DashboardRepository(application);
    }

    public MutableLiveData<Dashboard> getDashboard(LinearLayout linearLayout,String userId) {
        return dashboardRepository.getDashboard(linearLayout,userId);
    }
}
