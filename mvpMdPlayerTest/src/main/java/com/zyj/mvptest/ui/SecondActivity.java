package com.zyj.mvptest.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zyj.mvptest.R;

/**
 * Created by zhaoyuejun on 2016/12/23.
 */

public class SecondActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private FrameLayout mDrawer;
    View iv_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_second1);

        initView();
        initDrawer();
        setAllClick();
    }

    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawer = (FrameLayout) findViewById(R.id.fram_fragment_view);
        mDrawerLayout.setDrawerShadow(R.color.black_semi_transparent, GravityCompat.START);
        iv_right = findViewById(R.id.iv_right);
    }

    private void initDrawer() {
        getDrawerView();
        ViewGroup.LayoutParams params = mDrawer.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels * 4 / 5;
        mDrawer.setLayoutParams(params);
//        mDrawer.setBackgroundColor(Color.GRAY);
        mDrawer.setClickable(true);
        mDrawerLayout.setDrawerShadow(R.color.black_semi_transparent, GravityCompat.END);
    }

    public void setAllClick() {
        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    mDrawerLayout.closeDrawer(Gravity.RIGHT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.RIGHT);
                }
            }
        });
    }

    private View getDrawerView() {
        View v = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.activity_pm_search, null);
        v.setBackgroundColor(Color.WHITE);
        mDrawer.addView(v);
        return v;
    }

}
