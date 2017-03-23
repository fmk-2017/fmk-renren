package com.example.everyoneassist.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.everyoneassist.Adapter.SkillImageAdapter;
import com.example.everyoneassist.Adapter.TextItemAdapter;
import com.example.everyoneassist.R;
import com.example.everyoneassist.Utils.Constant;
import com.example.everyoneassist.Utils.DialogShowUtils;
import com.example.everyoneassist.Utils.HttpPostRequestUtils;
import com.example.everyoneassist.View.MyGridView;
import com.example.everyoneassist.View.MyHorizontalScrollView;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 添加或编辑技能
 * type 0是添加技能， 1是修改
 */
public class EditSkillActivity extends BaseActivity
        implements AdapterView.OnItemClickListener,
        View.OnClickListener,
        HttpPostRequestUtils.HttpPostRequestCallback,
        MyHorizontalScrollView.OnItemClickListener,
        MyHorizontalScrollView.OnItemLongClickListener {

    private String header_text;

    private MyHorizontalScrollView imagelist;
    private MyGridView type_gridview, time_gridview;

    private String[] types, times;

    private TextView skill_type, skill_pay;
    private EditText skill_content, skill_price;

    private String skill_types = "跑腿代办";
    private int skill_type_id = 0;

    private final String METHOD_ADD_SERVER = "add_server";  //添加技能
    private final String METHOD_UP_SERVER = "up_server";  //更新技能

    private final int ACTION_IMAGE_CAPTURE_REQUEST = 123;  //拍照
    private final int ACTION_PICK_REQUEST = 124;  //相册
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_skill);
        getHeaderTitle();
        initHeader(header_text);

        user_id = shared.getString("user_id","");
        first = true;

        initView();
    }

    private void getHeaderTitle() {
        type = getIntent().getIntExtra("type", 0);
        if (type == 0) header_text = "添加技能";
        else header_text = "修改技能";

        skill_type_id = getIntent().getIntExtra("types_id", 0);
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

        type_gridview.setAdapter(new TextItemAdapter(this, types));
        time_gridview.setAdapter(new TextItemAdapter(this, times));

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
        skill_type.setText("技能类型：" + skill_types);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getAdapter() == time_gridview.getAdapter())
            server_name = position + 1 + "";
        else if (parent.getAdapter() == time_gridview.getAdapter())
            server_time = position + 1 + "";
        if (view.isSelected()) view.setSelected(false);
        else view.setSelected(true);
    }

    private Intent intent;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.skill_type:
                intent = new Intent(this, AddSkillListActivity.class);
                intent.putExtra("start","1");
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
                Intent i = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();
                return;
            }
            imagefilelist.add(0, imagefile.getAbsolutePath());
            showImage();
        }
        if (requestCode == ACTION_PICK_REQUEST) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();
                return;
            }
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            imagefilelist.add(0, picturePath);
            showImage();
        }
    }

    /**
     * 将选择的图片显示出来
     */
    public void showImage(){
        if (first) {
            Toast.makeText(this, "长安图片删除", Toast.LENGTH_SHORT).show();
            first = false;
        }
        imagelist.setAdapter(new SkillImageAdapter(this, type, imagefilelist));
    }

    /**
     * 请求服务器前判断是不是少必要参数
     * @return  是否缺参数
     */
    public boolean checkparam(){
        skill_infos = skill_content.getText().toString().trim();
        skill_prices = skill_price.getText().toString().trim();
        if (TextUtils.isEmpty(user_id)){
            Toast.makeText(this, "请重新登陆", Toast.LENGTH_SHORT).show(); return false;
        }
        if (TextUtils.isEmpty(server_name)){
            Toast.makeText(this, "请选择服务方式", Toast.LENGTH_SHORT).show(); return false;
        }
        if (TextUtils.isEmpty(server_time)){
            Toast.makeText(this, "请选择服务时间", Toast.LENGTH_SHORT).show(); return false;
        }
        if (TextUtils.isEmpty(skill_infos)){
            Toast.makeText(this, "请输入技能介绍", Toast.LENGTH_SHORT).show(); return false;
        }
        if (TextUtils.isEmpty(skill_prices)){
            Toast.makeText(this, "请输入服务价格", Toast.LENGTH_SHORT).show(); return false;
        }
        return true;
    }


    public void Addskill() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("act", METHOD_ADD_SERVER);
        map.put("user_id", user_id);
        map.put("category_id", skill_type_id + "");
        map.put("server_name", server_name);
        map.put("server_time", server_time);
        map.put("skill_info", skill_infos);
        map.put("skill_photo", METHOD_ADD_SERVER);
        map.put("skill_price", skill_prices);
        HttpPostRequestUtils.getInstance(this).Post(map);
    }


    @Override
    public void Success(String method, JSONObject json) {

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
        if (pos == (imagelist.getAdapter().getCount()-1)){
            dsu = DialogShowUtils.getInstance(view.getContext()).ShowGetImageType(this);
        }
    }

    @Override
    public void onLongClick(View view, int pos) {
        if (pos != (imagelist.getAdapter().getCount()-1))
            imagefilelist.remove(pos);
        imagelist.setAdapter(new SkillImageAdapter(this, type, imagefilelist));
    }
}
