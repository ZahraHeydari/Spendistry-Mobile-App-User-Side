package com.shashank.spendistry.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shashank.spendistry.Constants.Constants;
import com.shashank.spendistry.InvoicesActivity;
import com.shashank.spendistry.Models.BusinessDetail;
import com.shashank.spendistry.Models.Invoice;
import com.shashank.spendistry.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @Override
    public void onBindViewHolder(@NonNull DashboardInvoiceAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.business_email.setText(businessDetails.get(position).getBusinessEmail());
        holder.business_monthly_exp.setText(" ₹"+businessDetails.get(position).getMonthlyTotal());
        holder.business_all_exp.setText(" ₹"+businessDetails.get(position).getAllTotal());
        Glide.with(activity).load(Constants.API_URL+"vendorProfile/"+businessDetails.get(position).getBusinessEmail()+".jpeg")
                .placeholder(R.drawable.loading).error(R.drawable.no_profile) .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .apply(RequestOptions.skipMemoryCacheOf(true)).into(holder.business_image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, InvoicesActivity.class);
                intent.putExtra("business_email",businessDetails.get(position).getBusinessEmail());
                intent.putExtra("activity","dashboard");
                activity.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return businessDetails.size();
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
