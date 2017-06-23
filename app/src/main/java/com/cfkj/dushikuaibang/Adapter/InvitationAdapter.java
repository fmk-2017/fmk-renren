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
import com.cfkj.dushikuaibang.Entity.Invitation;
import com.cfkj.dushikuaibang.Interface.OnInvitedListener;
import com.cfkj.dushikuaibang.R;
import com.cfkj.dushikuaibang.Utils.AppUtils;
import com.cfkj.dushikuaibang.Utils.Constant;
import com.cfkj.dushikuaibang.Utils.TimeUtils;
import com.cfkj.dushikuaibang.View.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by fengm on 2017-4-10.
 */

public class InvitationAdapter extends BaseAdapter implements View.OnClickListener {

    private Context context;
    private List<Invitation> invitations;
    private View.OnClickListener onclick;
    private boolean is_self;
    private OnInvitedListener invitedListener;

    public InvitationAdapter(Context context, List<Invitation> invitations, View.OnClickListener onclick, boolean is_self) {
        this.context = context;
        this.invitations = invitations;
        this.onclick = onclick;
        this.is_self = is_self;
    }

    @Override
    public int getCount() {
        return invitations.size();
    }

    @Override
    public Object getItem(int position) {
        return invitations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.serverinvitation_layout, null, false);
            vh = new ViewHolder();
            vh.start_one = (ImageView) convertView.findViewById(R.id.start_one);
            vh.start_two = (ImageView) convertView.findViewById(R.id.start_two);
            vh.start_three = (ImageView) convertView.findViewById(R.id.start_three);
            vh.image[0] = (ImageView) convertView.findViewById(R.id.image1);
            vh.image[1] = (ImageView) convertView.findViewById(R.id.image2);
            vh.image[2] = (ImageView) convertView.findViewById(R.id.image3);
            vh.auth1 = (ImageView) convertView.findViewById(R.id.auth1);
            vh.auth2 = (ImageView) convertView.findViewById(R.id.auth2);
            vh.auth3 = (ImageView) convertView.findViewById(R.id.auth3);
            vh.auth4 = (ImageView) convertView.findViewById(R.id.auth4);
            vh.auth5 = (ImageView) convertView.findViewById(R.id.auth5);
            vh.invitantion = (TextView) convertView.findViewById(R.id.invitantion);
            vh.server_name = (TextView) convertView.findViewById(R.id.server_name);
            vh.server_type = (TextView) convertView.findViewById(R.id.server_type);
            vh.textView3 = (TextView) convertView.findViewById(R.id.textView3);
            vh.content = (TextView) convertView.findViewById(R.id.content);
            vh.server_price = (TextView) convertView.findViewById(R.id.server_price);
            vh.server_time = (TextView) convertView.findViewById(R.id.server_time);
            vh.avatar = (CircleImageView) convertView.findViewById(R.id.avatar);
            convertView.setTag(vh);
        } else vh = (ViewHolder) convertView.getTag();

        Invitation invi = invitations.get(position);

        ImageLoader.getInstance().displayImage(Constant.HOST + invi.getUser_photo(), vh.avatar);
        vh.server_name.setText(invi.getNickname());
        vh.server_time.setText(TimeUtils.getTime(invi.getAddtime()));
        vh.server_price.setText("￥" + invi.getSkill_price());
        vh.content.setText(invi.getSkill_info());
        vh.textView3.setText(AppUtils.getdistance(Float.valueOf(invi.getRange())));
        vh.server_type.setText(invi.getCat_name());
        if (is_self) {
            vh.invitantion.setVisibility(View.VISIBLE);
            vh.server_price.setVisibility(View.VISIBLE);
        } else {
            vh.invitantion.setVisibility(View.GONE);
            vh.server_price.setVisibility(View.INVISIBLE);
        }


        for (int i = 0; i < vh.image.length; i++) {
            if (invi.getSkill_photos().size() == 0)
                vh.image[i].setVisibility(View.GONE);
            else vh.image[i].setVisibility(View.INVISIBLE);
        }

        for (int i = 0; i < invi.getSkill_photos().size(); i++) {
            vh.image[i].setVisibility(View.VISIBLE);
            vh.image[i].setTag(Constant.HOST + invi.getSkill_photos().get(i));
            ImageLoader.getInstance().displayImage(Constant.HOST + invi.getSkill_photos().get(i), vh.image[i], AppUtils.getOptions());
            vh.image[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(v.getContext(), ImageActivity.class).putExtra("url", (String) v.getTag()));
                }
            });
        }

        vh.invitantion.setTag(position);
        vh.invitantion.setOnClickListener(onclick);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invitantion://立即应邀
                invitedListener.onInvited((Integer) v.getTag());
                break;
        }
    }

    class ViewHolder {
        CircleImageView avatar;
        TextView server_name, server_type, textView3, content, server_price, server_time, invitantion;
        ImageView start_one, start_two, start_three, auth1, auth2, auth3, auth4, auth5;
        ImageView[] image = new ImageView[3];
    }

}
