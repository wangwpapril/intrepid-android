
package com.swishlabs.intrepid_android.customViews;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.swishlabs.intrepid_android.R;

import java.util.ArrayList;
import java.util.List;


public class IndicatorLinearLayout extends LinearLayout {

    private List<ImageView> points = new ArrayList<ImageView>();
    private Context context;
    private int selectedIndex;
    private ViewPager mViewPager;

    public IndicatorLinearLayout(Context context) {
        super(context);
        this.context = context;
    }

    public IndicatorLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void initPoints(int count, int selectedIndex, ViewPager viewPager) {
    	this.removeAllViews();
        this.selectedIndex = selectedIndex;
        this.mViewPager = viewPager;

        for (int i = 0; i < count; i++) {
            ImageView point = new ImageView(context);
            point.setImageResource(R.drawable.home_indirector_point);
            if (i == selectedIndex) {
                point.setSelected(true);
            } else {
                point.setSelected(false);
            }
            point.setTag(i);

            points.add(i, point);
            point.setOnClickListener( new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = (int) v.getTag();
                    indicator(index);
                    mViewPager.setCurrentItem(index);
                }
            });

            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 15;
            this.addView(points.get(i),params);
        }
        invalidate();
    }

    public void indicator(int nextSelectedIndex) {
        points.get(selectedIndex).setSelected(false);
        points.get(nextSelectedIndex).setSelected(true);
        this.selectedIndex = nextSelectedIndex;
        invalidate();
    }

/*    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }*/

/*    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }*/

/*    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        onTouchEvent(ev);
//        return super.dispatchTouchEvent(ev);
        return false;
    }*/


}
