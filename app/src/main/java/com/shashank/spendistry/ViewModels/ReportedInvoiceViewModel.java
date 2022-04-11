package com.shashank.spendistry.ViewModels;

import android.app.Application;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.shashank.spendistry.Models.Report;
import com.shashank.spendistry.Repositories.InvoiceRepository;

import java.util.List;

public class ReportedInvoiceViewModel extends AndroidViewModel {
    MutableLiveData<List<Report>> report;
    MutableLiveData<String> stringMutableLiveData = new MutableLiveData<>();
    InvoiceRepository invoiceRepository;
    public ReportedInvoiceViewModel(@NonNull Application application, String userId) {
        super(application);
        invoiceRepository = new InvoiceRepository(application);
        report = invoiceRepository.getReportedInvoices();
        invoiceRepository.setUserId(userId);
    }

    public MutableLiveData<List<Report>> getReport() {
        return report;
    }
    public void deleteReportRequest(LinearLayout linearLayout, String reportId){
        invoiceRepository.deleteReportRequest(linearLayout, reportId);
    }

    public void setUserId(String email) {
        invoiceRepository.setUserId(email);
    }
}
