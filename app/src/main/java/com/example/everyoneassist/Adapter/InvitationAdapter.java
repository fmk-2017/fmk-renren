package com.example.everyoneassist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.everyoneassist.Activity.AtWillBuyActivity;
import com.example.everyoneassist.Entity.Invitation;
import com.example.everyoneassist.R;
import com.example.everyoneassist.Utils.Constant;
import com.example.everyoneassist.View.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by fengm on 2017-4-10.
 */

public class InvitationAdapter extends BaseAdapter {

    private Context context;
    private List<Invitation> invitations;
    private View.OnClickListener onclick;
    private boolean is_self;

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
            vh.image1 = (ImageView) convertView.findViewById(R.id.image1);
            vh.image2 = (ImageView) convertView.findViewById(R.id.image2);
            vh.image3 = (ImageView) convertView.findViewById(R.id.image3);
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
//        vh.server_time.setText(invi.get);
//        vh.server_price.setText();
//        vh.content.setText();
//        vh.textView3.setText();
        vh.server_type.setText(invi.getCat_name());
        if (is_self) {
            vh.invitantion.setVisibility(View.VISIBLE);
            vh.server_price.setVisibility(View.VISIBLE);
        } else {
            vh.invitantion.setVisibility(View.GONE);
            vh.server_price.setVisibility(View.INVISIBLE);
        }

        vh.invitantion.setTag(position);
        vh.invitantion.setOnClickListener(onclick);
        return convertView;
    }

    class ViewHolder {
        CircleImageView avatar;
        TextView server_name, server_type, textView3, content, server_price, server_time, invitantion;
        ImageView start_one, start_two, start_three, image1, image2, image3, auth1, auth2, auth3, auth4, auth5;
    }

}