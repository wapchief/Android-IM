package com.wapchief.jpushim.framework.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ConvertUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.wapchief.jpushim.R;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


/**
 * @author wh
 * @date 2018/2/1
 */

public class GlideUtil {

    private static RequestOptions userHeadImgOptions;
    private static RequestOptions chooseDrawableOptions;
    private static RequestOptions coverPictureOptions;
    private static RequestOptions imageMessageOptions;

    /**
     * 加载用户头像
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadUserHeadImg(Context context, String url, ImageView imageView) {
        if (null == userHeadImgOptions) {
            userHeadImgOptions = new RequestOptions().placeholder(R.drawable.icon_user);
        }
        Glide.with(context).load(url).apply(userHeadImgOptions).into(imageView);
    }


    /**
     * 加载封面图像
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadCoverPicture(Context context, String url, ImageView imageView) {
        if (null == coverPictureOptions) {
            coverPictureOptions = new RequestOptions().placeholder(R.drawable.icon_user);
        }
        Glide.with(context).load(url).apply(coverPictureOptions).into(imageView);
    }

    public static void loadImagMessage(Context context, String url, ImageView imageView) {
        if (null == imageMessageOptions) {
            imageMessageOptions = new RequestOptions().placeholder(R.drawable.icon_user);
        }
        Glide.with(context).load(url).apply(imageMessageOptions).into(imageView);
    }

    public static void loadSmallImageMessage(Context context, String url, final ImageView imageView) {
        if (null == imageMessageOptions) {
            imageMessageOptions = new RequestOptions().placeholder(R.drawable.icon_user);
        }
        Glide.with(context).asBitmap().load(url).apply(imageMessageOptions).into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                if (width > height) {
                    width = (width / height) * 198;
                    height = 198;
                } else {
                    height = (height / width) * 198;
                    width = 198;
                }
                imageView.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
                imageView.setImageBitmap(bitmap);
            }
        });
    }

    /**
     * 加载圆角封面，默认4dp
     */
    public static void loadCornerPicture(Context context, String imgUrl, ImageView imageView) {
        Glide.with(context)
                .load(imgUrl)
                .apply(new RequestOptions().error(R.drawable.icon_user))
                .apply(RequestOptions.bitmapTransform(new MultiTransformation<Bitmap>(new CenterCrop(),
                        new RoundedCornersTransformation(ConvertUtils.dp2px(4), 0))))
                .into(imageView);
    }

    public static void loadCornerPicture(Context context, String imgUrl,ImageView imageView,  int resourceId) {
        Glide.with(context)
                .load(imgUrl)
                .apply(new RequestOptions().error(resourceId))
                .apply(RequestOptions.bitmapTransform(new MultiTransformation<Bitmap>(new CenterCrop(),
                        new RoundedCornersTransformation(ConvertUtils.dp2px(4), 0))))
                .into(imageView);
    }

    public static void loadCornerPicture(Context context, String imgUrl, int cornerRadius, int errorResourceId, ImageView imageView) {
        Glide.with(context)
                .load(imgUrl)
                .apply(new RequestOptions().error(errorResourceId))
                .apply(RequestOptions.bitmapTransform(new MultiTransformation<Bitmap>(new CenterCrop(),
                        new RoundedCornersTransformation(ConvertUtils.dp2px(cornerRadius), 0))))
                .into(imageView);
    }

    /**
     * 加载自定义封面带圆角
     *
     * @param context      上下文
     * @param imgUrl       图片链接
     * @param cornerRadius 圆角弧度
     * @param imageView    view
     */
    public static void loadCornerPicture(Context context, String imgUrl, int cornerRadius, ImageView imageView) {
        Glide.with(context)
                .load(imgUrl)
                .apply(new RequestOptions().error(R.drawable.icon_user))
                .apply(RequestOptions.bitmapTransform(new MultiTransformation<Bitmap>(new CenterCrop(),
                        new RoundedCornersTransformation(ConvertUtils.dp2px(cornerRadius), 0))))
                .into(imageView);
    }

    /**不使用缓存的图片*/
    public static void loadPictureCacheNone(Context context, String imgUrl, ImageView imageView) {
        Glide.with(context)
                .load(imgUrl)
                .apply(new RequestOptions().skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .error(R.drawable.icon_user))
                .into(imageView);
    }

    public static void loadPictureCacheNone(Context context, String imgUrl, int maskImgRes,ImageView imageView) {
        Glide.with(context)
                .load(imgUrl)
                .apply(new RequestOptions().skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .error(maskImgRes))
                .into(imageView);
    }
}
