package com.example.dushikuaibang.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dushikuaibang.R;
import com.example.dushikuaibang.Utils.AppUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {

    static BlankFragment bf;
    HomeFragment.HomeMessage message;

    public BlankFragment() {
    }

    public static BlankFragment newInstance(HomeFragment.HomeMessage message) {
        bf = new BlankFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("home", message);
        bf.setArguments(bundle);
        return bf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        message = (HomeFragment.HomeMessage) getArguments().get("home");

        TextView onlytext = (TextView) getView().findViewById(R.id.onlytext);

        String name = AppUtils.AuthorPhone(message.getNickname()) ? message.getNickname().substring(0, 5) + "******" : message.getNickname();

        String showtext = name + " 发布了一条 " + message.getCat_name() + "的需求";
//        onlytext.setBackgroundColor(Color.BLUE);
        onlytext.setText(showtext);

    }
}
