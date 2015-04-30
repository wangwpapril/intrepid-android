
package com.swishlabs.intrepid_android.customViews;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.swishlabs.intrepid_android.R;

import java.util.ArrayList;
import java.util.List;


public class IndicatorLinearLayout extends LinearLayout {

    private List<ImageView> points = new ArrayList<ImageView>();
    private Context context;
    private int selectedIndex;
    private ViewPager mViewPager;
    private int totalNum;

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
        this.totalNum = count;

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

    private VelocityTracker mVelocityTracker;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        final int action = event.getAction();
        final float x = event.getRawX();
        switch (action) {
            case MotionEvent.ACTION_DOWN:


                // Remember where the motion event started

                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000);
                int velocityX= (int) velocityTracker.getXVelocity();
                if (velocityX > 150) {
                    if(selectedIndex < totalNum-1) {
                        mViewPager.setCurrentItem(selectedIndex+1);
                    }

                } else if (velocityX < -150) {
                    if(selectedIndex > 0) {
                        mViewPager.setCurrentItem(selectedIndex-1);
                    }
                }

                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }

                break;
            case MotionEvent.ACTION_CANCEL:

        }
        return true;
    }


}
