package com.wzx.base.guide;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public abstract class AbsIndicatorView extends View implements ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    private int mCount;

    public AbsIndicatorView(Context context) {
        super(context);
    }

    public AbsIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AbsIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * indicator与ViewPager建立连接
     * @param viewPager
     */
    public void setUpWithViewPager(ViewPager viewPager) {
        releaseViewPager();
        if (viewPager == null) {
            return;
        }
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(this);
        mCount = mViewPager.getAdapter().getCount();
    }

    /**
     * 重置ViewPager
     */
    private void releaseViewPager() {
        if (mViewPager != null) {
            mViewPager.removeOnPageChangeListener(this);
            mViewPager = null;
        }

    }
}
