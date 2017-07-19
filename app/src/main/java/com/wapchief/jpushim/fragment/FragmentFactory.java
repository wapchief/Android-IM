package com.wapchief.jpushim.fragment;


import android.support.v4.app.Fragment;

import com.wapchief.jpushim.framework.base.BaseFragment;

import java.util.HashMap;

/**
 * Created by wapchief on 2017/7/18.
 * 工厂模式创建Fragment
 */

    public class FragmentFactory {

        private static HashMap<Integer, Fragment> fragments;

        public static Fragment createFragment(int position) {
            fragments = new HashMap<Integer, Fragment>();
            Fragment fragment = fragments.get(position);//从集合中取出Fragment
            if (fragment == null) {//没有在集合中取到再进入实例化过程
                switch (position) {
                    case 0:
                        fragment = new MessageFragment();
                        break;
                    case 1:
                        fragment = new ContactFragment();
                        break;
                    case 2:
                        fragment = new QZoneFragment();
                        break;
                    default:
                        break;
                }
                fragments.put(position, fragment);//存入集合中
            }
            return fragment;
        }
    }


