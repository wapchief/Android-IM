package com.wapchief.jpushim.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.wapchief.jpushim.R;
import com.wapchief.jpushim.framework.base.BaseActivity;
import com.wapchief.jpushim.view.MyFloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wapchief on 2017/7/20.
 */

public class WebViewActivity extends BaseActivity {
    @BindView(R.id.web_progress)
    ProgressBar mWebProgress;
    @BindView(R.id.web_view)
    WebView mWebView;
    @BindView(R.id.web_bt)
    MyFloatingActionButton mWebBt;
    private String urlString;

    @Override
    protected int setContentView() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initView() {
        initIsWebPeoress();
        mWebView.loadUrl(urlString);
    }

    private void initIsWebPeoress() {

        //设置WebView属性，能够执行Javascript脚本
        mWebView.getSettings().setJavaScriptEnabled(true);
        urlString = getIntent().getStringExtra("URL");
        mWebView.setWebViewClient(new WebViewClient() {
            //覆写shouldOverrideUrlLoading实现内部显示网页
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                if (newProgress == 100) {
                    mWebProgress.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    mWebProgress.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    mWebProgress.setProgress(newProgress);//设置进度值
                }

            }
        });


    }

    @OnClick(R.id.web_bt)
    public void onViewClicked() {
        showToast(WebViewActivity.this, "使用浏览器打开");
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(urlString);
        intent.setData(content_url);
        startActivity(intent);

    }

    //设置返回键动作（防止按返回键直接退出程序)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO 自动生成的方法存根
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {//当webview不是处于第一页面时，返回上一个页面
                mWebView.goBack();
                return true;
            } else {//当webview处于第一页面时,直接退出程序
               finish();
            }


        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
