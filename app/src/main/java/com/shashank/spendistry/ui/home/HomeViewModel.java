package com.shashank.spendistry.ui.home;

import android.app.Application;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shashank.spendistry.Models.Dashboard;
import com.shashank.spendistry.Repositories.DashboardRepository;

public class HomeViewModel extends ViewModel {

    private DashboardRepository dashboardRepository;

    public HomeViewModel(Application application) {
        dashboardRepository = new DashboardRepository(application);
    }

    public MutableLiveData<Dashboard> getDashboard(LinearLayout linearLayout, String userId) {
        return dashboardRepository.getDashboard(linearLayout,userId);
    }

}