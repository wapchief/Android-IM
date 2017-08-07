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
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.utils.UnreadMsgUtils;
import com.flyco.tablayout.widget.MsgView;
import com.wapchief.jpushim.activity.AboutActivity;
import com.wapchief.jpushim.activity.AddFriendsActivity;
import com.wapchief.jpushim.activity.SettingActivity;
import com.wapchief.jpushim.activity.UserActivty;
import com.wapchief.jpushim.activity.WebViewActivity;
import com.wapchief.jpushim.entity.TabEntity;
import com.wapchief.jpushim.fragment.FragmentFactory;
import com.wapchief.jpushim.framework.base.BaseActivity;
import com.wapchief.jpushim.framework.helper.GreenDaoHelper;
import com.wapchief.jpushim.framework.helper.SharedPrefHelper;
import com.wapchief.jpushim.framework.system.SystemStatusManager;
import com.wapchief.jpushim.framework.utils.UIUtils;
import com.wapchief.jpushim.greendao.RequestListDao;
import com.wapchief.jpushim.greendao.SearchAddDao;
import com.wapchief.jpushim.greendao.model.RequestList;
import com.wapchief.jpushim.greendao.model.SearchAdd;
import com.wapchief.jpushim.view.MyAlertDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.event.ContactNotifyEvent;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.android.eventbus.EventBus;

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
    //NavigationViewHeader
    private LinearLayout nav_header_ll;
    private TextView nav_header_name,nav_header_id;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private String[] mTitles = {"消息", "联系人", "动态"};
    private int[] tabIconGray = new int[]{R.mipmap.icon_tab_message_gray, R.mipmap.icon_tab_im_gray, R.mipmap.icon_tab_d_gray};
    private int[] tabIcon = new int[]{R.mipmap.icon_tab_message, R.mipmap.icon_tab_im, R.mipmap.icon_tab_d};
    private SharedPrefHelper helper;
    private GreenDaoHelper daoHelper;
    private RequestListDao dao;

    @Override
    protected int setContentView() {
        return R.layout.activity_main;

    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        JMessageClient.registerEventReceiver(this);
        daoHelper = new GreenDaoHelper(this);
        dao = daoHelper.initDao().getRequestListDao();
        //状态栏
        new SystemStatusManager(this).setTranslucentStatus(R.drawable.shape_titlebar);
        helper = SharedPrefHelper.getInstance();
        mTitleOptionsImg.setVisibility(View.VISIBLE);
        //设置NavigationView
        initNaView();
        initSideDrawer();
        initTab();
        initPageAdapter();
        initNVHeader();
    }

    /*接收好友请求通知*/
    public void onEventMainThread(final ContactNotifyEvent event) {
//        if (event.getType().equals("invite_received")){
            //设置未读消息红点
            mMainRootTab.showDot(1);
            MsgView rtv_2_2 = mMainRootTab.getMsgView(1);
            if (rtv_2_2 != null) {
                //设置小红点大小和位置
                UnreadMsgUtils.setSize(rtv_2_2, UIUtils.dip2px(this, 7.5f));
            }
            //删除已经存在重复的搜索历史.如果来自同一个人的请求，保存最新
            List<RequestList> list = dao.queryBuilder()
                    .where(RequestListDao.Properties.UserName.eq(event.getFromUsername())).build().list();
            dao.deleteInTx(list);
            //添加
             JMessageClient.getUserInfo(event.getFromUsername(), new GetUserInfoCallback() {
                 @Override
                 public void gotResult(int i, String s, UserInfo userInfo) {
                     if (i==0)
                     dao.insert(new RequestList(null, event.getReason().toString(),event.getFromUsername().toString(),userInfo.getNickname(),""));
                 }
             });
//        }
        Log.e("bean===", event.getFromUsername() + "," + event.getReason()+","+event.getType());
    }

    /*初始化NavigationView头部控件*/
    private void initNVHeader() {
//        mMainNv.setItemTextColor(getResources().getColorStateList(R.drawable.nav_select_tv,null));
        mMainNv.setItemIconTintList(null);
        View headerView = mMainNv.getHeaderView(0);
        nav_header_ll = (LinearLayout) headerView.findViewById(R.id.nav_header_ll);
        nav_header_name = (TextView) headerView.findViewById(R.id.nav_header_name);
        nav_header_name.setText(helper.getNakeName());
        nav_header_id =(TextView) headerView.findViewById(R.id.nav_header_id);
        nav_header_id.setText("ID:  "+helper.getUserId());
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        //未读消息标签
        Log.e("activity", JMessageClient.getAllUnReadMsgCount()+"");
        if (JMessageClient.getAllUnReadMsgCount()>0) {
            mMainRootTab.showMsg(0, JMessageClient.getAllUnReadMsgCount());
            mMainRootTab.setMsgMargin(0, -6, 5);
        }else {
            mMainRootTab.setBackgroundColor(00000000);
            mMainRootTab.showMsg(0,0);
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

    @OnClick({R.id.title_bar_back, R.id.title_options_img,R.id.title_options_tv})
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
                MyAlertDialog dialog = new MyAlertDialog(
                        MainActivity.this, new String[]{"创建群组", "添加好友／群"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i){
                                    case 0:
                                        showToast(MainActivity.this, "暂未开放" );
                                        break;
                                    case 1:
                                        Intent intent = new Intent(MainActivity.this, AddFriendsActivity.class);
                                        startActivity(intent);
                                        break;
                                }
                            }
                        });
                dialog.initDialog(Gravity.RIGHT|Gravity.TOP);
                dialog.dialogSize(200,0,0,55);
                break;
            case R.id.title_options_tv:
                MyAlertDialog dialog1 = new MyAlertDialog(
                        MainActivity.this, new String[]{"创建群组", "添加好友／群"},
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                showToast(MainActivity.this, "暂未开放" );
                                break;
                            case 1:
                                Intent intent = new Intent(MainActivity.this, AddFriendsActivity.class);
                                startActivity(intent);
                                break;
                        }
                    }
                });
                dialog1.initDialog(Gravity.TOP|Gravity.RIGHT);
                dialog1.dialogSize(200,0,0,55);
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

    /**
     * 单击回退
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();
        }
        return false;
    }

    /**
     * 双击退出
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            showLongToast(this,"再按一次退出程序");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
