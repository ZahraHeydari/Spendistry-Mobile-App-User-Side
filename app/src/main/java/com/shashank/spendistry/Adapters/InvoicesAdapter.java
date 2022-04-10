package com.shashank.spendistry.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.shashank.spendistry.Models.BusinessDetail;
import com.shashank.spendistry.Models.Invoice;
import com.shashank.spendistry.Models.Report;
import com.shashank.spendistry.R;
import com.shashank.spendistry.ViewModels.InvoiceViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;

public class InvoicesAdapter extends RecyclerView.Adapter<InvoicesAdapter.ViewHolder> {
    ArrayList<Invoice> invoices;
    Context context;
    boolean returned = false;
    LifecycleOwner lifecycleOwner;
    InvoiceViewModel invoiceViewModel;
    LinearLayout linearLayout;

    public InvoicesAdapter(ArrayList<Invoice> invoices, LinearLayout linearLayout, Context context, boolean returned) {
        this.invoices = invoices;
        this.context = context;
        this.returned = returned;
        this.lifecycleOwner = (LifecycleOwner) context;
        invoiceViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(InvoiceViewModel.class);
        this.linearLayout = linearLayout;
        for (Invoice invoice : invoices) {
            Date time = new Date(Long.parseLong(invoice.getInvoiceTime()));
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy 'Time: ' hh:mm:ss aaa", java.util.Locale.getDefault());
            String date = sdf.format(time);
            invoice.setDate(date);
        }
    }


    @NonNull
    @Override
    public InvoicesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.invoice_layout, parent, false);
        return new InvoicesAdapter.ViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull InvoicesAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Invoice invoice = invoices.get(position);
        holder.bind(invoice);
        String name = invoice.getTitle();
        //capitalize first letter
        if (name != null) {
            String firstLetter = name.substring(0, 1);
            String restOfWord = name.substring(1);

            String capitalizedFirstLetter = firstLetter.toUpperCase();
            String capitalizedRestOfWord = restOfWord.toLowerCase();
            holder.businessName.setText(capitalizedFirstLetter + capitalizedRestOfWord);
        }
        holder.businessEmail.setText(invoice.getSentBy());

        holder.businessPhone.setText(invoice.getBusinessContactNo());
        holder.customerEmail.setText(invoice.getSentTo());
        holder.invoiceTotal.setText("Total: ₹" + invoice.getTotal());
        holder.invoiceDiscount.setText("Discount: " + invoice.getDiscount() + "%");
        holder.invoiceIGST.setText("IGST: " + invoice.getIGST() + "%");
        holder.invoiceCGST.setText("CGST:" + invoice.getCGST() + "%");
        holder.invoiceSGST.setText("SGST: " + invoice.getSGST() + "%");
        holder.invoiceUTGST.setText("UTGST: " + invoice.getUTGST() + "%");
        holder.roundOff.setText("Net total: ₹" + invoice.getRoundOff() + "");
        holder.paymentMode.setText("Payment Method: " + invoice.getPaymentMode());
        holder.description.setText(invoice.getDescription());
        //string to date

        holder.invoiceTime.setText("Date:  " + invoice.getDate());
        holder.invoiceAddress.setText("BusinessAddress: " +invoice.getBusinessAddress());
        holder.notice.setText("SUBJECT TO " + invoice.getCity().toUpperCase() + " JURISDICTION ONLY");
        holder.gstNo.setText("GST No.: " + invoice.getGstNumber());
        holder.invoiceNo.setText("Invoice No.: " + invoice.getInvoiceNumber());
        SubInvoiceAdapter subInvoiceAdapter = new SubInvoiceAdapter(invoice.getTotalItems(), context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        holder.subRecyclerView.setLayoutManager(layoutManager);
        holder.subRecyclerView.setAdapter(subInvoiceAdapter);

        if (returned) {
            holder.report.setVisibility(View.GONE);
            holder.download.setVisibility(View.GONE);
        }

        holder.report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.report_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(true);
                dialog.show();
                TextView report = dialog.findViewById(R.id.report_business);
                EditText reportReason = dialog.findViewById(R.id.report_reason_edit);
                TextInputLayout reportLayout = dialog.findViewById(R.id.report_reason);
                Button reportButton = dialog.findViewById(R.id.report_button);
                report.setText("Business Email: " + invoice.getSentBy());

                reportButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String reason = reportReason.getText().toString();
                        if (!reason.isEmpty()) {
                            //send
                            invoiceViewModel.reportInvoice(new Report(invoice.getInvoiceId(),invoice.getSentTo(), invoice.getTitle(), invoice.getBusinessContactNo(),invoice.getSentBy(), reason)).observe(lifecycleOwner, new Observer<Report>() {
                                @Override
                                public void onChanged(Report report) {
                                    if (report != null) {
                                        Snackbar snackbar = Snackbar.make(linearLayout, "Invoice Reported", Snackbar.LENGTH_SHORT);
                                        snackbar.setTextColor(Color.WHITE);
                                        snackbar.setBackgroundTint(context.getResources().getColor(R.color.cardBlue));
                                        snackbar.show();
                                        dialog.dismiss();
                                    }
                                }
                            });
                        } else {
                            reportLayout.setError("Please enter a reason");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    reportLayout.setErrorEnabled(false);
                                }
                            },2000);
                        }
                    }
                });
            }
        });

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invoiceViewModel.getPDF(invoice.getSentTo(), invoice.getSentBy(), invoice.getInvoiceId()).observe(lifecycleOwner, new Observer<ResponseBody>() {
                    @Override
                    public void onChanged(ResponseBody responseBody) {
                        try {
                            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                                    "Spendistry");
                            if (!f.exists()) {
                                f.mkdirs();
                            }
                            File f1 = new File(f,  invoice.getTitle());
                            if (!f1.exists()) {
                                f1.mkdirs();
                            }
                            File file = new File(f1, invoice.getTitle() + "_"+invoice.getRoundOff()+"_"
                                    + Calendar.getInstance().getTime().getDate()
                                    +Calendar.getInstance().getTime().getHours()
                                    +Calendar.getInstance().getTime().getMinutes()
                                    +Calendar.getInstance().getTime().getSeconds()
                                    + ".pdf");
                            //write response to file
                            if (!file.exists()) {
                                InputStream inputStream = responseBody.byteStream();
                                FileOutputStream stream = new FileOutputStream(file);
                                byte[] buffer = new byte[1024];
                                int len;
                                while ((len = inputStream.read(buffer)) != -1) {
                                    stream.write(buffer, 0, len);
                                }
                                Snackbar snackbar = Snackbar.make(linearLayout, "Invoice saved in DOWNLOADS", Snackbar.LENGTH_SHORT);
                                snackbar.setTextColor(Color.WHITE);
                                snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.mainBlue));
                                snackbar.show();
                                stream.close();
                                inputStream.close();
                            } else {
                                Snackbar snackbar = Snackbar.make(linearLayout, "Already Downloaded", Snackbar.LENGTH_SHORT);
                                snackbar.setTextColor(Color.WHITE);
                                snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.mainBlue));
                                snackbar.show();
                            }

                        } catch (Exception e) {
                            Snackbar snackbar = Snackbar.make(linearLayout, "Something went wrong!!", Snackbar.LENGTH_SHORT);
                            snackbar.setTextColor(Color.WHITE);
                            snackbar.setBackgroundTint(context.getResources().getColor(R.color.red));
                            snackbar.show();
                        }
                    }
                });

            }
        });

        holder.subRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean expanded = invoice.isExpanded();
                invoice.setExpanded(!expanded);
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return invoices.size();
    }

    public ArrayList<Invoice> getArrayList() {
        return invoices;
    }

    public void searchQuery(String query, ArrayList<Invoice> AllInvoices) {
        ArrayList<Invoice> searchList = new ArrayList<>();
        if (!query.isEmpty()) {
            for (Invoice invoice : AllInvoices) {
                if (invoice.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    searchList.add(invoice);
                } else if(invoice.getDate().toLowerCase().contains(query.toLowerCase())){
                    searchList.add(invoice);
                }
            }
            invoices = searchList;
            notifyDataSetChanged();
        } else {
            invoices = AllInvoices;
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView businessName, invoiceTime, invoiceAddress, notice, gstNo, invoiceNo, businessEmail, businessPhone, customerEmail, invoiceTotal, invoiceDiscount, invoiceIGST, invoiceCGST, invoiceSGST, invoiceUTGST, roundOff, paymentMode, description;
        RecyclerView subRecyclerView;
        LinearLayout linearLayout;
        ImageButton download, report;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            businessName = itemView.findViewById(R.id.business_name_invoice1);
            invoiceTime = itemView.findViewById(R.id.invoice_time);
            invoiceAddress = itemView.findViewById(R.id.business_address);
            notice = itemView.findViewById(R.id.notice);
            gstNo = itemView.findViewById(R.id.gst_no);
            invoiceNo = itemView.findViewById(R.id.invoice_no);
            businessEmail = itemView.findViewById(R.id.business_email_invoice);
            businessPhone = itemView.findViewById(R.id.business_phone);
            customerEmail = itemView.findViewById(R.id.customer_email);
            invoiceTotal = itemView.findViewById(R.id.invoice_total);
            invoiceDiscount = itemView.findViewById(R.id.invoice_discount);
            invoiceIGST = itemView.findViewById(R.id.invoice_igst);
            invoiceCGST = itemView.findViewById(R.id.invoice_cgst);
            invoiceSGST = itemView.findViewById(R.id.invoice_sgst);
            invoiceUTGST = itemView.findViewById(R.id.invoice_utgst);
            roundOff = itemView.findViewById(R.id.round_off);
            paymentMode = itemView.findViewById(R.id.payment_mode);
            description = itemView.findViewById(R.id.description);
            subRecyclerView = itemView.findViewById(R.id.list);
            linearLayout = itemView.findViewById(R.id.title_layout);
            download = itemView.findViewById(R.id.download_btn);
            report = itemView.findViewById(R.id.report_btn);
        }

        @SuppressLint("SetTextI18n")
        private void bind(Invoice invoice) {

            boolean expandable = invoice.isExpanded();

            if (!expandable) {
                paymentMode.setVisibility(View.GONE);
                description.setVisibility(View.GONE);
                businessEmail.setVisibility(View.GONE);
                subRecyclerView.setVisibility(View.GONE);
                invoiceAddress.setVisibility(View.GONE);
                notice.setVisibility(View.GONE);
                gstNo.setVisibility(View.GONE);
                businessPhone.setVisibility(View.GONE);
                customerEmail.setVisibility(View.GONE);
                invoiceTotal.setVisibility(View.GONE);
                invoiceDiscount.setVisibility(View.GONE);
                invoiceIGST.setVisibility(View.GONE);
                invoiceCGST.setVisibility(View.GONE);
                invoiceSGST.setVisibility(View.GONE);
                invoiceUTGST.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);
                report.setVisibility(View.GONE);
            } else {
                paymentMode.setVisibility(View.VISIBLE);
                description.setVisibility(View.VISIBLE);
                businessEmail.setVisibility(View.VISIBLE);
                subRecyclerView.setVisibility(View.VISIBLE);
                invoiceAddress.setVisibility(View.VISIBLE);
                notice.setVisibility(View.VISIBLE);
                gstNo.setVisibility(View.VISIBLE);
                businessPhone.setVisibility(View.VISIBLE);
                customerEmail.setVisibility(View.VISIBLE);
                invoiceTotal.setVisibility(View.VISIBLE);
                invoiceDiscount.setVisibility(View.VISIBLE);
                invoiceIGST.setVisibility(View.VISIBLE);
                invoiceCGST.setVisibility(View.VISIBLE);
                invoiceSGST.setVisibility(View.VISIBLE);
                invoiceUTGST.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                report.setVisibility(View.VISIBLE);
            }

        }
    }
}
