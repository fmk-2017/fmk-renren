package com.cfkj.dushikuaibang.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cfkj.dushikuaibang.Activity.ImageActivity;
import com.cfkj.dushikuaibang.Entity.Skill;
import com.cfkj.dushikuaibang.Layout.PercentLinearLayout;
import com.cfkj.dushikuaibang.MyApplication;
import com.cfkj.dushikuaibang.R;
import com.cfkj.dushikuaibang.Utils.AppUtils;
import com.cfkj.dushikuaibang.Utils.TimeUtils;
import com.cfkj.dushikuaibang.View.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.x;

import java.util.List;

/**
 * Created by fengm on 2017/1/12.
 */
public class HomeAdapter extends BaseAdapter {

    private Context context;
    private List<Skill> skill_list;
    private View.OnClickListener onclick;

    public HomeAdapter(Context context) {
        this.context = context;
    }

    public HomeAdapter(Context context, List<Skill> skill_list, View.OnClickListener onclick) {
        this.context = context;
        this.skill_list = skill_list;
        this.onclick = onclick;
    }

    @Override
    public int getCount() {
        return skill_list.size();
    }

    @Override
    public Object getItem(int position) {
        return skill_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.homeadapter_item, null, false);
            viewHolder = new ViewHolder();
            viewHolder.homeitem_avatar = (CircleImageView) convertView.findViewById(R.id.homeitem_avatar);
            viewHolder.homeitem_title = (TextView) convertView.findViewById(R.id.homeitem_title);
            viewHolder.homeitem_name = (TextView) convertView.findViewById(R.id.homeitem_name);
            viewHolder.homeitem_works = (TextView) convertView.findViewById(R.id.homeitem_works);
            viewHolder.homeitem_time = (TextView) convertView.findViewById(R.id.homeitem_time);
            viewHolder.homeitem_text = (TextView) convertView.findViewById(R.id.homeitem_text);
            viewHolder.homeitem_zan = (TextView) convertView.findViewById(R.id.homeitem_zan);
            viewHolder.homeitem_evaluate = (TextView) convertView.findViewById(R.id.homeitem_evaluate);
            viewHolder.homeitem_distance = (TextView) convertView.findViewById(R.id.homeitem_distance);
            viewHolder.homeitem_rank1 = (ImageView) convertView.findViewById(R.id.homeitem_rank1);
            viewHolder.homeitem_rank2 = (ImageView) convertView.findViewById(R.id.homeitem_rank2);
            viewHolder.homeitem_rank3 = (ImageView) convertView.findViewById(R.id.homeitem_rank3);
            viewHolder.imagelayout = (PercentLinearLayout) convertView.findViewById(R.id.imagelayout);
            viewHolder.homeitem_image1 = (ImageView) convertView.findViewById(R.id.homeitem_image1);
            viewHolder.homeitem_image2 = (ImageView) convertView.findViewById(R.id.homeitem_image2);
            viewHolder.homeitem_image3 = (ImageView) convertView.findViewById(R.id.homeitem_image3);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position == 0)
            viewHolder.homeitem_title.setVisibility(View.VISIBLE);
        else viewHolder.homeitem_title.setVisibility(View.GONE);

        Skill skill = skill_list.get(position);
//        x.image().bind(viewHolder.homeitem_avatar, skill.getUser_photo(), MyApplication.io);
        if (skill.getUser_photo().endsWith(".png") || skill.getUser_photo().endsWith(".jpg") || skill.getUser_photo().endsWith(".jpeg"))
            ImageLoader.getInstance().displayImage(AppUtils.getAvatarPath(skill.getUser_photo()), viewHolder.homeitem_avatar, AppUtils.getOptions());
        else viewHolder.homeitem_avatar.setImageResource(R.mipmap.home_07);
        viewHolder.homeitem_name.setText(skill.getCat_name());
        viewHolder.homeitem_works.setText(skill.getServer_type_name());
        viewHolder.homeitem_time.setText(TimeUtils.getTime_difference(skill.getServer_time()) + "前");
        viewHolder.homeitem_text.setText(skill.getSkill_info());
        viewHolder.homeitem_distance.setText(AppUtils.getdistance(Float.valueOf(skill.getRange())));
        viewHolder.homeitem_zan.setText(skill.getPraisesum());

        List<String> strlist = skill.getSkill_photos();
        if (strlist == null || strlist.size() < 1)
            viewHolder.imagelayout.setVisibility(View.GONE);
        else if (strlist != null && strlist.size() < 2) {
            viewHolder.imagelayout.setVisibility(View.VISIBLE);
//            x.image().bind(viewHolder.homeitem_image1, strlist.get(0));
            viewHolder.homeitem_image1.setTag(strlist.get(0));
            ImageLoader.getInstance().displayImage(strlist.get(0), viewHolder.homeitem_image1, AppUtils.getOptions());
            viewHolder.homeitem_image1.setVisibility(View.VISIBLE);
            viewHolder.homeitem_image2.setVisibility(View.INVISIBLE);
            viewHolder.homeitem_image3.setVisibility(View.INVISIBLE);
        } else if (strlist != null && strlist.size() < 3) {
            viewHolder.imagelayout.setVisibility(View.VISIBLE);
            viewHolder.homeitem_image1.setVisibility(View.VISIBLE);
            viewHolder.homeitem_image2.setVisibility(View.VISIBLE);
//            x.image().bind(viewHolder.homeitem_image1, strlist.get(0), MyApplication.io);
//            x.image().bind(viewHolder.homeitem_image2, strlist.get(1), MyApplication.io);
            viewHolder.homeitem_image1.setTag(strlist.get(0));
            viewHolder.homeitem_image2.setTag(strlist.get(1));
            ImageLoader.getInstance().displayImage(strlist.get(0), viewHolder.homeitem_image1, AppUtils.getOptions());
            ImageLoader.getInstance().displayImage(strlist.get(1), viewHolder.homeitem_image2, AppUtils.getOptions());
            viewHolder.homeitem_image3.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.homeitem_image1.setVisibility(View.VISIBLE);
            viewHolder.homeitem_image2.setVisibility(View.VISIBLE);
            viewHolder.homeitem_image2.setVisibility(View.VISIBLE);
            viewHolder.imagelayout.setVisibility(View.VISIBLE);
//            x.image().bind(viewHolder.homeitem_image1, strlist.get(0), MyApplication.io);
//            x.image().bind(viewHolder.homeitem_image2, strlist.get(1), MyApplication.io);
//            x.image().bind(viewHolder.homeitem_image2, strlist.get(2), MyApplication.io);
            viewHolder.homeitem_image1.setTag(strlist.get(0));
            viewHolder.homeitem_image2.setTag(strlist.get(1));
            viewHolder.homeitem_image3.setTag(strlist.get(2));
            ImageLoader.getInstance().displayImage(strlist.get(0), viewHolder.homeitem_image1, AppUtils.getOptions());
            ImageLoader.getInstance().displayImage(strlist.get(1), viewHolder.homeitem_image2, AppUtils.getOptions());
            ImageLoader.getInstance().displayImage(strlist.get(2), viewHolder.homeitem_image3, AppUtils.getOptions());
        }
        viewHolder.homeitem_zan.setTag(skill.getSkill_id());
        viewHolder.homeitem_zan.setOnClickListener(onclick);

        viewHolder.homeitem_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = (String) v.getTag();
                v.getContext().startActivity(new Intent(v.getContext(), ImageActivity.class).putExtra("url", url));
            }
        });
        viewHolder.homeitem_image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = (String) v.getTag();
                v.getContext().startActivity(new Intent(v.getContext(), ImageActivity.class).putExtra("url", url));
            }
        });
        viewHolder.homeitem_image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = (String) v.getTag();
                v.getContext().startActivity(new Intent(v.getContext(), ImageActivity.class).putExtra("url", url));
            }
        });
        return convertView;
    }

    class ViewHolder {
        private TextView homeitem_title, homeitem_name, homeitem_works, homeitem_time, homeitem_text, homeitem_zan, homeitem_evaluate, homeitem_distance;
        private CircleImageView homeitem_avatar;
        private ImageView homeitem_rank1, homeitem_rank2, homeitem_rank3, homeitem_image1, homeitem_image2, homeitem_image3;
        private PercentLinearLayout imagelayout;
    }

}
