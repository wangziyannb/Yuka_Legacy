package com.wzy.yuka.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lzf.easyfloat.EasyFloat;
import com.wzy.yuka.R;

public class HomeFragment extends Fragment implements View.OnClickListener {
    static final String TAG = "HomeFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home, container, false);
        root.findViewById(R.id.startBtn).setOnClickListener(this);
        root.findViewById(R.id.closeBtn).setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startBtn:
//                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_settings);
                EasyFloat.showAppFloat("startBtn");
                break;
            case R.id.closeBtn:
                EasyFloat.hideAppFloat("selectWindow");
                EasyFloat.hideAppFloat("startBtn");
                break;
            default:
                break;
        }
    }

    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            return AnimationUtils.loadAnimation(getActivity(), R.anim.scene_open_enter);
        } else {
            return AnimationUtils.loadAnimation(getActivity(), R.anim.scene_close_exit);
        }
    }
}
