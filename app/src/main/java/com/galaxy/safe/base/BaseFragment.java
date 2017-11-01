
package com.galaxy.safe.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
    public Activity mactivity;
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = initview(inflater);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mactivity = getActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        initDate();
    }

    /**
     * 可重写也可不重写
     */
    public void initDate() {
    }

    /**
     * 子类必许实现 传个填充器，作为当前fragment布局来展示
     *
     * @param inflater
     * @return
     */
    public abstract View initview(LayoutInflater inflater);

    public View getRootView() {
        return view;
    }
}