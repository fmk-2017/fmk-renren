package com.example.everyoneassist.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.everyoneassist.Entity.Skill;
import com.example.everyoneassist.Layout.PercentLinearLayout;
import com.example.everyoneassist.MyApplication;
import com.example.everyoneassist.R;
import com.example.everyoneassist.Utils.TimeUtils;
import com.example.everyoneassist.View.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.x;

import java.util.List;

/**
 * Created by fengm on 2017/1/12.
 */
public class HomeAdapter extends BaseAdapter {

    private Context context;
    private List<Skill> skill_list;

    public HomeAdapter(Context context) {
        this.context = context;
    }

    public HomeAdapter(Context context, List<Skill> skill_list) {
        this.context = context;
        this.skill_list = skill_list;
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
            ImageLoader.getInstance().displayImage(skill.getUser_photo(), viewHolder.homeitem_avatar);
        else viewHolder.homeitem_avatar.setImageResource(R.mipmap.home_07);
        viewHolder.homeitem_name.setText(skill.getServer_name() + "");
        viewHolder.homeitem_works.setText(skill.getCategory_id() + "");
        viewHolder.homeitem_time.setText(TimeUtils.getTime_difference(skill.getServer_time()) + "前");
        viewHolder.homeitem_text.setText(skill.getSkill_info());

        List<String> strlist = skill.getSkill_photos();
        if (strlist != null && strlist.size() < 1)
            viewHolder.imagelayout.setVisibility(View.GONE);
        else if (strlist != null && strlist.size() < 2) {
            viewHolder.imagelayout.setVisibility(View.VISIBLE);
            x.image().bind(viewHolder.homeitem_image1, strlist.get(0));
            viewHolder.homeitem_image1.setVisibility(View.VISIBLE);
            viewHolder.homeitem_image2.setVisibility(View.INVISIBLE);
            viewHolder.homeitem_image3.setVisibility(View.INVISIBLE);
        } else if (strlist != null && strlist.size() < 3) {
            viewHolder.imagelayout.setVisibility(View.VISIBLE);
            viewHolder.homeitem_image1.setVisibility(View.VISIBLE);
            viewHolder.homeitem_image2.setVisibility(View.VISIBLE);
            x.image().bind(viewHolder.homeitem_image1, strlist.get(0), MyApplication.io);
            x.image().bind(viewHolder.homeitem_image2, strlist.get(1), MyApplication.io);
            viewHolder.homeitem_image3.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.homeitem_image1.setVisibility(View.VISIBLE);
            viewHolder.homeitem_image2.setVisibility(View.VISIBLE);
            viewHolder.homeitem_image2.setVisibility(View.VISIBLE);
            viewHolder.imagelayout.setVisibility(View.VISIBLE);
            x.image().bind(viewHolder.homeitem_image1, strlist.get(0), MyApplication.io);
            x.image().bind(viewHolder.homeitem_image2, strlist.get(1), MyApplication.io);
            x.image().bind(viewHolder.homeitem_image2, strlist.get(2), MyApplication.io);
        }
        return convertView;
    }

    class ViewHolder {
        private TextView homeitem_title, homeitem_name, homeitem_works, homeitem_time, homeitem_text, homeitem_zan, homeitem_evaluate, homeitem_distance;
        private CircleImageView homeitem_avatar;
        private ImageView homeitem_rank1, homeitem_rank2, homeitem_rank3, homeitem_image1, homeitem_image2, homeitem_image3;
        private PercentLinearLayout imagelayout;
    }

}
