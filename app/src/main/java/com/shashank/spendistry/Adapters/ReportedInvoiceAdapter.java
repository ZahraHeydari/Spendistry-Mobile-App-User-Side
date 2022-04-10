package com.shashank.spendistry.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import com.shashank.spendistry.BusinessProfileActivity;
import com.shashank.spendistry.InvoicesActivity;
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
        for (Report report : reportInvoiceList) {
            Date time = new Date(Long.parseLong(report.getTime()));
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy 'Time: ' hh:mm:ss aaa");
            String date = sdf.format(time);
            report.setDate(date);
        }
    }

    @NonNull
    @Override
    public ReportedInvoiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.reported_invoices, parent, false);
        return new ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ReportedInvoiceAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText("Business: " + reportInvoiceList.get(position).getBusinessName());
        holder.email.setText("Email: " + reportInvoiceList.get(position).getBusinessEmail());
        if (reportInvoiceList.get(position).getBusinessPhone() != null) {
            holder.contact.setText("Phone: " + reportInvoiceList.get(position).getBusinessPhone());
        } else {
            holder.contact.setVisibility(View.GONE);
        }

        holder.date.setText("Date: " + reportInvoiceList.get(position).getDate());
        holder.reason.setText("Reason: " + reportInvoiceList.get(position).getReason());
        holder.name.setPaintFlags(holder.name.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.email.setPaintFlags(holder.email.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.contact.setPaintFlags(holder.contact.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open email app to compose email
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + reportInvoiceList.get(position).getBusinessEmail()));
                activity.startActivity(intent);
            }
        });

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open business profile activity
                Intent intent = new Intent(context, BusinessProfileActivity.class);
                intent.putExtra("businessId", reportInvoiceList.get(position).getBusinessEmail());
                context.startActivity(intent);
            }
        });

        holder.contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open phone app to call
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + reportInvoiceList.get(position).getBusinessPhone()));
                activity.startActivity(intent);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open invoice activity
                Intent intent = new Intent(context, InvoicesActivity.class);
                intent.putExtra("activity", "reported");
                intent.putExtra("invoiceId", reportInvoiceList.get(position).getInvoiceID());
                intent.putExtra("business_email", reportInvoiceList.get(position).getBusinessEmail());
                context.startActivity(intent);
            }
        });

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

    public ArrayList<Report> getArrayList() {
        return reportInvoiceList;
    }

    public void searchQuery(String query, ArrayList<Report> reportInvoiceList) {
        ArrayList<Report> searchList = new ArrayList<>();
        if (!query.isEmpty()) {
            for (Report report : reportInvoiceList) {
                if (report.getBusinessName().toLowerCase().contains(query.toLowerCase())) {
                    searchList.add(report);
                } else if (report.getBusinessEmail().toLowerCase().contains(query.toLowerCase())) {
                    searchList.add(report);
                } else if (report.getReason().toLowerCase().contains(query.toLowerCase())) {
                    searchList.add(report);
                } else if (report.getDate().toLowerCase().contains(query.toLowerCase())) {
                    searchList.add(report);
                }
            }
            this.reportInvoiceList = searchList;
        } else {
            this.reportInvoiceList = reportInvoiceList;
        }
        notifyDataSetChanged();
    }

    public Report recentRemove(int position) {
        Report report = reportInvoiceList.get(position);
        reportInvoiceList.remove(position);
        notifyItemRemoved(position);
        return report;
    }

    public void undoRecent(int position, Report report) {
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

