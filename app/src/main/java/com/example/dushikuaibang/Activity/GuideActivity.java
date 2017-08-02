package com.example.dushikuaibang.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.dushikuaibang.Adapter.SectionsPagerAdapter;
import com.example.dushikuaibang.R;
import com.example.dushikuaibang.runtimepermissions.PermissionsManager;
import com.example.dushikuaibang.runtimepermissions.PermissionsResultAction;

public class GuideActivity extends BaseActivity {

    private ViewPager guide_viewpager;

    private ImageView[] imageViews;
    private int[] images = new int[]{R.mipmap.guide1, R.mipmap.guide2, R.mipmap.guide3, R.mipmap.guide};
    private int starxx;
    private String[] dpermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!shared.getBoolean("is_first", true)) {
            startActivity(new Intent(this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
            return;
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide);

        guide_viewpager = (ViewPager) this.findViewById(R.id.guide_viewpager);
        guide_viewpager.setOffscreenPageLimit(3);
        imageViews = new ImageView[images.length];
        for (int i = 0; i < images.length; i++) {
            imageViews[i] = new ImageView(this);
            imageViews[i].setImageResource(images[i]);
            if (i == (images.length - 1))
                imageViews[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getPermisission();
                    }
                });
        }
        guide_viewpager.setAdapter(new SectionsPagerAdapter(this, imageViews));
        guide_viewpager.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                if (guide_viewpager.getCurrentItem() == (images.length - 1)) {
                    if (event.getAction() == DragEvent.ACTION_DRAG_STARTED) {
                        starxx = (int) event.getX();
                    } else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED) {
                        if ((event.getX() - starxx) > 0) {
                            getPermisission();
                        }
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int res = 0;
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    shouldShowRequestPermissionRationale(permissions[i]);
                } else break;
            }
            res += grantResults[i];
        }
        if (res == 0) {
            shared.edit().putBoolean("is_first", false).commit();
            startActivity(new Intent(GuideActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        }
    }

    public void getPermisission() {
        String[] permissions = new String[]{Manifest.permission.LOCATION_HARDWARE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};
        if (!PermissionsManager.getInstance().hasAllPermissions(this, permissions)) {
            PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
                @Override
                public void onGranted() {
                    shared.edit().putBoolean("is_first", false).commit();
                    startActivity(new Intent(GuideActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }

                @Override
                public void onDenied(String permission) {
                }
            });
        }

    }


}
