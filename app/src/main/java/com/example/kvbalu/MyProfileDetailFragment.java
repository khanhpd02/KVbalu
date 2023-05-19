package com.example.kvbalu;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kvbalu.api.UserAPI;
import com.example.kvbalu.activity.IntroActivity;
import com.example.kvbalu.activity.ProfileActivity;
import com.example.kvbalu.adapter.DeliveryDetailAdapter;
import com.example.kvbalu.Interface.DeliveryAddressSelectInterface;
import com.example.kvbalu.Model.UserModel;
import com.example.kvbalu.Object.DeliveryDetail;
import com.example.kvbalu.R;
import com.example.kvbalu.common.SharedPrefManager;
import com.example.kvbalu.model.UserModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileDetailFragment extends Fragment implements DeliveryAddressSelectInterface {

    TextView tvName, tvUsername, tvPhone, tvEmail, tvViewAllAddress, tvChangePassword2;
    ConstraintLayout clAddNewDeliveryAddress;
    ImageView ivNoAddressFound;
    RecyclerView rvDeliveryAddress;
    AppCompatButton appCompatEditProfileBtn, appCompatLogoutBtn;
    List<DeliveryDetail> deliveryDetails = new ArrayList<>();
    DeliveryDetailAdapter deliveryDetailAdapter;


    public MyProfileDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_profile_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        anhXa();
        loadDetail();

        editProfile();
//        logout();

        tvViewAllAddress.setOnClickListener(v -> openAddressList());
        tvChangePassword2.setOnClickListener(v -> openChangePassword());
    }

    void anhXa() {
        tvName = getView().findViewById(R.id.tvName);
        tvUsername = getView().findViewById(R.id.tvUsername);
        tvEmail = getView().findViewById(R.id.tvEmail);
        tvPhone = getView().findViewById(R.id.tvPhoneNumber);
        tvViewAllAddress = getView().findViewById(R.id.tvViewAllAddress);
        tvChangePassword2 = getView().findViewById(R.id.tvChangePassword2);

        appCompatEditProfileBtn = getView().findViewById(R.id.appCompatEditProfileBtn);
//        appCompatLogoutBtn = getView().findViewById(R.id.appCompatLogoutBtn);
    }

    void loadDetail() {
        UserModel user = SharedPrefManager.getInstance(getContext()).getUser();
        if (user != null) {
            tvName.setText(user.getName());
            tvUsername.setText(user.getUserName());
            tvPhone.setText(user.getPhoneNumber());
            tvEmail.setText(user.getEmail());
        }
    }

    void editProfile() {
        appCompatEditProfileBtn.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_edit_profile);

            Window window = dialog.getWindow();
            if (window == null) {
                return;
            }
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.CENTER);
            dialog.setCancelable(true);

            dialog.show();

            AppCompatButton dialogCancelBtn = dialog.findViewById(R.id.dialogCancelBtn);
            AppCompatButton editProfileBtn = dialog.findViewById(R.id.editProfileBtn);

            EditText name = dialog.findViewById(R.id.etName);
            EditText email = dialog.findViewById(R.id.etEmail);
            EditText phone = dialog.findViewById(R.id.etPhoneNumber);

            dialogCancelBtn.setOnClickListener(v1 -> dialog.dismiss());
            editProfileBtn.setOnClickListener(v1 -> {
                String n = String.valueOf(name.getText());
                String e = String.valueOf(email.getText());
                String p = String.valueOf(phone.getText());

//                if (!TextUtils.isEmpty(n) && !TextUtils.isEmpty(e) && !TextUtils.isEmpty(p)) {
                    if (!TextUtils.isEmpty(e) && !Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
                        email.setError("Please enter a valid email");
                        email.requestFocus();
                    } else {
                        UserModel user = SharedPrefManager.getInstance(getContext()).getUser();

                        if (!TextUtils.isEmpty(n)){
                            user.setName(n);
                        }
                        if(!TextUtils.isEmpty(e)){
                            user.setEmail(e);
                        }
                        if(!TextUtils.isEmpty(p)){
                            user.setPhoneNumber(p);
                        }
                        UserAPI.USER_API.updateUser(user.getId(), user).enqueue(new Callback<UserModel>() {
                            @Override
                            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                                if (response.isSuccessful()) {
                                    UserModel u = response.body();
                                    if (u != null) {
                                        SharedPrefManager.getInstance(getContext()).userLogin(u);
                                        Toast.makeText(getContext(),
                                                "Update Name: " + u.getName()
                                                        + "\nUpdate Phone: " + u.getPhoneNumber()
                                                        + "\nUpdate Email: " + u.getEmail(),
                                                Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        getActivity().finish();
                                        getActivity().startActivity(new Intent(getContext(), ProfileActivity.class));
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<UserModel> call, Throwable t) {
                                dialog.dismiss();
                            }
                        });
                    }

//                } else {
//                    String err = "This field cannot be blank";
//                    if (TextUtils.isEmpty(p)) {
//                        phone.requestFocus();
//                        phone.setError(err);
//                    }
//                    if (TextUtils.isEmpty(e)) {
//                        email.requestFocus();
//                        email.setError(err);
//                    }
//                    if (TextUtils.isEmpty(n)) {
//                        name.requestFocus();
//                        name.setError(err);
//                    }
//                }
            });
        });
    }

    void logout() {
        appCompatLogoutBtn.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("Warning!");
            alert.setIcon(R.drawable.icons8_warning_96);
            alert.setMessage("If you continue to logout, it will delete all your delivery Address list!!\nDo you want to logout?");
            alert.setPositiveButton("Yes", (dialog, which) -> {
                SharedPrefManager.getInstance(getContext()).deleteDeliveryList();
                SharedPrefManager.getInstance(getContext()).userLogout();
                startActivity(new Intent(getContext(), IntroActivity.class));
            });
            alert.setNegativeButton("No", (dialog, which) -> {

            });

            alert.show();
        });
    }

    void openAddressList() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_show_address);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setCancelable(true);
        loadAddressList(dialog);

        TextView tvClose = dialog.findViewById(R.id.tvCloseAddressDialog);
        tvClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();


    }

    void loadAddressList(@NonNull Dialog dialog) {
        ivNoAddressFound = dialog.findViewById(R.id.ivNoAddressFound);
        rvDeliveryAddress = dialog.findViewById(R.id.rvDeliveryAddress);
        clAddNewDeliveryAddress = dialog.findViewById(R.id.clAddNewDeliveryAddress);

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvDeliveryAddress.setLayoutManager(manager);

        DeliveryAddressSelectInterface selectInterface = this;
        deliveryDetails = SharedPrefManager.getInstance(getContext()).getDeliveryDetail();
        if (deliveryDetails.size() == 0) {
            ivNoAddressFound.setVisibility(View.VISIBLE);
        } else {
            ivNoAddressFound.setVisibility(View.GONE);
        }
        for (DeliveryDetail detail : deliveryDetails) {
            Log.e("Address list" + detail.getReceiverName(), detail.getReceiverAddress());
        }
        deliveryDetailAdapter = new DeliveryDetailAdapter(deliveryDetails, getContext(), selectInterface);
        rvDeliveryAddress.setAdapter(deliveryDetailAdapter);

        clAddNewDeliveryAddress.setOnClickListener(v -> openAddAddressDialog());

    }

    @SuppressLint("NotifyDataSetChanged")
    public void openAddAddressDialog() {
        deliveryDetails = SharedPrefManager.getInstance(getContext()).getDeliveryDetail();

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_address);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setCancelable(true);

        TextView receiverNameEdit = dialog.findViewById(R.id.receiverNameEdit);
        TextView receiverPhoneEdit = dialog.findViewById(R.id.receiverPhoneEdit);
        TextView receiverAddressEdit = dialog.findViewById(R.id.receiverAddressEdit);

        AppCompatButton add = dialog.findViewById(R.id.addAddressBtn);
        AppCompatButton cancel = dialog.findViewById(R.id.dialogCancleBtn);

        receiverNameEdit.setText(SharedPrefManager.getInstance(getContext()).getUser().getName());
        add.setOnClickListener(v -> {
            String u = String.valueOf(receiverNameEdit.getText());
            String p = String.valueOf(receiverPhoneEdit.getText());
            String a = String.valueOf(receiverAddressEdit.getText());
            if (!TextUtils.isEmpty(u) && !TextUtils.isEmpty(p) && !TextUtils.isEmpty(a)) {
                deliveryDetails.add(new DeliveryDetail(String.valueOf(receiverNameEdit.getText()),
                        String.valueOf(receiverPhoneEdit.getText()),
                        String.valueOf(receiverAddressEdit.getText())));
                SharedPrefManager.getInstance(getContext()).setList("DeliveryList", deliveryDetails);
//                deliveryDetailAdapter.notifyDataSetChanged();
//                deliveryDetailAdapter.notifyItemInserted(deliveryDetails.size() - 1);
//                clProceedToPayment.setVisibility(View.VISIBLE);
                DeliveryAddressSelectInterface selectInterface = this;
                deliveryDetails = SharedPrefManager.getInstance(getContext()).getDeliveryDetail();
                deliveryDetailAdapter = new DeliveryDetailAdapter(deliveryDetails, getContext(), selectInterface);
                rvDeliveryAddress.setAdapter(deliveryDetailAdapter);
                ivNoAddressFound.setVisibility(View.GONE);
                cancel.performClick();
            }
            if (TextUtils.isEmpty(a)) {
                receiverAddressEdit.setError("Please enter receiver address");
                receiverAddressEdit.requestFocus();
            }
            if (TextUtils.isEmpty(p)) {
                receiverPhoneEdit.setError("Please enter receiver phone");
                receiverPhoneEdit.requestFocus();
            }
        });

        cancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public void openChangePassword() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_change_password);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.getAttributes().windowAnimations = R.style.dialog_animation;
        dialog.setCancelable(true);
        dialog.show();


        EditText etCurrentPassword = dialog.findViewById(R.id.etCurrentPassword);
        EditText etNewPass = dialog.findViewById(R.id.etNewPassword);
        EditText etConfirmPass = dialog.findViewById(R.id.etConfirmPassword);

        Button change = dialog.findViewById(R.id.appCompatChangePasswordBtn);

        change.setOnClickListener(v -> {
            String cp = String.valueOf(etCurrentPassword.getText());
            String np = String.valueOf(etNewPass.getText());
            String cfp = String.valueOf(etConfirmPass.getText());

            if (!TextUtils.isEmpty(cp) && !TextUtils.isEmpty(np) && !TextUtils.isEmpty(cfp)) {
                if (!np.equals(cfp)) {
                    etConfirmPass.setText(null);
                    etConfirmPass.setError("Confirm Password not match!!!");
                    etConfirmPass.requestFocus();
                } else {
                    UserModel user = SharedPrefManager.getInstance(getContext()).getUser();
                    long id = user.getId();
                    UserAPI.USER_API.changePassword(id, cp, np).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getContext(), response.body(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                if (response.code() == 200) {
                                    user.setPassword(np);
                                    SharedPrefManager.getInstance(getContext()).userLogin(user);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            dialog.dismiss();
                        }
                    });
                }
            } else {
                String err = "This field cannot be blank!!!";
                if (TextUtils.isEmpty(cfp)) {
                    etConfirmPass.requestFocus();
                    etConfirmPass.setError(err);
                }
                if (TextUtils.isEmpty(np)) {
                    etNewPass.requestFocus();
                    etNewPass.setError(err);
                }
                if (TextUtils.isEmpty(cp)) {
                    etCurrentPassword.requestFocus();
                    etCurrentPassword.setError(err);
                }
            }
        });
    }

    @Override
    public void onDeliveryDetailClick(int position) {

    }

    @Override
    public void onDeleteDeliveryDetailClick(int size) {
        if (size == 0) {
            ivNoAddressFound.setVisibility(View.VISIBLE);
        } else {
            ivNoAddressFound.setVisibility(View.GONE);
        }
    }
}