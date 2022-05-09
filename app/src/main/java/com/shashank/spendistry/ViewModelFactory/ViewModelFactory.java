package com.shashank.spendistry.ViewModelFactory;

import android.app.Application;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.shashank.spendistry.ViewModels.AllInvoiceViewModel;
import com.shashank.spendistry.ViewModels.BusinessInvoiceViewModel;
import com.shashank.spendistry.ViewModels.BusinessViewModel;
import com.shashank.spendistry.ViewModels.DashboardFragmentViewModel;
import com.shashank.spendistry.ViewModels.ReportedInvoiceViewModel;
import com.shashank.spendistry.ViewModels.ReturnedInvoiceViewModel;
import com.shashank.spendistry.ViewModels.SingleReportedViewModel;

@SuppressWarnings("ALL")
public class ViewModelFactory extends androidx.lifecycle.ViewModelProvider.NewInstanceFactory {

    private final Object[] mParams;
    private final Application mApplication;

    public ViewModelFactory(Application application, Object... params) {
        mParams = params;
        mApplication = application;
    }

    @NonNull
    @Override
    public <T extends androidx.lifecycle.ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        if (modelClass.isAssignableFrom(BusinessViewModel.class)) {
            return (T) new BusinessViewModel(mApplication, (String) mParams[0]);
        } else if (modelClass.isAssignableFrom(DashboardFragmentViewModel.class)) {
            return (T) new DashboardFragmentViewModel(mApplication, (LinearLayout) mParams[0], (String) mParams[1]);
        } else if (modelClass.isAssignableFrom(ReportedInvoiceViewModel.class)) {
            return (T) new ReportedInvoiceViewModel(mApplication, (String) mParams[0]);
        } else if (modelClass.isAssignableFrom(BusinessInvoiceViewModel.class)) {
            return (T) new BusinessInvoiceViewModel(mApplication, (String) mParams[0], (String) mParams[1]);
        } else if (modelClass.isAssignableFrom(AllInvoiceViewModel.class)) {
            return (T) new AllInvoiceViewModel(mApplication, (String) mParams[0]);
        } else if (modelClass.isAssignableFrom(ReturnedInvoiceViewModel.class)) {
            return (T) new ReturnedInvoiceViewModel(mApplication, (String) mParams[0]);
        } else if (modelClass.isAssignableFrom(SingleReportedViewModel.class)) {
            return (T) new SingleReportedViewModel(mApplication,(String) mParams[0],(String) mParams[1], (String) mParams[2]);
        }
        else {
            throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
        }
    }
}
