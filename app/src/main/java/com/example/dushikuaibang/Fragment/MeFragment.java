package com.example.dushikuaibang.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dushikuaibang.Activity.MyCollectActivity;
import com.example.dushikuaibang.Activity.MyOrderActivity;
import com.example.dushikuaibang.Activity.MyWalletActivity;
import com.example.dushikuaibang.Activity.PersonAuthActivity;
import com.example.dushikuaibang.Activity.SkillManagerActivity;
import com.example.dushikuaibang.Activity.SystemSettingActivity;
import com.example.dushikuaibang.Activity.UpateInfoActivity;
import com.example.dushikuaibang.Activity.UserManualActivity;
import com.example.dushikuaibang.R;
import com.example.dushikuaibang.Utils.Constant;
import com.example.dushikuaibang.View.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import static android.content.Context.MODE_PRIVATE;

public class MeFragment extends Fragment implements View.OnClickListener {

    protected SharedPreferences shared;
    private TextView[] textviews = new TextView[7];//my_order, demand_manager, skill_manager, my_wallet, system_setting, user_manual, my_collect, person_auth;
    private TextView tvUser, tvUserId;
    private LinearLayout user;
    private CircleImageView ivMeImg;

    public MeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        textviews[0] = (TextView) view.findViewById(R.id.my_order);
        textviews[1] = (TextView) view.findViewById(R.id.skill_manager);
        textviews[2] = (TextView) view.findViewById(R.id.my_wallet);
        textviews[3] = (TextView) view.findViewById(R.id.system_setting);
        textviews[4] = (TextView) view.findViewById(R.id.user_manual);
        textviews[5] = (TextView) view.findViewById(R.id.my_collect);
        textviews[6] = (TextView) view.findViewById(R.id.person_auth);
        tvUser = (TextView) view.findViewById(R.id.tvUser);
        tvUserId = (TextView) view.findViewById(R.id.tvUserId);
        ivMeImg = (CircleImageView) view.findViewById(R.id.ivMeImg);
        for (TextView textview : textviews) {
            textview.setOnClickListener(this);
        }
        user = (LinearLayout) view.findViewById(R.id.user);
        user.setOnClickListener(this);
        ivMeImg.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        shared = getActivity().getSharedPreferences(Constant.SHARED_NAME, MODE_PRIVATE);
        String user_photo = shared.getString("user_photo", "user_photo");
        String username = shared.getString("username", "username");
        String user_id = shared.getString("user_id", "user_id");
        ImageLoader.getInstance().displayImage(user_photo, ivMeImg);

        if (username.equals("username")) {
            tvUser.setText("人人帮");
        } else {
            tvUser.setText(username);
        }
        if (user_id.equals("user_id")) {
            tvUserId.setText("ID:无");
        } else {
            tvUserId.setText("ID:" + user_id);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user:
                break;
            case R.id.my_order:
                startActivity(new Intent(getActivity(), MyOrderActivity.class));
                break;
            case R.id.skill_manager:
                startActivity(new Intent(getActivity(), SkillManagerActivity.class));
                break;
            case R.id.my_wallet:
                startActivity(new Intent(getActivity(), MyWalletActivity.class));
                break;
            case R.id.system_setting:
                startActivity(new Intent(getActivity(), SystemSettingActivity.class));
                break;
            case R.id.user_manual:
                startActivity(new Intent(getActivity(), UserManualActivity.class));
                break;
            case R.id.my_collect:
                startActivity(new Intent(getActivity(), MyCollectActivity.class));
                break;
            case R.id.person_auth:
                startActivity(new Intent(getActivity(), PersonAuthActivity.class));
                break;
            case R.id.ivMeImg://修改个人信息
                startActivityForResult(new Intent(getActivity(), UpateInfoActivity.class), 666);
                break;
        }
    }

}
