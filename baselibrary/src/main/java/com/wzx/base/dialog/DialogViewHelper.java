package com.wzx.base.dialog;


import android.content.Context;
import android.support.annotation.IdRes;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Description:dialog的辅助类
 */
class DialogViewHelper {
    private View mContentView;
    private SparseArray<WeakReference<View>> mViewArray;

    public DialogViewHelper(Context context, int layoutResId) {
        mContentView = LayoutInflater.from(context).inflate(layoutResId, null);
        mViewArray = new SparseArray<>();
    }

    public DialogViewHelper(View contentView) {
        mContentView = contentView;
        mViewArray = new SparseArray<>();
    }

    public View getContentView() {
        return mContentView;
    }

    public void setText(@IdRes int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        if (tv != null) {
            tv.setText(text);
        }

    }

    public <T extends View> T getView(@IdRes int viewId) {
        WeakReference<View> viewWeakReference = mViewArray.get(viewId);
        View view = null;
        if (viewWeakReference != null) {
            view = viewWeakReference.get();
        }
        if (view == null) {
            view = mContentView.findViewById(viewId);
            if (view != null) {
                mViewArray.put(viewId, new WeakReference<View>(view));
            }
        }
        return (T) view;
    }
    public void setOnClickListener(int viewId, View.OnClickListener onClicklistener) {
        View view = getView(viewId);
        if (view != null) {
            view.setOnClickListener(onClicklistener);
        }
    }
}
