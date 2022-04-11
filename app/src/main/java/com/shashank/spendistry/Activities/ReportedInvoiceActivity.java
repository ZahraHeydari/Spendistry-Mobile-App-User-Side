package com.shashank.spendistry.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.shashank.spendistry.Adapters.ReportedInvoiceAdapter;
import com.shashank.spendistry.Models.Report;
import com.shashank.spendistry.R;
import com.shashank.spendistry.ViewModelFactory.ViewModelFactory;
import com.shashank.spendistry.ViewModels.ReportedInvoiceViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ReportedInvoiceActivity extends AppCompatActivity {

    private ReportedInvoiceViewModel reportedInvoiceViewModel;
    private  String email;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int recentPosition;
    private ReportedInvoiceAdapter reportedInvoiceAdapter;
    private RecyclerView recyclerView;
    private Dialog dialog;
    private LinearLayout linearLayout;
    private TextView toolbarTitle;
    private ArrayList<Report> reportArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reported_invoice);
        setSupportActionBar(findViewById(R.id.toolbar_reported));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle("");
        }
        recyclerView = findViewById(R.id.reported_invoices);
        linearLayout = findViewById(R.id.reported_invoice_layout);
        toolbarTitle = findViewById(R.id.reported_title);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_report);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.loading_layout);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        SharedPreferences sharedPreferences = getSharedPreferences("loggedIn",MODE_PRIVATE);
        email = sharedPreferences.getString("email","");
        reportedInvoiceViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelFactory(getApplication(),email)).get(ReportedInvoiceViewModel.class);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this,R.color.mainBlue),ContextCompat.getColor(this,R.color.cardBlue), ContextCompat.getColor(this,R.color.windowBlue));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reportedInvoiceViewModel.setUserId(email);
            }
        });
        //fetch data and set adapter
        loadData();





        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {
                    recentPosition = viewHolder.getBindingAdapterPosition();
                    Report report = reportedInvoiceAdapter.recentRemove(viewHolder.getBindingAdapterPosition());
                    deleteDialog(report, linearLayout);

                }

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(ReportedInvoiceActivity.this, R.color.windowBlue))
                        .addSwipeLeftActionIcon(R.drawable.delete)
                        .addSwipeLeftLabel("DELETE")
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(ReportedInvoiceActivity.this, R.color.windowBlue))
                        .addSwipeRightActionIcon(R.drawable.edit)
                        .addSwipeRightLabel("EDIT")
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(recyclerView);

    }
    private void loadData(){
        reportedInvoiceViewModel.getReport().observe(this, new Observer<List<Report>>() {
            @Override
            public void onChanged(List<Report> reports) {
                Collections.reverse(reports);
                reportedInvoiceAdapter = new ReportedInvoiceAdapter((ArrayList<Report>) reports, ReportedInvoiceActivity.this, ReportedInvoiceActivity.this, linearLayout);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ReportedInvoiceActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(reportedInvoiceAdapter);
                recyclerView.setHasFixedSize(true);
                swipeRefreshLayout.setRefreshing(false);
                dialog.dismiss();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void deleteDialog(Report report, LinearLayout linearLayout) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.delete_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        Button remove = dialog.findViewById(R.id.delete_btn);
        Button cancel = dialog.findViewById(R.id.cancel_delete_btn);
        TextView clientEmail = dialog.findViewById(R.id.barcode_delete);
        TextView reason = dialog.findViewById(R.id.item_price_delete);
        remove.setText("Remove");
        cancel.setText("Cancel");
        clientEmail.setText("Client: " + report.getClientEmail());
        reason.setText("Reason: " + report.getReason());
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportedInvoiceViewModel.deleteReportRequest(linearLayout, report.getId());
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportedInvoiceAdapter.undoRecent(recentPosition, report);
                dialog.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.invoice_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.invoice_search).getActionView();
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {
                    toolbarTitle.setVisibility(View.VISIBLE);
                } else {
                    toolbarTitle.setVisibility(View.GONE);
                    reportArrayList = reportedInvoiceAdapter.getArrayList();
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
                reportedInvoiceAdapter.searchQuery(s, reportArrayList);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

}