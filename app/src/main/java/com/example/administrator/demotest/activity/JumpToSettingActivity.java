package com.example.administrator.demotest.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.administrator.demotest.R;
import com.example.administrator.demotest.Utils.ToastUtil;
import com.example.administrator.demotest.view.ConfirmDialog;

public class JumpToSettingActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String IMAGE_TYPE = "image/*";
    public static final int PHOTOZOOM = 10; // 相册
    private static final int REQUEST_PERMISSION = 0;

    private Button mBtnJumpToSetting;
    private Button mBtnJumpToAlbum;
    private ToastUtil toastUtil;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump_to_setting);
        toastUtil = new ToastUtil(this);
        mBtnJumpToSetting = (Button) findViewById(R.id.btn_jump_setting);
        mBtnJumpToAlbum = (Button) findViewById(R.id.btn_jump_Albun);
        mBtnJumpToSetting.setOnClickListener(this);
        mBtnJumpToAlbum.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_jump_setting:
                Intent settingIntent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(settingIntent);
                break;
            case R.id.btn_jump_Albun:
                if (checkPermession()) {
                    Intent albumIntent = new Intent(Intent.ACTION_GET_CONTENT, null);
                    albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_TYPE);
                    startActivityForResult(albumIntent, PHOTOZOOM);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PHOTOZOOM) {
            Uri uri = data.getData();
            String path = uri.getPath();
            toastUtil.showToast("拿回来的图片" + path);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean checkPermession() {
        boolean readPermession = this.getPackageManager().checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, this.getPackageName()) == PackageManager.PERMISSION_GRANTED;
        boolean writePermession = this.getPackageManager().checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, this.getPackageName()) == PackageManager.PERMISSION_GRANTED;

        if (!readPermession || !writePermession) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermission();
            }
            return false;
        }
        return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.ACCESS_COARSE_LOCATION
                },
                REQUEST_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                dialog = new ConfirmDialog.Builder(this)
                        .setMessage("啊哦，权限被拒绝了，请点击确定开启权限")
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .build();
                dialog.show();
            } else {
                Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
                albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_TYPE);
                startActivityForResult(albumIntent, PHOTOZOOM);
            }
        }
    }
}
