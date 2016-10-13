package com.sajib.chitchat.binding;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by sajib on 05-07-2016.
 */
public class BindingAdapters {

    @BindingAdapter("app:Click")
    public static void onClick(View view, final Runnable runnable) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runnable.run();
            }
        });
    }
    @BindingAdapter("app:src")
    public static void getPic(ImageView imageView, Drawable drawable)
    {
        imageView.setImageDrawable(drawable);
    }
    @BindingAdapter("app:text")
    public static void getText(TextView textView,String text)
    {
        textView.setText(text);

    }


    @BindingAdapter("app:setbitmap")
    public static void getbitmap(ImageView imageView,String url)
    {
        Glide.with(imageView.getContext()).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(imageView);
    }

}

