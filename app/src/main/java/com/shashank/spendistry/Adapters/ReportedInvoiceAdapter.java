package com.shashank.spendistry.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.snackbar.Snackbar;
import com.shashank.spendistry.Models.Report;
import com.shashank.spendistry.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportedInvoiceAdapter extends RecyclerView.Adapter<ReportedInvoiceAdapter.ViewHolder> {

    private ArrayList<Report> reportInvoiceList;
    private Context context;
    private Activity activity;
    private SharedPreferences sharedPreferences;
    private LinearLayout linearLayout;

    public ReportedInvoiceAdapter(ArrayList<Report> reportInvoiceList, Context context, Activity activity, LinearLayout linearLayout) {
        this.reportInvoiceList = reportInvoiceList;
        this.context = context;
        this.activity = activity;
        this.linearLayout = linearLayout;
        sharedPreferences = context.getSharedPreferences("loggedIn", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public ReportedInvoiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.reported_invoices, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ReportedInvoiceAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText("Business: "+reportInvoiceList.get(position).getBusinessName());
        holder.email.setText("Email: "+reportInvoiceList.get(position).getBusinessEmail());
        holder.contact.setText("Phone: "+reportInvoiceList.get(position).getBusinessPhone());
        Date time = new Date(Long.parseLong(reportInvoiceList.get(position).getDate()));
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy 'Time: ' hh:mm:ss aaa");
        String date = sdf.format(time);
        holder.date.setText("Date: "+date);
        holder.reason.setText("Reason: "+reportInvoiceList.get(position).getReason());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Snackbar snackbar = Snackbar.make(linearLayout, "Swipe left to take action", Snackbar.LENGTH_SHORT);
                snackbar.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(context.getResources().getColor(R.color.mainBlue));
                snackbar.show();
                return true;
            }
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return reportInvoiceList.size();
    }

    public Report recentRemove(int position){
        Report report = reportInvoiceList.get(position);
        reportInvoiceList.remove(position);
        notifyItemRemoved(position);
        return report;
    }

    public void undoRecent(int position, Report report){
        reportInvoiceList.add(position, report);
        notifyItemInserted(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView email;
        TextView contact;
        TextView reason;
        TextView date;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.client_name);
            email = itemView.findViewById(R.id.client_email);
            contact = itemView.findViewById(R.id.client_phone);
            reason = itemView.findViewById(R.id.client_reason);
            date = itemView.findViewById(R.id.generated_date);
        }
    }

}

