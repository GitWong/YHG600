package com.galaxy.safe.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.galaxy.safe.R;

public class RefreshListView extends ListView implements OnScrollListener {

    private LinearLayout mHeaderView;
    private View mFootView;      //脚布局
    private LinearLayout mPullDownHeaderView;
    private ImageView ivArrow;// 箭头
    private ProgressBar mProgressbar;// 进度条
    private TextView tvState;                //状态
    private TextView tvLastUpdateTime;
    private View mCustomHeaderView; // 新增头布局
    private int mListViewYOnScreen = -1; // ListView在屏幕中y值

    private final int PULL_DOWN = 0; // 下拉刷新
    private final int RELEASE_REFRESH = 1; // 释放刷新
    private final int REFRESHING = 2; // 正在刷新

    private int currentState = PULL_DOWN; // 当前拉头状态默认为向下̬

    private int measuredHeight;// 下拉刷新的高度
    private int downY;
    private RotateAnimation downAnimation;// 下拉动画
    private RotateAnimation upAnimation;// 释放动画
    private OnRefreshListen mOnRefreshListen;
    private int mFootHitht;//  脚布局高度
    private boolean isLoadingMore = false; // 是否加载更多，默认不
    private boolean isEnabledPullDownRefresh = true; // 是否下拉刷新
    private boolean isEnabledLoadingMore = true; // 是否加载更多


    public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initHeader();
        initFootView();
        // TODO Auto-generated constructor stub
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        initHeader();
        initFootView();
    }

    public RefreshListView(Context context) {
        super(context);
        initHeader();
        initFootView();
        // TODO Auto-generated constructor stub
    }

    private void initFootView() {
        // TODO Auto-generated method stub

        mFootView = View.inflate(getContext(), R.layout.refresh_foot, null);
        mFootView.measure(0, 0);
        mFootHitht = mFootView.getMeasuredHeight();
        mFootView.setPadding(0, -mFootHitht, 0, 0);
        this.addFooterView(mFootView);
        this.setOnScrollListener(this);


    }

    private void initHeader() {
        mHeaderView = (LinearLayout) View.inflate(getContext(), R.layout.refresh_header, null);
        mPullDownHeaderView = (LinearLayout) mHeaderView.findViewById(R.id.ll_refresh_header_view_pull_down);
        ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_refresh_header_view_pull_down_arrow);
        mProgressbar = (ProgressBar) mHeaderView.findViewById(R.id.pb_refresh_header_view_pull_down);
        tvState = (TextView) mHeaderView.findViewById(R.id.tv_refresh_header_view_pull_down_state);
        tvLastUpdateTime = (TextView) mHeaderView.findViewById(R.id.tv_refresh_header_view_pull_down_last_update_time);
        tvLastUpdateTime.setText("上次刷新时间" + getCurrentTime());

        mPullDownHeaderView.measure(0, 0);//下拉刷新高度
        measuredHeight = mPullDownHeaderView.getMeasuredHeight();

        //默认是隐藏的
        mPullDownHeaderView.setPadding(0, -measuredHeight, 0, 0);

        this.addHeaderView(mHeaderView);
        initAnimation();
    }

    private void initAnimation() {
        upAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
        upAnimation.setDuration(500);
        upAnimation.setFillAfter(true);
        downAnimation = new RotateAnimation(-180, -360, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
        downAnimation.setDuration(500);
        downAnimation.setFillAfter(true);
    }

    /**
     * 增加自定义布局头
     *
     * @param v
     */
    public void addCustomHeaderView(View v) {

        this.mCustomHeaderView = v;
        mHeaderView.addView(mCustomHeaderView);

    }

    // 触摸事件处理
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:


                // 如果没有启用下拉刷新功能, 直接跳出switch
                if (!isEnabledPullDownRefresh) {
                    break;
                }


                if (mCustomHeaderView != null) {
                    int[] location = new int[2];

                    if (mListViewYOnScreen == -1) {

                        this.getLocationOnScreen(location);
                        mListViewYOnScreen = location[1];

                    }

                    mCustomHeaderView.getLocationOnScreen(location);
                    int mCustomHeaderViewYOnScreen = location[1];

                    if (mListViewYOnScreen > mCustomHeaderViewYOnScreen) {
                        break;
                    }
                }

                if (currentState == REFRESHING) {
                    // 正在刷新 跳出switch
                    break;
                }

                int moveY = (int) ev.getY();
                int diffY = downY - moveY;

//                Log.i("66", moveY + "到" + diffY);
                if (getFirstVisiblePosition() == 0 && diffY < 0) {//确保是竖直向下滑动
                    int paddingTop = (-measuredHeight + Math.abs(diffY));
                    mPullDownHeaderView.setPadding(0, paddingTop, 0, 0);
                    if (paddingTop < 0 && currentState != PULL_DOWN) {// 大于下拉头的高度̬状态改变
                        currentState = PULL_DOWN;

                        refreshHeaderState();
                        mPullDownHeaderView.setPadding(0, paddingTop, 0, 0);

                    } else if (paddingTop > 0 && currentState != RELEASE_REFRESH) {
                        currentState = RELEASE_REFRESH;
                        refreshHeaderState();
                    }
                    if (paddingTop > 0) {
                        paddingTop = paddingTop / 5;
                    }
                    mPullDownHeaderView.setPadding(0, paddingTop, 0, 0);
//                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (currentState == PULL_DOWN) {
                    mPullDownHeaderView.setPadding(0, -measuredHeight, 0, 0);
                } else if (currentState == RELEASE_REFRESH) {
                    mPullDownHeaderView.setPadding(0, 0, 0, 0);
                    currentState = REFRESHING;
                    refreshHeaderState();
                }
                break;


            default:
                break;
        }
        return super.onTouchEvent(ev);
    }


    /**
     * 根据刷新头改变状态̬
     */
    private void refreshHeaderState() {
        // TODO Auto-generated method stub
        switch (currentState) {
            case PULL_DOWN:
                ivArrow.startAnimation(downAnimation);
                tvState.setText("下拉刷新");

                break;
            case RELEASE_REFRESH:
                ivArrow.startAnimation(upAnimation);
                tvState.setText("释放立即刷新");

                break;
            case REFRESHING:
                ivArrow.clearAnimation();
                ivArrow.setVisibility(View.INVISIBLE);
                tvState.setText("正在刷新....");
                mProgressbar.setVisibility(View.VISIBLE);
                if (mOnRefreshListen != null) {
                    mOnRefreshListen.onPullDownRefresh();
                }
                break;
            default:
                break;
        }

    }

    /**
     * 结束刷新时的调用
     */
    public void onRefreshFinish() {
        if (isLoadingMore) {
            // 隐藏脚布局
            isLoadingMore = false;
            mFootView.setPadding(0, -mFootHitht, 0, 0);
        } else {
            mPullDownHeaderView.setPadding(0, -measuredHeight, 0, 0);
            currentState = PULL_DOWN;
            mProgressbar.setVisibility(View.INVISIBLE);
            ivArrow.setVisibility(View.VISIBLE);
            tvState.setText("下拉刷新");
            tvLastUpdateTime.setText("最后刷新时间: " + getCurrentTime());

        }
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    private String getCurrentTime() {
        // TODO Auto-generated method stub
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm ");
        return simpleDateFormat.format(new Date());
    }

    /**
     * 设置下拉刷新的监听
     *
     * @param onRefreshListen
     */
    public void setOnRefreshListen(OnRefreshListen onRefreshListen) {
        this.mOnRefreshListen = onRefreshListen;

    }

    /**
     * @author Administrator
     *         �提供接口下拉给控件提供下拉刷新接口
     */
    public interface OnRefreshListen {

        /**
         * 调用的方法什么时候调用
         */
        public void onPullDownRefresh();

        public void onLoadingMore();

    }

    // 滚动刷新的监听
            /*
             *  SCROLL_STATE_IDLE,  停滞
			 *   SCROLL_STATE_TOUCH_SCROLL  触摸滚动
			 *    SCROLL_STATE_FLING.惯性滑动
			 * 
			 */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO Auto-generated method stub


        if (!isEnabledLoadingMore) {
            // 当前没有启用加载更多的功能
            return;
        }
        if (scrollState == SCROLL_STATE_IDLE
                || scrollState == SCROLL_STATE_FLING) {
            int lastVisiblePosition = getLastVisiblePosition();

            if ((lastVisiblePosition == getCount() - 1) && !isLoadingMore) {


                isLoadingMore = true;

                mFootView.setPadding(0, 0, 0, 0);

                this.setSelection(getCount());

                if (mOnRefreshListen != null) {
                    mOnRefreshListen.onLoadingMore();
                }
            }


        }
    }

    //滚动时 调用此方法
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // TODO Auto-generated method stub

    }

    /**
     * 是否启用下拉刷新的功能
     *
     * @param isEnabled true 启用
     */
    public void isEnabledPullDownRefresh(boolean isEnabled) {
        isEnabledPullDownRefresh = isEnabled;
    }

    /**
     * 是否启用加载更多
     *
     * @param isEnabled
     */
    public void isEnabledLoadingMore(boolean isEnabled) {
        isEnabledLoadingMore = isEnabled;
    }

}
