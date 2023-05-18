package com.example.kvbalu.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.kvbalu.Fragment.MyProfileDetailFragment;

public class MyProfileViewPagerAdapter extends FragmentStateAdapter {

    public MyProfileViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public MyProfileViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        /*if(position == 0){*/
            return new MyProfileDetailFragment();
        /*}else if(position == 1){
            return new MyProfileWalletFragment();
        }
        else{
            return new MyProfileOtherFragment();
        }*/
    }

    @Override
    public int getItemCount() {
        return 1;
    }

}
