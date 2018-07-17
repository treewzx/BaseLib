package com.wzx.base.toolbar;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.wzx.base.mvp.R;

import java.lang.ref.WeakReference;

public class DefaultToolBar extends AbsToolBar<DefaultToolBar.Builder.Params> {
    private DefaultToolBar(Builder.Params params) {
        super(params);

    }

    @Override
    protected void setToolBarBackgroundColor(int toolbarColor) {
        if(getParams().isStatusBarImmersed){
            setStatusBarColor(toolbarColor);
        }
        super.setToolBarBackgroundColor(toolbarColor);
    }

   private void setStatusBarColor(int toolbarColor){
       if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT
               && android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
           ((Activity) (getParams().mContext).get()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
           if (toolbar != null) {
               //1.先设置toolbar的高度
               ViewGroup.LayoutParams params = toolbar.getLayoutParams();
               int statusBarHeight = AndroidBarUtils.getStatusBarHeight(getParams().mContext.get());
               params.height += statusBarHeight;
               toolbar.setLayoutParams(params);
               //2.设置paddingTop，以达到状态栏不遮挡toolbar的内容。
               toolbar.setPadding(
                       toolbar.getPaddingLeft(),
                       toolbar.getPaddingTop() + statusBarHeight,
                       toolbar.getPaddingRight(),
                       toolbar.getPaddingBottom());
           }
       } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
           ((Activity) (getParams().mContext).get()).getWindow().setStatusBarColor(toolbarColor);

       }
   }

    @Override
    public int bindLayoutRes() {
        return R.layout.layout_toolbar;
    }

    @Override
    public void applyToolBarView() {
        setText(R.id.title, getParams().mTitle);
       if (getParams().mToolBarColor != null) {
            setToolBarBackgroundColor(getParams().mToolBarColor);
        }

    }
    //Builder用于设置所有效果
    public static class Builder extends AbsToolBar.Builder {
        private Params defaultToolbarParams;

        public Builder(Context context) {
            defaultToolbarParams = new Params(new WeakReference<>(context));
        }

        @Override
        public DefaultToolBar build() {
            DefaultToolBar toolBar = new DefaultToolBar(defaultToolbarParams);
            return toolBar;
        }

        public Builder setTitle(String title) {
            defaultToolbarParams.mTitle = title;
            return this;
        }

        public Builder setToolBarColor(@ColorInt Integer toolBarColor) {
            defaultToolbarParams.mToolBarColor = toolBarColor;
            return this;
        }
        public Builder setStatusBarImmersed(boolean isImmersed) {
            defaultToolbarParams.isStatusBarImmersed = isImmersed;
            return this;
        }


        //Params用于放置所有效果的参数
        public static class Params extends AbsToolBar.Builder.Params {
            public String mTitle;
            public Integer mToolBarColor;
            public boolean isStatusBarImmersed;

            public Params(WeakReference<Context> context) {
                super(context);
            }

        }
    }
}
