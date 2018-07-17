package com.wzx.base.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import static com.wzx.base.utils.Preconditions.checkNotNull;

public class AlertController {

    private AlertDialog mDialog;
    private Window mWindow;
    private DialogViewHelper mViewHelper;

    private AlertController(AlertDialog dialog, Window window) {
        this.mDialog = dialog;
        this.mWindow = window;
    }

    public static AlertController Create(AlertDialog dialog, Window window) {
        return new AlertController(dialog, window);
    }

    /**
     * 获取Dialog
     *
     * @return
     */
    public AlertDialog getDialog() {
        return mDialog;
    }

    /**
     * 获取Dialog的窗体
     *
     * @return
     */
    public Window getWindow() {
        return mWindow;
    }

    public <T extends View> T getView(int viewId) {
        return mViewHelper.getView(viewId);
    }

    public void setViewHelper(DialogViewHelper viewHelper) {
        mViewHelper = viewHelper;
    }

    public void setText(int viewId, CharSequence text) {
        mViewHelper.setText(viewId, text);
    }

    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        mViewHelper.setOnClickListener(viewId, listener);
    }


    public static class AlertParams {
        public Context mContext;
        public int mThemeResId;
        //布局View
        public View mContentView;
        //布局的id
        public int mLayoutResId;
        //点击空白是否消失
        public boolean mCancelable = true;
        //dialog cancel监听
        public DialogInterface.OnCancelListener mOnCancelListener;
        //dialog dismiss监听
        public DialogInterface.OnDismissListener mOnDismissListener;
        //dialog key监听
        public DialogInterface.OnKeyListener mOnKeyListener;
        //存放Text的集合
        public SparseArray<CharSequence> mTextArray = new SparseArray<>();
        //dialog中View的点击监听
        public SparseArray<View.OnClickListener> mClickArray = new SparseArray<>();
        //dialog的宽度
        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        //dialog的高度
        public int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        //dialog的位置
        public int mGravity = Gravity.CENTER;
        //设置动画
        public int mAnimations;

        public AlertParams(Context context, int themeResId) {
            this.mContext = context;
            this.mThemeResId = themeResId;
        }

        /**
         * 设置参数,也就是将Builder中设置的参数应用到dialog中
         *
         * @param alert
         */
        public void apply(AlertController alert) {

            DialogViewHelper viewHelper = null;
            if (mLayoutResId != 0) {
                viewHelper = new DialogViewHelper(mContext, mLayoutResId);
            }
            if (mContentView != null) {
                viewHelper = new DialogViewHelper(mContentView);
            }
            checkNotNull(viewHelper, "the ContentView of dialog cannot be null");
            alert.setViewHelper(viewHelper);
            //1.给dialog设置布局
            alert.getDialog().setContentView(viewHelper.getContentView());
            //设置文本
            for (int i = 0; i < mTextArray.size(); i++) {
                alert.setText(mTextArray.keyAt(i), mTextArray.valueAt(i));
            }
            //设置点击事件
            for (int i = 0; i < mClickArray.size(); i++) {
                alert.setOnClickListener(mClickArray.keyAt(i), mClickArray.valueAt(i));
            }
            Window window = alert.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = mWidth;
            params.height = mHeight;
            params.gravity = mGravity;
            params.windowAnimations = mAnimations;
            window.setAttributes(params);
        }
    }
}
