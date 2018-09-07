package com.baniya.pintooaggarwal.bookbuyselling.imagelib.utils;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Pintoo Aggarwal on 23-09-2017.
 */
public class Utils {

    public static void loge(String className, String message) {
        Log.e(className, message);
    }



    public static void showLongSnack(View parent, String message){
        Snackbar snackbar = Snackbar.make(parent, message, Snackbar.LENGTH_LONG);
        TextView textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(10);
        snackbar.show();
    }

    public static void initToolBar(AppCompatActivity activity, Toolbar toolbar, boolean homeUpIndicator){
        activity.setSupportActionBar(toolbar);
        final ActionBar ab = activity.getSupportActionBar();
        ab.setDisplayShowHomeEnabled(false);
        ab.setDisplayHomeAsUpEnabled(homeUpIndicator);
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
    }





    public static void setViewBackgroundColor(Activity activity, View view, int color){
        if(color == 0)
            return;
        if(view instanceof ImageButton){
            AppCompatImageButton imageButton = (AppCompatImageButton) view;
            GradientDrawable drawable = (GradientDrawable) imageButton.getBackground();
            drawable.setColor(color);
            if(Build.VERSION.SDK_INT >= 16)
                imageButton.setBackground(drawable);
            else
                imageButton.setBackgroundDrawable(drawable);
        }
        else if(view instanceof ImageView){
            ImageView imageView = (ImageView) view;
            GradientDrawable drawable = (GradientDrawable) imageView.getBackground();
            drawable.setColor(color);
            if(Build.VERSION.SDK_INT >= 16)
                imageView.setBackground(drawable);
            else
                imageView.setBackgroundDrawable(drawable);
        }
        else if(view instanceof Toolbar){
            Toolbar toolbar = (Toolbar) view;
            toolbar.setBackgroundColor(color);
            if(Build.VERSION.SDK_INT >= 21) {
                Window window = activity.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getDarkColor(color));
            }
        }
    }







    public static int getDarkColor(int color){
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.rgb((int)(red * 0.8), (int)(green * 0.8), (int)(blue * 0.8));
    }

    public static int getLightColor(int color){
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb((int)(255*0.5), red, green, blue);
    }
}
