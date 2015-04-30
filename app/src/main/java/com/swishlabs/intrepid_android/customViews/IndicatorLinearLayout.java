
package com.swishlabs.intrepid_android.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.swishlabs.intrepid_android.R;

import java.util.ArrayList;
import java.util.List;


public class IndicatorLinearLayout extends LinearLayout {

    private List<ImageView> points = new ArrayList<ImageView>();
    private Context context;
    private int selectedIndex;

    public IndicatorLinearLayout(Context context) {
        super(context);
        this.context = context;
    }

    public IndicatorLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void initPoints(int count, int selectedIndex) {
    	this.removeAllViews();
        this.selectedIndex = selectedIndex;
        for (int i = 0; i < count; i++) {
            ImageView point = new ImageView(context);
            point.setImageResource(R.drawable.home_indirector_point);
            if (i == selectedIndex) {
                point.setEnabled(true);
            } else {
                point.setEnabled(false);
            }
            points.add(i, point);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.leftMargin = 15;
            this.addView(points.get(i),params);
        }
        invalidate();
    }

    public void indicator(int nextSelectedIndex) {
        points.get(selectedIndex).setEnabled(false);
        points.get(nextSelectedIndex).setEnabled(true);
        this.selectedIndex = nextSelectedIndex;
        invalidate();
    }

}
