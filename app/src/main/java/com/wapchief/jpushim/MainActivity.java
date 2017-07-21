package com.wapchief.jpushim;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.utils.UnreadMsgUtils;
import com.flyco.tablayout.widget.MsgView;
import com.wapchief.jpushim.activity.AboutActivity;
import com.wapchief.jpushim.activity.SettingActivity;
import com.wapchief.jpushim.activity.UserActivty;
import com.wapchief.jpushim.activity.WebViewActivity;
import com.wapchief.jpushim.entity.TabEntity;
import com.wapchief.jpushim.fragment.FragmentFactory;
import com.wapchief.jpushim.framework.base.BaseActivity;
import com.wapchief.jpushim.framework.system.SystemStatusManager;
import com.wapchief.jpushim.framework.utils.UIUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.title_bar_back)
    ImageView mTitleBarBack;
    @BindView(R.id.title_bar_title)
    TextView mTitleBarTitle;
    @BindView(R.id.title_options_img)
    ImageView mTitleOptionsImg;
    @BindView(R.id.main_root_vp)
    ViewPager mMainRootVp;
    @BindView(R.id.main_nv)
    NavigationView mMainNv;
    @BindView(R.id.side_main)
    DrawerLayout mSideMain;
    @BindView(R.id.main_root)
    LinearLayout mMainRoot;
    @BindView(R.id.main_root_tab)
    CommonTabLayout mMainRootTab;
    @BindView(R.id.title_options_tv)
    TextView mTitleOptionsTv;

    private LinearLayout nav_header_ll;
    private TextView nav_header_name;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private String[] mTitles = {"消息", "联系人", "动态"};
    private int[] tabIconGray = new int[]{R.mipmap.icon_tab_message_gray, R.mipmap.icon_tab_im_gray, R.mipmap.icon_tab_d_gray};
    private int[] tabIcon = new int[]{R.mipmap.icon_tab_message, R.mipmap.icon_tab_im, R.mipmap.icon_tab_d};

    @Override
    protected int setContentView() {
        return R.layout.activity_main;

    }

    @Override
    protected void initView() {
        //状态栏
        new SystemStatusManager(this).setTranslucentStatus(R.drawable.shape_titlebar);
        //设置NavigationView
        initNaView();
        initSideDrawer();
        initTab();
        initPageAdapter();
        initNVHeader();
    }

    /*初始化NavigationView头部控件*/
    private void initNVHeader() {
//        mMainNv.setItemTextColor(getResources().getColorStateList(R.drawable.nav_select_tv,null));
        mMainNv.setItemIconTintList(null);
        View headerView = mMainNv.getHeaderView(0);
        nav_header_ll = (LinearLayout) headerView.findViewById(R.id.nav_header_ll);
        nav_header_name = (TextView) headerView.findViewById(R.id.nav_header_name);
        nav_header_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UserActivty.class);
                startActivity(intent);
            }
        });
    }

    /*初始化tab标签*/
    private void initTab() {
        //当xml属性失效的时候需要手动在代码中
        mMainRootTab.setTextSelectColor(getResources().getColor(R.color.colorTheme));
        mMainRootTab.setTextUnselectColor(getResources().getColor(R.color.colorText));
//        for (String title : mTitles) {
//            mFragments.add(fragment);
//        }

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], tabIcon[i], tabIconGray[i]));
        }

        mMainRootTab.setTabData(mTabEntities);

        mMainRootVp.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        //未读消息标签
        mMainRootTab.showMsg(0, 100);
        mMainRootTab.setMsgMargin(0, -6, 5);

        //设置未读消息红点
        mMainRootTab.showDot(2);
        MsgView rtv_2_2 = mMainRootTab.getMsgView(2);
        if (rtv_2_2 != null) {
            //设置小红点大小和位置
            UnreadMsgUtils.setSize(rtv_2_2, UIUtils.dip2px(this, 7.5f));
        }
    }


    /*添加监听器，手动设置Main布局的位置*/
    private void initSideDrawer() {
        mSideMain.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //获取屏幕的宽高
                WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                mMainRoot.layout(mMainNv.getRight(), 0, mMainNv.getRight() + display.getWidth(), display.getHeight());
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    /*NavigationView侧滑菜单*/
    private void initNaView() {
        mMainNv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.side_bar1:
                        Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                        intent.putExtra("URL", "https://github.com/wapchief");
                        startActivity(intent);
                        break;
                    case R.id.side_bar2:
                        Intent intent1 = new Intent(MainActivity.this, WebViewActivity.class);
                        intent1.putExtra("URL", "http://blog.csdn.net/wapchief");
                        startActivity(intent1);
                        break;
                    case R.id.side_bar3:
                        Intent intent2 = new Intent(MainActivity.this, WebViewActivity.class);
                        intent2.putExtra("URL", "http://www.jianshu.com/users/9f0bedd0835c");
                        startActivity(intent2);
                        break;
                    case R.id.side_bar4:
                        Intent intent3 = new Intent(MainActivity.this, WebViewActivity.class);
                        intent3.putExtra("URL", "http://wapchief.github.io");
                        startActivity(intent3);
                        break;
                    case R.id.side_bar5:
                        Intent intent4 = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.side_bar6:
                        Intent intent5 = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(intent5);
                        break;
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.side_main);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.title_bar_back, R.id.title_options_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_bar_back:
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.side_main);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.title_options_img:
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setItems(new String[]{"创建群组","添加好友/群"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showToast(MainActivity.this, "" + i);

                    }
                }).create();
                dialog.show();
                Window window = dialog.getWindow();
                WindowManager.LayoutParams params = window.getAttributes();
                window.setGravity(Gravity.RIGHT | Gravity.TOP);
                params.width = UIUtils.dip2px(this,200);
                params.y = UIUtils.dip2px(this,55);
                dialog.getWindow().setAttributes(params);


                break;
        }
    }

    /*初始化Pager*/
    private void initPageAdapter() {
        mMainRootTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                titleChange(position);
                mMainRootVp.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
//                Log.e("positionTAB", ":"+position);
                if (position == 0) {
//                    mMainRootTab.showMsg(0, mRandom.nextInt(100) + 1);
//                    UnreadMsgUtils.show(mTabLayout_2.getMsgView(0), mRandom.nextInt(100) + 1);
                }
            }
        });

        mMainRootVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                titleChange(position);
                mMainRootTab.setCurrentTab(position);
            }

            public void onPageScrollStateChanged(int state) {

            }
        });

        mMainRootVp.setCurrentItem(0);
    }

    /*设置title变化*/
    private void titleChange(int position) {
        switch (position) {
            case 0:
                mTitleBarTitle.setText("消息");
                mTitleOptionsImg.setVisibility(View.VISIBLE);
                mTitleOptionsTv.setVisibility(View.GONE);
                break;
            case 1:
                mTitleBarTitle.setText("联系人");
                mTitleOptionsImg.setVisibility(View.GONE);
                mTitleOptionsTv.setVisibility(View.VISIBLE);
                mTitleOptionsTv.setText("添加");
                break;
            case 2:
                mTitleBarTitle.setText("动态");
                mTitleOptionsImg.setVisibility(View.GONE);
                mTitleOptionsTv.setVisibility(View.VISIBLE);
                mTitleOptionsTv.setText("更多");
                break;
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = FragmentFactory.createFragment(position);
            return fragment;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
