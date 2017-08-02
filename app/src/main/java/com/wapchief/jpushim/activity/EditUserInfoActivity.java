package com.wapchief.jpushim.activity;

import android.app.Dialog;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.jpush.Gson;
import com.wapchief.jpushim.R;
import com.wapchief.jpushim.entity.RegionBean;
import com.wapchief.jpushim.framework.base.BaseActivity;
import com.wapchief.jpushim.framework.helper.SharedPrefHelper;
import com.wapchief.jpushim.framework.utils.StringUtils;
import com.wapchief.jpushim.framework.utils.TimeUtils;
import com.wapchief.jpushim.greendao.model.User;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by wapchief on 2017/7/31.
 * 编辑个人资料
 */

public class EditUserInfoActivity extends BaseActivity {
    @BindView(R.id.title_bar_back)
    ImageView mTitleBarBack;
    @BindView(R.id.title_bar_title)
    TextView mTitleBarTitle;
    @BindView(R.id.edit_user_avatar)
    TextView mEditUserAvatar;
    @BindView(R.id.edit_user_signature)
    EditText mEditUserSignature;
    @BindView(R.id.edit_user_nackName)
    EditText mEditUserNackName;
    @BindView(R.id.edit_user_gender)
    TextView mEditUserGender;
    @BindView(R.id.edit_user_birthday)
    TextView mEditUserBirthday;
    @BindView(R.id.edit_user_address)
    TextView mEditUserAddress;
    @BindView(R.id.bottom_bar_left)
    RelativeLayout mBottomBarLeft;
    @BindView(R.id.bottom_bar_tv2)
    TextView mBottomBarTv2;
    private SharedPrefHelper helper;
    private OptionsPickerView<String> mOptionsPickerView;
    private ArrayList<RegionBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private UserInfo userInfo;
    private Dialog dialog;
    @Override
    protected int setContentView() {
        return R.layout.activity_edit_user;
    }

    @Override
    protected void initView() {
        helper = SharedPrefHelper.getInstance();
        userInfo = JMessageClient.getMyInfo();
        initBar();
        initData();
        initGson();

    }

    private void initBar() {
        mTitleBarBack.setImageDrawable(getResources().getDrawable(R.mipmap.icon_back));
        mTitleBarTitle.setText("编辑资料");
        mBottomBarLeft.setVisibility(View.GONE);
        mBottomBarTv2.setText("保存资料");
    }

    @Override
    protected void initData() {
        mEditUserNackName.setText(JMessageClient.getMyInfo().getNickname()+"");
        mEditUserBirthday.setText(TimeUtils.ms2date("yyyy-MM-dd",JMessageClient.getMyInfo().getBirthday()));
        mEditUserGender.setText(StringUtils.constant2String(JMessageClient.getMyInfo().getGender().name()));
        mEditUserSignature.setText(JMessageClient.getMyInfo().getSignature()+"");
        mEditUserAddress.setText(JMessageClient.getMyInfo().getAddress()+"");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.title_bar_back, R.id.edit_user_avatar, R.id.edit_user_gender, R.id.edit_user_birthday, R.id.edit_user_address, R.id.bottom_bar_tv2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_bar_back:
                finish();
                break;
            case R.id.edit_user_avatar:
                showHeadDialog();
                break;
            case R.id.edit_user_gender:
                initPickViewGender();
                break;
            case R.id.edit_user_birthday:
                initTimePickView();
                break;
            case R.id.edit_user_address:
                initRegionPickView();
                break;
            case R.id.bottom_bar_tv2:
                userInfo.setAddress(mEditUserAddress.getText()+"");
                userInfo.setGender(UserInfo.Gender.valueOf(StringUtils.string2contant(mEditUserGender.getText().toString())));
                userInfo.setBirthday(TimeUtils.date2ms("yyyy-MM-dd",mEditUserBirthday.getText().toString()));
                userInfo.setSignature(mEditUserSignature.getText()+"");
                userInfo.setNickname(mEditUserNackName.getText()+"");
                JMessageClient.updateMyInfo(UserInfo.Field.all, userInfo, new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        if (i==0){
                            showToast(EditUserInfoActivity.this,"更新成功");
                            finish();
                        }else {
                            showToast(EditUserInfoActivity.this,"更新失败："+s);
                        }
                    }
                });
                break;
        }
    }



    /*头像选择器*/
    private void showHeadDialog() {
        TextView update_dialog_TK,update_dialog_PZ,update_dialog_cancel;
        View view = getLayoutInflater().inflate(R.layout.dialog_photo_type, null);
        dialog = new Dialog(this, R.style.dialog_animal);

        Display display = getWindowManager().getDefaultDisplay();
        dialog.setContentView(view, new ViewGroup.LayoutParams(
                display.getWidth()
                , display.getHeight()
        ));
//        dialog.getWindow().setWindowAnimations(R.style.Dialog_Anim_Style);
//        dialog.setCanceledOnTouchOutside(true);
        //图库
        update_dialog_TK=(TextView)view.findViewById(R.id.update_dialog_TK);
        update_dialog_TK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                doHandlerPhoto(PIC_FROM＿LOCALPHOTO);
                dialog.dismiss();
            }
        });
        //取消
        update_dialog_cancel= (TextView)view.findViewById(R.id.update_dialog_cancel);
        update_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        //相机拍照
        update_dialog_PZ = (TextView) view.findViewById(R.id.update_dialog_PZ);
        update_dialog_PZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    /*性别选择器*/
    private void initPickViewGender() {
        final ArrayList<String> options = new ArrayList<>();
        options.add("男");
        options.add("女");
        options.add("未知");
        mOptionsPickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int i, int i1, int i2, View view) {
                mEditUserGender.setText(options.get(i));
            }
        }).build();
        mOptionsPickerView.setPicker(options);
        mOptionsPickerView.show();
    }
    /*日期选择期*/
    public void initTimePickView(){
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                mEditUserBirthday.setText(getTime(date));
            }

        }).setType(new boolean[]{true, true, true, false, false, false})
                .build();
        pvTime.show();
    }

    private String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

   /*地区选择器*/
   protected void initRegionPickView(){
       OptionsPickerView  pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
           @Override
           public void onOptionsSelect(int options1, int options2, int options3, View v) {
               //返回的分别是三个级别的选中位置
               String tx = options1Items.get(options1).getPickerViewText()+
                       options2Items.get(options1).get(options2)+
                       options3Items.get(options1).get(options2).get(options3);
               mEditUserAddress.setText(tx);

           }
       })

               .setTitleText("城市选择")
               .setDividerColor(Color.BLACK)
               .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
               .setContentTextSize(20)
               .setOutSideCancelable(false)// default is true
               .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
       pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器
       pvOptions.show();
   }
    //解析地区
    private void initGson() {
       //将json转化为String
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = this.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open("province.json")));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String jsonData = stringBuilder.toString();
        ArrayList<RegionBean> jsonBean = parseData(jsonData);//用Gson 转成实体

        options1Items = jsonBean;

        for (int i=0;i<jsonBean.size();i++){//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c=0; c<jsonBean.get(i).getCityList().size(); c++){//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        ||jsonBean.get(i).getCityList().get(c).getArea().size()==0) {
                    City_AreaList.add("");
                }else {

                    for (int d=0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }
    }

    /*Gson解析*/
    public ArrayList<RegionBean> parseData(String result) {
        ArrayList<RegionBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                RegionBean entity = gson.fromJson(data.optJSONObject(i).toString(), RegionBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }
}
