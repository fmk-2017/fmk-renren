package com.example.dushikuaibang.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dushikuaibang.Layout.PercentLinearLayout;
import com.example.dushikuaibang.Layout.PercentRelativeLayout;
import com.example.dushikuaibang.R;
import com.example.dushikuaibang.Utils.Constant;
import com.example.dushikuaibang.Utils.DialogShowUtils;
import com.example.dushikuaibang.Utils.HttpPostRequestUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

public class AuthAutonymActivity extends BaseActivity implements View.OnClickListener, HttpPostRequestUtils.HttpPostRequestCallback {


    private int ACTION_IMAGE_CAPTURE_REQUEST = 1111;
    private int ACTION_PICK_REQUEST = 2222;
    private PercentRelativeLayout image_lz, image_lf;
    private ImageView image_z, image_f;
    private TextView commit;
    private DialogShowUtils dsu;
    private File imagefile;
    private boolean zf = true;
    private String[] sfz = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_autonym);
        initHeader("实名认证");

        initView();
    }

    private void initView() {
        image_lz = (PercentRelativeLayout) this.findViewById(R.id.image_lz);
        image_lf = (PercentRelativeLayout) this.findViewById(R.id.image_lf);

        image_lz.setOnClickListener(this);
        image_lf.setOnClickListener(this);

        image_z = (ImageView) this.findViewById(R.id.image_z);
        image_f = (ImageView) this.findViewById(R.id.image_f);

        commit = (TextView) this.findViewById(R.id.commit);

        commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_lf:
                zf = true;
                dsu = DialogShowUtils.getInstance(v.getContext()).ShowGetImageType(this);
                break;
            case R.id.image_lz:
                zf = false;
                dsu = DialogShowUtils.getInstance(v.getContext()).ShowGetImageType(this);
                break;
            case R.id.camera:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imagefile = new File(Constant.getAppPath(Constant.IMAGE_PATH + "/original/temp") + "/" + System.currentTimeMillis() + ".jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagefile));
                startActivityForResult(intent, ACTION_IMAGE_CAPTURE_REQUEST);
                dsu.dismiss();
                break;
            case R.id.album:
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, ACTION_PICK_REQUEST);
                dsu.dismiss();
                break;
            case R.id.commit:
                HashMap<String, String> map = new HashMap<>();
                map.put("act", "auth");
                map.put("user_id", shared.getString("user_id", ""));
                map.put("image[0]", sfz[0]);
                map.put("image[1]", sfz[1]);
                HttpPostRequestUtils.getInstance(this).Post(map);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_IMAGE_CAPTURE_REQUEST) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(getApplicationContext(), "获取图片失败", Toast.LENGTH_SHORT).show();
                return;
            }
            showImage(imagefile.getAbsolutePath());
        } else if (requestCode == ACTION_PICK_REQUEST) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(getApplicationContext(), "获取图片失败", Toast.LENGTH_SHORT).show();
                return;
            }
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            showImage(picturePath);
        }
    }

    private void showImage(String path) {
        if (zf) {
            sfz[1] = path;
            image_f.setImageBitmap(BitmapFactory.decodeFile(path));
        } else {
            sfz[0] = path;
            image_z.setImageBitmap(BitmapFactory.decodeFile(path));
        }
    }

    @Override
    public void Success(String method, JSONObject json) throws JSONException {
        Toast.makeText(this, "提交成功，请等待审核", Toast.LENGTH_LONG).show();
    }

    @Override
    public void Fail(String method, String error) {
        Log.e("Fail", "Fail: " + method);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
