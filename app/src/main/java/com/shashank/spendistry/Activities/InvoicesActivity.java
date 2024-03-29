package com.shashank.spendistry.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.shashank.spendistry.Adapters.InvoicesAdapter;
import com.shashank.spendistry.Models.Invoice;
import com.shashank.spendistry.R;
import com.shashank.spendistry.ViewModelFactory.ViewModelFactory;
import com.shashank.spendistry.ViewModels.AllInvoiceViewModel;
import com.shashank.spendistry.ViewModels.BusinessInvoiceViewModel;
import com.shashank.spendistry.ViewModels.ReturnedInvoiceViewModel;
import com.shashank.spendistry.ViewModels.SingleReportedViewModel;

import java.util.ArrayList;

public class InvoicesActivity extends AppCompatActivity {
    private InvoicesAdapter invoicesAdapter;
    private ArrayList<Invoice> AllInvoices;
    private TextView toolbarTitle;
    private LinearLayout linearLayout;
    private RecyclerView recyclerView;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoices);
        Toolbar toolbar = findViewById(R.id.invoices_toolbar);
        linearLayout = findViewById(R.id.activity_invoices);
        recyclerView = findViewById(R.id.invoicesRecyclerView);
        toolbarTitle = findViewById(R.id.invoices_toolbar_title);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        String activity = intent.getStringExtra("activity");
        String business_Email = "";
        String email = getSharedPreferences("loggedIn", MODE_PRIVATE).getString("email", "");
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.loading_layout);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        switch (activity) {
            case "dashboard":
                business_Email = intent.getStringExtra("business_email");
                toolbarTitle.setText("Invoices");
                BusinessInvoiceViewModel businessInvoiceViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelFactory(getApplication(), email, business_Email)).get(BusinessInvoiceViewModel.class);
                businessInvoiceViewModel.getBusinessInvoices().observe(this, new Observer<ArrayList<Invoice>>() {
                    @Override
                    public void onChanged(ArrayList<Invoice> invoices) {
                        getInvoices(invoices, false, dialog);
                    }
                });
                break;
            case "all":
                toolbarTitle.setText("All Invoices");
                AllInvoiceViewModel allInvoiceViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelFactory(getApplication(), email)).get(AllInvoiceViewModel.class);
                allInvoiceViewModel.getAllInvoices().observe(this, new Observer<ArrayList<Invoice>>() {
                    @Override
                    public void onChanged(ArrayList<Invoice> invoices) {
                        getInvoices(invoices, false, dialog);
                    }
                });
                break;
            case "reported":
                business_Email = intent.getStringExtra("business_email");
                String invoiceId = intent.getStringExtra("invoiceId");
                toolbarTitle.setText("Reported Invoices");
                SingleReportedViewModel singleReportedViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelFactory(getApplication(), email, business_Email, invoiceId)).get(SingleReportedViewModel.class);
                singleReportedViewModel.getSingleReportedInvoice().observe(this, new Observer<ArrayList<Invoice>>() {
                    @Override
                    public void onChanged(ArrayList<Invoice> invoices) {
                        getInvoices(invoices, true, dialog);
                    }
                });

                break;
            default:
                toolbarTitle.setText("Returned Invoices");
                ReturnedInvoiceViewModel returnedInvoiceViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelFactory(getApplication(), email)).get(ReturnedInvoiceViewModel.class);
                returnedInvoiceViewModel.getReturnedInvoices().observe(this, new Observer<ArrayList<Invoice>>() {
                    @Override
                    public void onChanged(ArrayList<Invoice> invoices) {
                        getInvoices(invoices, true, dialog);
                    }
                });
                break;
        }
    }

    public void getInvoices(ArrayList<Invoice> invoices, boolean returned, Dialog dialog) {
        invoicesAdapter = new InvoicesAdapter(invoices, linearLayout ,InvoicesActivity.this, returned);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(InvoicesActivity.this);
        recyclerView.setAdapter(invoicesAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        dialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.invoice_menu, menu);
        android.widget.SearchView searchView = (android.widget.SearchView) menu.findItem(R.id.invoice_search).getActionView();
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {
                    toolbarTitle.setVisibility(View.VISIBLE);
                } else {
                    toolbarTitle.setVisibility(View.GONE);
                    AllInvoices = invoicesAdapter.getArrayList();
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                invoicesAdapter.searchQuery(s, AllInvoices);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}