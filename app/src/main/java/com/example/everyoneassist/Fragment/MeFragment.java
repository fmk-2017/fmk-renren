package com.example.everyoneassist.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.everyoneassist.Activity.MyCollectActivity;
import com.example.everyoneassist.Activity.MyInfoActivity;
import com.example.everyoneassist.Activity.MyWalletActivity;
import com.example.everyoneassist.Activity.PersonAuthActivity;
import com.example.everyoneassist.Activity.SkillManagerActivity;
import com.example.everyoneassist.Activity.SystemSettingActivity;
import com.example.everyoneassist.Activity.UserManualActivity;
import com.example.everyoneassist.R;

public class MeFragment extends Fragment implements View.OnClickListener {

    private TextView[] textviews = new TextView[8];//my_order, demand_manager, skill_manager, my_wallet, system_setting, user_manual, my_collect, person_auth;
    private LinearLayout user;

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
        textviews[1] = (TextView) view.findViewById(R.id.demand_manager);
        textviews[2] = (TextView) view.findViewById(R.id.skill_manager);
        textviews[3] = (TextView) view.findViewById(R.id.my_wallet);
        textviews[4] = (TextView) view.findViewById(R.id.system_setting);
        textviews[5] = (TextView) view.findViewById(R.id.user_manual);
        textviews[6] = (TextView) view.findViewById(R.id.my_collect);
        textviews[7] = (TextView) view.findViewById(R.id.person_auth);
        for (TextView textview : textviews) {
            textview.setOnClickListener(this);
        }
        user = (LinearLayout) view.findViewById(R.id.user);
        user.setOnClickListener(this);
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
                startActivity(new Intent(getActivity(), MyInfoActivity.class));
                break;
            case R.id.my_order:

                break;
            case R.id.demand_manager:

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
        }
    }
}
