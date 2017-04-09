package com.example.everyoneassist.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.everyoneassist.Dialog.SelectDialog;
import com.example.everyoneassist.R;
import com.example.everyoneassist.Utils.HttpPostRequestUtils;
import com.example.everyoneassist.View.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Administrator on 2017/4/3.
 */

public class UpateInfoActivity extends BaseActivity implements View.OnClickListener, HttpPostRequestUtils.HttpPostRequestCallback {

    private final String USER_INOF = "user_edit";

    private CircleImageView ivShowHead;
    private EditText etEmail;
    private TextView tvSex,tvEdSure,etUser;

    private File imgFile;
    private String user_photo,username,sex,email,user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);
        initView();
        initListener();
    }

    private void initView() {
        initHeader("个人信息");

        ivShowHead = (CircleImageView) findViewById(R.id.ivShowHead);
        etUser = (TextView) findViewById(R.id.etUser);
        etEmail = (EditText) findViewById(R.id.etEmail);
        tvSex = (TextView) findViewById(R.id.tvSex);
        tvEdSure = (TextView) findViewById(R.id.tvEdSure);

        user_photo = shared.getString("user_photo", "user_photo");
        username = shared.getString("username","username");
        sex = shared.getString("sex","sex");
        email = shared.getString("email","email");
        user_id = shared.getString("user_id","user_id");

        if (!user_photo.equals("user_photo")){
            ImageLoader.getInstance().displayImage(user_photo, ivShowHead);
        }
        if (!username.equals("username")){
            etUser.setText(username);
        }
        if (!sex.equals("sex")){
            tvSex.setText(sex.equals("1") ? "男" : "女");
        }
        if (!email.equals("email")){
            etEmail.setText(email);
        }

    }

    private void initListener(){
        tvEdSure.setOnClickListener(this);
        tvSex.setOnClickListener(this);
        ivShowHead.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivShowHead://头像
                choosePicDialog();
                break;
            case R.id.tvSex://性别
                chooseSexDialog();
                break;
            case R.id.tvEdSure://确定
                email = etEmail.getEditableText().toString().trim();

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("act", USER_INOF);
                map.put("user_id", user_id);
                map.put("nickname", username);
                map.put("image", user_photo);
                map.put("sex", sex);
                map.put("email", email);
                HttpPostRequestUtils.getInstance(this).Post(map);
                break;
        }
    }

    private void chooseSexDialog() {
        new SelectDialog.Build(UpateInfoActivity.this)
                .setXiangJi("男")
                .setXiangCe("女")
                .setQuxiao("取消")
                .setOnClickNum(new SelectDialog.OnClickNum() {
                    @Override
                    public void getNum(int num) {
                        switch (num){
                            case 1://男
                                tvSex.setText("男");
                                sex = "1";
                                break;
                            case 2://女
                                tvSex.setText("女");
                                sex = "0";
                                break;
                        }
                    }
                })
                .build();
    }

    private void choosePicDialog() {
        new SelectDialog.Build(UpateInfoActivity.this).setOnClickNum(new SelectDialog.OnClickNum() {
            @Override
            public void getNum(int num) {
                switch (num) {
                    case 1://相机
                        imgFile = getFile();
                        if (imgFile == null) return;
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action is capture
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgFile));
                        startActivityForResult(intent, 555);//头像拍照
                        break;
                    case 2://相册
                        Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);
                        openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(openAlbumIntent, 666);
                        break;
                }
            }
        }).build();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 判断是否返回值
        if (resultCode == RESULT_OK) {
            if (requestCode == 555) {
                CropPic(Uri.fromFile(imgFile));
            } else if (requestCode == 666) {
                CropPic(data.getData());
            }else if(requestCode == 777){
                Bundle extras = data.getExtras();
                Bitmap avatarbitmap = extras.getParcelable("data");
                ivShowHead.setImageBitmap(avatarbitmap);
                try {
                    File file = getFile();
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    avatarbitmap.compress(Bitmap.CompressFormat.PNG, 80, bos);
                    bos.flush();
                    bos.close();
                    user_photo = file.getAbsolutePath();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void CropPic(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 777);
    }

    public File getFile() {
        File image = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if (sdCardExist) {
            File sdDir = Environment.getExternalStorageDirectory();//获取跟目录
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            //.getExternalFilesDir()方法可以获取到 SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
            //创建临时文件,文件前缀不能少于三个字符,后缀如果为空默认未".tmp"
            try {
                image = File.createTempFile(
                        imageFileName,
                        ".jpg",
                        sdDir
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return image;
    }

    @Override
    public void Success(String method, JSONObject json) throws JSONException {
        //接口有问题待修改
        Log.e("pig",json.toString());
    }

    @Override
    public void Fail(String method, String error) {
        Log.e("pig",method + "---" + error);
    }

    @Override
    public Context getContext() {
        return null;
    }
}
