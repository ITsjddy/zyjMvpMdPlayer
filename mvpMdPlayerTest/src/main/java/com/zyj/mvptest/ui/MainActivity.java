package com.zyj.mvptest.ui;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zyj.mvptest.R;
import com.zyj.mvptest.adapter.BaseRecyclerViewAdapter;
import com.zyj.mvptest.adapter.BaseRecyclerViewHolder;
import com.zyj.mvptest.base.BaseActivity;
import com.zyj.mvptest.mvp.model.FileBean;
import com.zyj.mvptest.mvp.model.Model;
import com.zyj.mvptest.mvp.presenter.VideoFilePresenter;
import com.zyj.mvptest.mvp.view.IMVPView;

import java.util.List;

/**
 * Created by zhaoyuejun on 2016/12/14.
 */

public class MainActivity extends BaseActivity  implements IMVPView {

    private LinearLayoutManager mLayoutManager;
    private FileListAdapter mAdapter;
    private RecyclerView filesListView;
    private boolean isRefreshing;
    private int mScrollThreshold = 4;
    private VideoFilePresenter videoFilePresenter;
    private SwipeRefreshLayout refreshLayout;
    private Context mcontext;

    TextView jumpToActivity;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mcontext=MainActivity.this;
        filesListView = (RecyclerView) findViewById(R.id.filesListview);

        mAdapter = new FileListAdapter();
        mLayoutManager = new LinearLayoutManager(mcontext);
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        filesListView.setLayoutManager(mLayoutManager);
        filesListView.setItemAnimator(new DefaultItemAnimator());
        filesListView.setAdapter(mAdapter);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        // 这句话是为了，第一次进入页面的时候显示加载进度条
        refreshLayout.setProgressViewOffset(true, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28, getResources()
                        .getDisplayMetrics()));
        refreshLayout.setProgressViewEndTarget(false, 200);
        //
        jumpToActivity= (TextView) findViewById(R.id.jumpToActivity);
    }

    @Override
    protected void initData() {
        videoFilePresenter = new VideoFilePresenter();
        videoFilePresenter.attachView(MainActivity.this);
        videoFilePresenter.getFileData(false);
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {
        //item点击监听
        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(int position, Object data) {
                Intent intent = new Intent(mcontext, VideoListActivity.class);
                intent.putExtra("path", ((FileBean) data).path);
                mcontext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
            }
        });
        // 下拉刷新监听
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                readTaskHandler.post(new ReadVideoDirectoryTask(mcontext, mainHandler));
                refresh();
            }
        });
        // recyclerView滚动FloatingActionMenu显示隐藏监听
        filesListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean isSignificantDelta = Math.abs(dy) > mScrollThreshold;
                if (isSignificantDelta) {

                }
            }
        });
        jumpToActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,SecondActivity.class);
                startActivity(intent);
            }
        });
    }
    /**
     * 重新扫描,刷新文件
     */
    public void refresh(){
        if(!isRefreshing){
            videoFilePresenter.getFileData(true);
        }
    }
    @Override
    public void getDataSuccess(List<? extends Model> data) {
        mAdapter.setData((List<FileBean>) data);
        mAdapter.notifyDataSetChanged();
        showToast("读取到了"+data.size()+"个目录");
    }

    @Override
    public void getDataError(Throwable e) {
        e.printStackTrace();
        showToast("视频文件读取失败，请稍后重试！");
    }

    @Override
    public void showProgress() {
        isRefreshing  = true;
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        isRefreshing = false;
        refreshLayout.setRefreshing(false);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoFilePresenter.detachView();
    }
    class FileListAdapter extends BaseRecyclerViewAdapter<FileBean> {

        /**
         * 创建item view
         *
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        protected BaseRecyclerViewHolder createItem(ViewGroup parent, int viewType) {

            View view  = LayoutInflater.from(mcontext).inflate(R.layout.item_files_layout ,parent ,false);
            MyViewHolder viewHolder = new MyViewHolder(view);
            return viewHolder;
        }

        /**
         * 绑定数据
         *
         * @param holder
         * @param position
         */
        @Override
        protected void bindData(BaseRecyclerViewHolder holder, int position) {
            MyViewHolder viewHolder = (MyViewHolder)holder;
            viewHolder.tvPath.setText(getItemData(position).name);
            viewHolder.tvCount.setText(getItemData(position).count+"个视频文件");
        }

        class MyViewHolder extends BaseRecyclerViewHolder{
            View mView;
            TextView tvPath,tvCount;
            public MyViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
                tvPath = (TextView) itemView.findViewById(R.id.tvPath);
                tvCount = (TextView) itemView.findViewById(R.id.tvCount);
            }

            @Override
            protected View getView() {
                return mView;
            }
        }
    }
}
