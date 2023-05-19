package com.example.kvbalu.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kvbalu.Interface.DeliveryAddressSelectInterface;
import com.example.kvbalu.DeliveryDetail;
import com.example.kvbalu.R;
import com.example.kvbalu.common.SharedPrefManager;

import java.util.List;

public class DeliveryDetailAdapter extends RecyclerView.Adapter<DeliveryDetailAdapter.ViewHolder> {
    List<DeliveryDetail> deliveryDetails;
    Context context;

    int selectedPosition = -1;
    DeliveryAddressSelectInterface selectInterface;

    public DeliveryDetailAdapter(List<DeliveryDetail> deliveryDetails, Context context) {
        this.deliveryDetails = deliveryDetails;
        this.context = context;
    }

    public DeliveryDetailAdapter(List<DeliveryDetail> deliveryDetails, Context context, DeliveryAddressSelectInterface selectInterface) {
        this.deliveryDetails = deliveryDetails;
        this.context = context;
        this.selectInterface = selectInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_recycle_delivery_detail, parent, false);
        return new ViewHolder(view);
//        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvDeliveryAddress.setText(deliveryDetails.get(position).getReceiverAddress());
        holder.tvNameReceive.setText(deliveryDetails.get(position).getReceiverName());
        holder.tvPhoneNumber.setText(deliveryDetails.get(position).getReceiverPhone());

        holder.rdAddressSelectBtn.setChecked(position == selectedPosition);

        holder.itemView.setOnClickListener(v -> {
            holder.rdAddressSelectBtn.setChecked(true);
        });

        holder.rdAddressSelectBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedPosition = holder.getAdapterPosition();
                    selectInterface.onDeliveryDetailClick(holder.getAdapterPosition());

                    holder.clDeliveryDetail.setBackground(context.getDrawable(R.drawable.selected_delivery_bg));
                } else {
                    holder.clDeliveryDetail.setBackground(context.getDrawable(R.drawable.add_new_address_nobackground));
                }
            }
        });
        holder.tvEditDeliveryDetail.setOnClickListener(v -> {
            holder.openAddAddressDialog(position);
        });

    }

    @Override
    public int getItemCount() {
        return deliveryDetails == null ? 0 : deliveryDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameReceive, tvPhoneNumber, tvDeliveryAddress, tvDeleteAddress, tvEditDeliveryDetail;

        RadioButton rdAddressSelectBtn;

        ConstraintLayout clDeliveryDetail;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNameReceive = itemView.findViewById(R.id.tvNameRecieve);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
            tvDeliveryAddress = itemView.findViewById(R.id.tvDeliveryAddress);
            tvDeleteAddress = itemView.findViewById(R.id.tvDeleteAddress);
            tvEditDeliveryDetail = itemView.findViewById(R.id.tvEditDeliveryDetail);

            rdAddressSelectBtn = itemView.findViewById(R.id.rdAddressSelectBtn);

            clDeliveryDetail = itemView.findViewById(R.id.clDeliveryDetail);


            tvDeleteAddress.setOnClickListener(v -> deleteAddress());
        }

        void deleteAddress() {
            int p = getAdapterPosition();
            notifyItemRemoved(getAdapterPosition());
            deliveryDetails.remove(p);
            SharedPrefManager.getInstance(context).setList("DeliveryList", deliveryDetails);
            selectInterface.onDeleteDeliveryDetailClick(deliveryDetails.size());
            selectedPosition = -1;
        }

        public void openAddAddressDialog(int position) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_add_address);

            Window window = dialog.getWindow();
            if (window == null) {
                return;
            }

            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

//        WindowManager.LayoutParams windowAttributes = window.getAttributes();
//        windowAttributes.gravity = Gravity.CENTER;
//        window.setAttributes(windowAttributes);

            dialog.setCancelable(true);

            TextView receiverNameEdit = dialog.findViewById(R.id.receiverNameEdit);
            TextView receiverPhoneEdit = dialog.findViewById(R.id.receiverPhoneEdit);
            TextView receiverAddressEdit = dialog.findViewById(R.id.receiverAddressEdit);

            AppCompatButton add = dialog.findViewById(R.id.addAddressBtn);
            AppCompatButton cancel = dialog.findViewById(R.id.dialogCancleBtn);

            DeliveryDetail detail = SharedPrefManager.getInstance(context).getDeliveryDetail().get(position);

            String u = String.valueOf(detail.getReceiverName());
            String p = String.valueOf(detail.getReceiverPhone());
            String a = String.valueOf(detail.getReceiverAddress());

            receiverNameEdit.setText(u);
            receiverPhoneEdit.setText(p);
            receiverAddressEdit.setText(a);

            add.setText("EDIT");
            add.setOnClickListener(v -> {

                if (!TextUtils.isEmpty(u) && !TextUtils.isEmpty(p) && !TextUtils.isEmpty(a)) {
                    deliveryDetails.set(position, new DeliveryDetail(String.valueOf(receiverNameEdit.getText()),
                            String.valueOf(receiverPhoneEdit.getText()),
                            String.valueOf(receiverAddressEdit.getText())));
                    SharedPrefManager.getInstance(context).setList("DeliveryList", deliveryDetails);
                    notifyDataSetChanged();
//                clProceedToPayment.setVisibility(View.VISIBLE);
//                ivNoAddressFound.setVisibility(View.GONE);
                    cancel.performClick();
                }
                if (TextUtils.isEmpty(u)) {
                    receiverNameEdit.setError("Please enter receiver name");
                    receiverNameEdit.requestFocus();
                }
                if (TextUtils.isEmpty(p)) {
                    receiverPhoneEdit.setError("Please enter receiver phone");
                    receiverPhoneEdit.requestFocus();
                }

                if (TextUtils.isEmpty(a)) {
                    receiverAddressEdit.setError("Please enter receiver address");
                    receiverAddressEdit.requestFocus();
                }
            });

            cancel.setOnClickListener(v -> {
                dialog.dismiss();
            });

            dialog.show();
        }
    }


}
