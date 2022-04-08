package com.shashank.spendistry.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shashank.spendistry.Models.ItemPrices;
import com.shashank.spendistry.R;

import java.util.ArrayList;
import java.util.List;

public class SubInvoiceAdapter extends RecyclerView.Adapter<SubInvoiceAdapter.MyViewHolder> {

    private List<ItemPrices> itemPricesList;
    private Context context;

    public SubInvoiceAdapter(List<ItemPrices> itemPricesList, Context context) {
        this.itemPricesList = itemPricesList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.invoice_recyclerview, parent, false);
        return new MyViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.itemPrice.setText("₹"+itemPricesList.get(position).getPrice());
        holder.itemName.setText(itemPricesList.get(position).getItemName());
        if (itemPricesList.get(position).getQuantity()!=null){
            holder.itemQuantity.setText(itemPricesList.get(position).getQuantity());
            holder.itemTotal.setText("₹"+ Integer.parseInt(itemPricesList.get(position).getPrice()) * Integer.parseInt(itemPricesList.get(position).getQuantity()));

        } else {
            holder.itemQuantity.setText("1");
            holder.itemTotal.setText("₹"+itemPricesList.get(position).getPrice());
        }
    }

    @Override
    public int getItemCount() {
        return itemPricesList.size();
    }
    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView itemName;
        TextView itemPrice;
        TextView itemQuantity;
        TextView itemTotal;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemNameInvoice);
            itemPrice = itemView.findViewById(R.id.PriceInvoice);
            itemQuantity = itemView.findViewById(R.id.QuantityInvoice);
            itemTotal = itemView.findViewById(R.id.TotalInvoice);
        }
    }


    public String getId(int position){
        return itemPricesList.get(position).getId();
    }

    public String getBarcode(int position){
        return itemPricesList.get(position).getBarcode();
    }

    public String getItemName(int position){
        return itemPricesList.get(position).getItemName();
    }

    public String getPrice(int position){
        return itemPricesList.get(position).getPrice();
    }

    public String getQuantity(int position){
        return itemPricesList.get(position).getQuantity();
    }

    public String getTotal(int position){
        return itemPricesList.get(position).getTotal();
    }

    public ArrayList<ItemPrices> getItemPricesList() {
        return (ArrayList<ItemPrices>) itemPricesList;
    }

    public ItemPrices updateItem(int position,ItemPrices itemPrices){
        itemPricesList.add(position, itemPrices);
        notifyItemInserted(position);
        return itemPrices;
    }
    public ItemPrices recentRemove(int position){
        ItemPrices itemPrices = itemPricesList.get(position);
        itemPricesList.remove(position);
        notifyItemRemoved(position);
        return itemPrices;
    }

    public void undoRecent(int position, ItemPrices itemPrices){
        itemPricesList.add(position, itemPrices);
        notifyItemInserted(position);
    }
}
