package com.shashank.spendistry.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shashank.spendistry.Constants.Constants;
import com.shashank.spendistry.Activities.InvoicesActivity;
import com.shashank.spendistry.Models.BusinessDetail;
import com.shashank.spendistry.R;

import java.util.ArrayList;
import java.util.Random;

public class DashboardInvoiceAdapter extends RecyclerView.Adapter<DashboardInvoiceAdapter.ViewHolder>{


    private ArrayList<BusinessDetail> businessDetails;
    private Context context;
    private Activity activity;


    public DashboardInvoiceAdapter(ArrayList<BusinessDetail> businessDetails, Context context, Activity activity) {
//        Collections.reverse(businessDetails);
        this.businessDetails = businessDetails;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public DashboardInvoiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dashboard_invoice, parent, false);
        return new DashboardInvoiceAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DashboardInvoiceAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.business_email.setText(businessDetails.get(position).getBusinessEmail());
        holder.business_monthly_exp.setText(" ₹"+businessDetails.get(position).getMonthlyTotal());
        holder.business_all_exp.setText(" ₹"+businessDetails.get(position).getAllTotal());
        Glide.with(activity).load(Constants.API_URL+"vendorProfile/"+businessDetails.get(position).getBusinessEmail()+".jpeg")
                .placeholder(R.drawable.loading).error(R.drawable.no_profile).into(holder.business_image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (isConnected()) {
                        Intent intent = new Intent(activity, InvoicesActivity.class);
                        intent.putExtra("business_email", businessDetails.get(position).getBusinessEmail());
                        intent.putExtra("activity", "dashboard");
                        activity.startActivity(intent);
                    } else {
                        Snackbar snackbar = Snackbar.make(activity.findViewById(R.id.linear_layout), "Internet is not available", Snackbar.LENGTH_SHORT);
                        snackbar.setTextColor(Color.WHITE);
                        snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.red));
                        snackbar.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return businessDetails.size();
    }

    public void setBusinessDetails(ArrayList<BusinessDetail> businessDetails) {
        this.businessDetails = businessDetails;
        notifyDataSetChanged();
    }

    //get Business Details
    public BusinessDetail getBusinessDetails(int position) {
        return businessDetails.get(position);
    }

    public boolean isConnected() throws Exception {
        String command="";
        command = "ping -c 1 "+Constants.API_URL.replace("https://","").replace("/","");
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void searchQuery(String query, ArrayList<BusinessDetail> allData){
        ArrayList<BusinessDetail> searchList = new ArrayList<>();
        if (!query.equals("")) {
            for (BusinessDetail businessDetail : allData) {
                if (businessDetail.getBusinessEmail().toLowerCase().contains(query.toLowerCase())) {
                    searchList.add(businessDetail);
                }
            }
            businessDetails = searchList;
            notifyDataSetChanged();
        } else {
            businessDetails = allData;
            notifyDataSetChanged();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView business_email, business_monthly_exp, business_all_exp;
        RoundedImageView business_image;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            business_email = itemView.findViewById(R.id.business_email);
            business_monthly_exp = itemView.findViewById(R.id.business_monthly_exp);
            business_all_exp = itemView.findViewById(R.id.business_all_exp);
            business_image = itemView.findViewById(R.id.business_profile);
        }
    }
}
