package com.example.administrator.demotest.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.administrator.demotest.R;
import com.example.administrator.demotest.Utils.ToastUtil;
import com.example.administrator.demotest.view.ConfirmDialog;

import java.util.TimeZone;

public class JumpToSettingActivity extends BaseActivity implements View.OnClickListener {
    private static String TAG = "MainActivity";
    private static final String IMAGE_TYPE = "image/*";
    public static final int PHOTOZOOM = 10; // 相册
    private static final int REQUEST_PERMISSION = 0;
    private static final int CALENDAR_RESULT_CODE = 3210;
    private static final int CALENDAR_REQUEST_PERMISSION = 3220;

    private Button mBtnJumpToSetting;
    private Button mBtnJumpToAlbum;
    private Button btnJumpCalendar;
    private ToastUtil toastUtil;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump_to_setting);
        toastUtil = new ToastUtil(this);
        mBtnJumpToSetting = (Button) findViewById(R.id.btn_jump_setting);
        mBtnJumpToAlbum = (Button) findViewById(R.id.btn_jump_Albun);
        btnJumpCalendar = (Button) findViewById(R.id.btn_jump_calendar);
        mBtnJumpToSetting.setOnClickListener(this);
        mBtnJumpToAlbum.setOnClickListener(this);
        btnJumpCalendar.setOnClickListener(this);
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
            case R.id.btn_jump_calendar:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
                    insertTocCalender();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR}, CALENDAR_REQUEST_PERMISSION);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTOZOOM) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String path = uri.getPath();
                toastUtil.showToast("拿回来的图片" + path);
            }
        } else if (requestCode == CALENDAR_RESULT_CODE) {
            if (resultCode == RESULT_OK) {
                toastUtil.showToast("添加成功");
            } else {
                toastUtil.showToast("取消");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void insertTocCalender() {
        long beginTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis() + 24 * 1000 * 60 * 60;
        String title = "测试VIVOtitle";
        String description = "测试VIVOdescription";
        String eventLocation = "测试VIVOeventLocation";
        String email = "";
        if ("VIVO".equalsIgnoreCase(Build.MANUFACTURER) || "OPPO".equalsIgnoreCase(Build.MANUFACTURER)) {
            TimeZone timeZone = TimeZone.getDefault();
            final ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, beginTime);
            values.put(CalendarContract.Events.DTEND, endTime);
            values.put(CalendarContract.Events.TITLE, title);
            values.put(CalendarContract.Events.CALENDAR_ID, 1);
            values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            getApplicationContext().getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values);
        } else {
            Intent intent = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime)
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)
                    .putExtra(CalendarContract.Events.TITLE, title)
                    .putExtra(CalendarContract.Events.DESCRIPTION, description)
                    .putExtra(CalendarContract.Events.EVENT_LOCATION, eventLocation)
                    .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                    .putExtra(Intent.EXTRA_EMAIL, email);
            startActivityForResult(intent, CALENDAR_RESULT_CODE);
        }
    }

    private boolean checkPermession() {
        boolean readPermession = ContextCompat.
                checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean writePermession = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

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
        } else if (requestCode == CALENDAR_REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                insertTocCalender();
            } else {
                toastUtil.showToast("权限被拒绝");
            }
        }
    }
}
