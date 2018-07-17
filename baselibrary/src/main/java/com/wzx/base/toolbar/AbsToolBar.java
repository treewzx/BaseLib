package com.wzx.base.toolbar;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public abstract class AbsToolBar<P extends AbsToolBar.Builder.Params> implements IToolBar {
    private static final Integer DEFAULE_COLOR = 0xff000000;
    private  P mParams;
    public View toolbar;

    protected AbsToolBar(P params) {
        this.mParams = params;
        createAndBindToolBar();
    }

    //绑定ToolBar的布局并且创建ToolBar
    private void createAndBindToolBar() {
        ViewGroup decorView = null;
        if(mParams.mContext.get() instanceof Activity){
            decorView = (ViewGroup) ((Activity)(mParams.mContext).get()).getWindow().getDecorView();
        }else {
            throw new IllegalArgumentException("参数必须是Activity");
        }

        ViewGroup rootView = (ViewGroup) decorView.getChildAt(0);//LinearLayout
        toolbar = LayoutInflater.from((mParams.mContext).get()).inflate(bindLayoutRes(), rootView, false);
        rootView.addView(toolbar, 0);
        applyToolBarView();
    }


    public P getParams() {
        return mParams;
    }

    protected void setText(int viewId, String title) {
        TextView tv = findViewById(viewId);
        if (tv != null && !TextUtils.isEmpty(title)) {
            tv.setText(title);
        }
    }

    protected  void setToolBarBackgroundColor(@ColorInt int color){
        if(toolbar!=null){
            toolbar.setBackgroundColor(color);
        }
    }

    private <T extends View> T findViewById(int viewId) {

        return (T) toolbar.findViewById(viewId);
    }

    public abstract static class Builder {

        public abstract AbsToolBar build();

        public static class Params {
            public WeakReference<Context> mContext;

            protected Params(WeakReference<Context> context) {
                this.mContext = context;
            }
        }
    }
}
