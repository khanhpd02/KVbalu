package com.example.kvbalu.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.kvbalu.api.FileAPI;
import com.example.kvbalu.api.UserAPI;
import com.example.kvbalu.adapter.MyProfileViewPagerAdapter;
import com.example.kvbalu.model.UserModel;
import com.example.kvbalu.R;
import com.example.kvbalu.RealPathUtil;
import com.example.kvbalu.SharedPrefManager;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    TextView tvName;
    ImageView ivUserAvatar, ivChangeAvatar;
    ConstraintLayout clHome, clCart, clOrder, clProfile;
    ProgressDialog mProgressDialog;
    TabLayout tlMyProfileItem;
    ViewPager2 vp2MyProfileItem;
    AppCompatButton appCompatLogoutBtn;

    MyProfileViewPagerAdapter profileViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask
        setContentView(R.layout.activity_profile);

        anhXa();
        UserModel user = SharedPrefManager.getInstance(this).getUser();
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            if (!(user.getAvatar() == null)) {
                Glide.with(getApplicationContext()).load(user.getAvatar()).into(ivUserAvatar);
            }
        }

        toHome();
        toCart();
        logout();
        toOrder();
        loadAdapter();
        changeAvatar();
    }

    void anhXa() {
        tvName = findViewById(R.id.tvName);

        ivUserAvatar = findViewById(R.id.ivUserAvatar);
        ivChangeAvatar = findViewById(R.id.ivChangeAvatar);

        clHome = findViewById(R.id.clHomeAppBar);
        clCart = findViewById(R.id.clCartAppBar);
        clOrder = findViewById(R.id.clOrderAppBar);
        clProfile = findViewById(R.id.clProfileAppBar);

        tlMyProfileItem = findViewById(R.id.tlMyProfileItem);
        vp2MyProfileItem = findViewById(R.id.vp2MyProfileItem);
        appCompatLogoutBtn = findViewById(R.id.appCompatLogoutBtn);


        mProgressDialog = new ProgressDialog(ProfileActivity.this);
        mProgressDialog.setMessage("Please wait upload ...");
        mProgressDialog.setCancelable(false);
    }

    void toHome() {
        clHome.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
        });
    }

    void toCart() {
        clCart.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(ProfileActivity.this, CartActivity.class));
        });
    }

    void toOrder() {
        clOrder.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(ProfileActivity.this, OrdersActivity.class));
        });
    }

    void loadAdapter() {
        tvName.setText(SharedPrefManager.getInstance(getApplicationContext()).getUser().getName());

        //tlMyProfileItem.addTab(tlMyProfileItem.newTab().setText("Thông tin cá nhân"));
      /*  tlMyProfileItem.addTab(tlMyProfileItem.newTab().setText("My Wallet"));
        tlMyProfileItem.addTab(tlMyProfileItem.newTab().setText("Other..."));*/

        profileViewPagerAdapter = new MyProfileViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        vp2MyProfileItem.setAdapter(profileViewPagerAdapter);

        tlMyProfileItem.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp2MyProfileItem.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        vp2MyProfileItem.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tlMyProfileItem.selectTab(tlMyProfileItem.getTabAt(position));
            }
        });
    }

    private Uri mUri;
    private UserModel user;
    public static final int MY_REQUEST_CODE = 100;
    public static final String TAG = ProfileActivity.class.getName();

    void changeAvatar() {
        ivChangeAvatar.setOnClickListener(v -> checkPermission());
    }

    public static String[] storage_permission = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storage_permission_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    };

    public static String[] permissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storage_permission_33;
        } else {
            p = storage_permission;
        }
        return p;
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGallery();
        } else {
            requestPermissions(permissions(), MY_REQUEST_CODE);
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
        }
    }

    private final ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.e(TAG, "onActivityResult");
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null) {
                            return;
                        }
                        Uri uri = data.getData();
                        mUri = uri;
                        Log.e("mUri", mUri.getPath());
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            uploadImage(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private void uploadImage(Bitmap avatar_bitmap) {
        mProgressDialog.show();
        user = SharedPrefManager.getInstance(this).getUser();

        String IMAGE_PATH = RealPathUtil.getRealPath(this, mUri);
        Log.e("--IMAGE_PATH", IMAGE_PATH);
        File file = new File(IMAGE_PATH);
        if (!file.canRead()) {
            Toast.makeText(ProfileActivity.this, "cannot read file", Toast.LENGTH_SHORT).show();
            mProgressDialog.dismiss();
            return;
        }

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part partBodyImages = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        FileAPI.FILE_API.uploadFile(partBodyImages).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    String file = response.body();
                    if (file != null) {
                        user.setAvatar(response.body());
                        UserAPI.USER_API.updateUser(user.getId(), user).enqueue(new Callback<UserModel>() {
                            @Override
                            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                                if (response.isSuccessful()) {
                                    ivUserAvatar.setImageBitmap(avatar_bitmap);
                                    SharedPrefManager.getInstance(ProfileActivity.this).userLogin(user);
                                    Toast.makeText(ProfileActivity.this, "Cập nhật ảnh thành công!", Toast.LENGTH_SHORT).show();
                                    mProgressDialog.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<UserModel> call, Throwable t) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.e("TAG", t.toString());
                Toast.makeText(ProfileActivity.this, "Lỗi cập nhật avatar!!!", Toast.LENGTH_LONG).show();
                mProgressDialog.dismiss();
            }
        });
    }
    void logout() {
        appCompatLogoutBtn.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Thông báo!");
            alert.setIcon(R.drawable.icons8_warning_96);
            alert.setMessage("Bạn muốn đăng xuất?");
            alert.setPositiveButton("Có", (dialog, which) -> {
                SharedPrefManager.getInstance(this).deleteDeliveryList();
                SharedPrefManager.getInstance(this).userLogout();
                startActivity(new Intent(this, IntroActivity.class));
            });
            alert.setNegativeButton("Không", (dialog, which) -> {

            });

            alert.show();
        });
    }

}

