package com.example.kvbalu.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kvbalu.API.OrderAPI;
import com.example.kvbalu.Model.OrderModel;
import com.example.kvbalu.Model.UserModel;
import com.example.kvbalu.R;
import com.example.kvbalu.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileWalletFragment extends Fragment {
    TextView tvTotalOrder, tvTotalMoney;

    public MyProfileWalletFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_profile_wallet, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        anhXa();

        loadDetail();
    }
    void anhXa(){
        tvTotalOrder = getView().findViewById(R.id.tvOrderCount);
        tvTotalMoney = getView().findViewById(R.id.tvTotalMoneySpent);
    }
    void loadDetail(){
        UserModel user = SharedPrefManager.getInstance(getContext()).getUser();
        OrderAPI.ORDER_API.getAllOrderByUserId(user.getId()).enqueue(new Callback<List<OrderModel>>() {
            @Override
            public void onResponse(Call<List<OrderModel>> call, Response<List<OrderModel>> response) {
                if(response.isSuccessful()){
                    List<OrderModel> list = response.body();
                    if(list != null){
                        tvTotalOrder.setText(String.valueOf(list.size()));
                        int total = 0;
                        for(OrderModel o : list){
                            total += o.getTotal();
                        }
                        tvTotalMoney.setText(String.valueOf(total));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<OrderModel>> call, Throwable t) {
                tvTotalOrder.setText("0");
                tvTotalMoney.setText("0");
            }
        });

    }
}