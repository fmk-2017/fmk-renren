package com.example.dushikuaibang.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.example.dushikuaibang.Adapter.SkillImageAdapter;
import com.example.dushikuaibang.Adapter.TextItemAdapter;
import com.example.dushikuaibang.R;
import com.example.dushikuaibang.Utils.Constant;
import com.example.dushikuaibang.Utils.DialogShowUtils;
import com.example.dushikuaibang.Utils.HttpPostRequestUtils;
import com.example.dushikuaibang.Utils.LocationUtils;
import com.example.dushikuaibang.View.MyGridView;
import com.example.dushikuaibang.View.MyHorizontalScrollView;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 添加或编辑技能
 * type 0是添加技能， 1是修改
 */
public class EditSkillActivity extends BaseActivity
        implements AdapterView.OnItemClickListener,
        View.OnClickListener,
        HttpPostRequestUtils.HttpPostRequestCallback,
        MyHorizontalScrollView.OnItemClickListener,
        MyHorizontalScrollView.OnItemLongClickListener, AMapLocationListener {

    private final String METHOD_ADD_SERVER = "add_server";  //添加技能
    private final String METHOD_UP_SERVER = "up_server";  //更新技能
    private final int ACTION_IMAGE_CAPTURE_REQUEST = 123;  //拍照
    private final int ACTION_PICK_REQUEST = 124;  //相册
    private String header_text;
    private MyHorizontalScrollView imagelist;
    private MyGridView type_gridview, time_gridview;
    private String[] types, times;
    private TextView skill_type, skill_pay;
    private EditText skill_content, skill_price;
    private String skill_types = "跑腿代办";
    private String skill_type_id = "0";
    private String server_name, server_time, skill_infos, skill_prices, user_id;

    /**
     * 图片列表
     */
    private List<String> imagefilelist = new ArrayList<String>();
    private File imagefile;

    private int type = 0;

    private DialogShowUtils dsu;//dialog

    private boolean first;
    private int REQUEST_NEEDTYPE_CODE = 100;
    private Intent intent;

    private String skill_id;
    private Map<String, Boolean> typemap = new HashMap<String, Boolean>();
    private Map<String, Boolean> timemap = new HashMap<String, Boolean>();
    private String user_lat, user_lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_skill);
        getHeaderTitle();
        initHeader(header_text);

        LocationUtils.getInstance(this).startLoaction(this);

        user_id = shared.getString("user_id", "");
        first = true;

        initView();
    }

    private void getHeaderTitle() {
        type = getIntent().getIntExtra("type", 0);
        if (type == 0) header_text = "添加技能";
        else {
            skill_id = getIntent().getStringExtra("skill_id");
            header_text = "修改技能";
        }

        skill_type_id = getIntent().getStringExtra("types_id");
        skill_types = getIntent().getStringExtra("types");

    }

    private void initView() {
        type_gridview = (MyGridView) this.findViewById(R.id.type_gridview);
        time_gridview = (MyGridView) this.findViewById(R.id.time_gridview);
        imagelist = (MyHorizontalScrollView) this.findViewById(R.id.imagelist);

        imagefilelist.add("add");
        imagelist.setAdapter(new SkillImageAdapter(this, type, imagefilelist));

        types = getResources().getStringArray(R.array.service_type);
        times = getResources().getStringArray(R.array.service_time);

        resetMap(typemap, types);
        resetMap(timemap, times);

        type_gridview.setAdapter(new TextItemAdapter(this, types, typemap));
        time_gridview.setAdapter(new TextItemAdapter(this, times, timemap));

        imagelist.setOnItemLongClickListener(this);
        imagelist.setOnItemClickListener(this);
        time_gridview.setOnItemClickListener(this);
        type_gridview.setOnItemClickListener(this);

        skill_pay = (TextView) this.findViewById(R.id.skill_pay);
        skill_type = (TextView) this.findViewById(R.id.skill_type);
        skill_content = (EditText) this.findViewById(R.id.skill_content);
        skill_price = (EditText) this.findViewById(R.id.skill_price);

        skill_pay.setOnClickListener(this);
        skill_type.setOnClickListener(this);
        skill_type.setText("技能类型：" + (TextUtils.isEmpty(skill_types) ? "点击选择" : skill_types));
    }

    public void resetMap(Map map, String[] array) {
        for (String str : array) {
            map.put(str, false);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.type_gridview) {
            server_name = position + 1 + "";
            resetMap(typemap, types);
            typemap.put(types[position], true);
            type_gridview.setAdapter(new TextItemAdapter(this, types, typemap));
        } else if (parent.getId() == R.id.time_gridview) {
            server_time = position + "";
            resetMap(timemap, times);
            timemap.put(times[position], true);
            time_gridview.setAdapter(new TextItemAdapter(this, times, timemap));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.skill_type:
                intent = new Intent(this, AddSkillListActivity.class);
                intent.putExtra("start", 1);
                startActivityForResult(intent, REQUEST_NEEDTYPE_CODE);
                break;
            case R.id.skill_pay:
                if (checkparam())
                    Addskill();
                break;
            case R.id.camera:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imagefile = new File(Constant.getAppPath(Constant.IMAGE_PATH + "/original") + "/" + System.currentTimeMillis() + ".jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagefile));
                startActivityForResult(intent, ACTION_IMAGE_CAPTURE_REQUEST);
                dsu.dismiss();
                break;
            case R.id.album:
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, ACTION_PICK_REQUEST);
                dsu.dismiss();
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
            imagefilelist.add(0, imagefile.getAbsolutePath());
            showImage();
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
            imagefilelist.add(0, picturePath);
            showImage();
        } else if (requestCode == REQUEST_NEEDTYPE_CODE) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(getApplicationContext(), "请选择技能类型", Toast.LENGTH_SHORT).show();
                return;
            }
            skill_type_id = data.getStringExtra("id");
            skill_types = data.getStringExtra("name");
            skill_type.setText("技能类型：" + skill_types);
        }
    }

    /**
     * 将选择的图片显示出来
     */
    public void showImage() {
        if (first) {
            Toast.makeText(getApplicationContext(), "长安图片删除", Toast.LENGTH_SHORT).show();
            first = false;
        }
        imagelist.setAdapter(new SkillImageAdapter(this, type, imagefilelist));
    }

    /**
     * 请求服务器前判断是不是少必要参数
     *
     * @return 是否缺参数
     */
    public boolean checkparam() {
        skill_infos = skill_content.getText().toString().trim();
        skill_prices = skill_price.getText().toString().trim();
        if (TextUtils.isEmpty(user_id)) {
            Toast.makeText(getApplicationContext(), "请重新登陆", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(server_name)) {
            Toast.makeText(getApplicationContext(), "请选择服务方式", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(server_time)) {
            Toast.makeText(getApplicationContext(), "请选择服务时间", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(skill_infos)) {
            Toast.makeText(getApplicationContext(), "请输入技能介绍", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(skill_prices)) {
            Toast.makeText(getApplicationContext(), "请输入服务价格", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void Addskill() {
        HashMap<String, String> map = new HashMap<String, String>();
        if (type == 0) map.put("act", METHOD_ADD_SERVER);
        else {
            map.put("act", METHOD_UP_SERVER);
            map.put("skill_id", skill_id);
        }
        map.put("user_id", user_id);
        map.put("category_id", skill_type_id + "");
        map.put("server_name", server_name);
        map.put("server_time", server_time);
        map.put("user_lon", user_lon);
        map.put("user_lat", user_lat);
        map.put("skill_info", skill_infos);
        for (int i = 0; i < imagefilelist.size() - 1; i++)
            map.put("image[" + i + "]", imagefilelist.get(i));
        map.put("skill_price", skill_prices);
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    @Override
    public void Success(String method, JSONObject json) {
        if (METHOD_ADD_SERVER.equals(method)) {
            Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT).show();
        } else if (METHOD_UP_SERVER.equals(method)) {
            Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    @Override
    public void Fail(String method, String error) {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onClick(View view, int pos) {
        if (pos == (imagelist.getAdapter().getCount() - 1)) {
            dsu = DialogShowUtils.getInstance(view.getContext()).ShowGetImageType(this);
        }
    }

    @Override
    public void onLongClick(View view, int pos) {
        if (pos != (imagelist.getAdapter().getCount() - 1))
            imagefilelist.remove(pos);
        imagelist.setAdapter(new SkillImageAdapter(this, type, imagefilelist));
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (String.valueOf(aMapLocation.getLatitude()).length() > 12) return;
        user_lat = aMapLocation.getLatitude() + "";
        user_lon = aMapLocation.getLongitude() + "";
    }
}
