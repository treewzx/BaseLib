package com.wzx.base.guide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wzx.base.R;
import com.wzx.base.utils.DisplayUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GuideLayout extends FrameLayout implements ViewPager.OnPageChangeListener {
    private List<GuideFragment> fragments;
    private ViewPager viewPager;
    private AbsIndicatorView mIndicator;
    private View button;
    private TextView defaultBtn;
    private int screenWidth;

    public GuideLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    public void setUp(int... ids) {
        setUp(null, ids);
    }

    public void setUp(View jumpBtn, int... ids) {
        setUp(jumpBtn, null, ids);
    }

    public <T extends AbsIndicatorView> void setUp(View jumpBtn, T indicator, int... ids) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        fragments = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            GuideFragment f = new GuideFragment();
            Bundle args = new Bundle();
            args.putInt("layoutId", ids[i]);
            f.setArguments(args);
            fragments.add(f);
        }
        //1.viewpager
        viewPager = new ViewPager(getContext());
        viewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        viewPager.setId(R.id.guide_pager);
        GuideFragmentPageAdapter adapter = new GuideFragmentPageAdapter(((FragmentActivity) getContext()).getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        addView(viewPager, 0);
        viewPager.addOnPageChangeListener(this);

        //2.LinearLayout
        LinearLayout container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.VERTICAL);
        LayoutParams containerLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        containerLayoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        container.setLayoutParams(containerLayoutParams);
        addView(container, 1);

        //2.1.Button（jump to Activity）
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.leftMargin = screenWidth;
        if (jumpBtn == null) {
            defaultBtn = new TextView(getContext());
            defaultBtn.setText("立即体验");
            defaultBtn.setTextColor(Color.WHITE);
            defaultBtn.setBackgroundResource(R.drawable.guidepagebtn_bg);
            button = defaultBtn;
        } else {
            button = jumpBtn;
        }
        // button.setVisibility(View.INVISIBLE);
        container.addView(button, 0);
        button.setLayoutParams(layoutParams);


        //2.2.Indicator
        if (indicator == null) {
            DefaultIndicatorView defaultIndicator = new DefaultIndicatorView(getContext());
            defaultIndicator.setRadius(DisplayUtils.dpToPx(6));
            // 设置Border
            defaultIndicator.setBorderWidth(DisplayUtils.dpToPx(1));
            // 设置选中颜色
            defaultIndicator.setSelectColor(Color.WHITE);
            //设置默认颜色
            defaultIndicator.setDotNormalColor(Color.WHITE);
            // 设置指示器间距
            defaultIndicator.setSpace(DisplayUtils.dpToPx(10));
            // 设置模式
            defaultIndicator.setFillMode(DefaultIndicatorView.FillMode.NONE);
            mIndicator = defaultIndicator;
        } else {
            mIndicator = indicator;
        }
        mIndicator.setUpWithViewPager(viewPager);
        container.addView(mIndicator, 1);//需要先添加才能获取到layoutParams
        LinearLayout.LayoutParams indicatorLayoutParams = (LinearLayout.LayoutParams) mIndicator.getLayoutParams();
        indicatorLayoutParams.gravity = Gravity.CENTER;
        indicatorLayoutParams.topMargin = 60;
        indicatorLayoutParams.bottomMargin = 50;
        mIndicator.setLayoutParams(indicatorLayoutParams);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position == (viewPager.getAdapter().getCount() - 2) && positionOffsetPixels > 0) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button.getLayoutParams();
            layoutParams.leftMargin = (int) (screenWidth - positionOffset * screenWidth);
            button.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void onPageSelected(int position) {
        //此处添加代码是为了确保button水平居中，因为上面的onPageScrolled在快速滑动过程中无法确保获取到的positionOffset的值是0-1的完整过程。
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button.getLayoutParams();
        if (position == (viewPager.getAdapter().getCount() - 1)) {
            layoutParams.leftMargin = 0;
        } else {
            layoutParams.leftMargin = screenWidth;
        }
        button.setLayoutParams(layoutParams);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void setJumpActivity(final Class<?> cls) {
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getCurrentActivity(), cls);
                getCurrentActivity().finish();
                getCurrentActivity().startActivity(intent);
            }
        });

    }

    private Activity getCurrentActivity() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            Map activities = (Map) activitiesField.get(activityThread);
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Activity activity = (Activity) activityField.get(activityRecord);
                    return activity;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
