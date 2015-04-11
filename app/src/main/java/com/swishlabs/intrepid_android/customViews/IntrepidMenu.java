package com.swishlabs.intrepid_android.customViews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.activity.SettingsActivity;
import com.swishlabs.intrepid_android.activity.TripPagesActivity;

/**
 * Created by ryanracioppo on 2015-04-09.
 */



public class IntrepidMenu extends ScrollView {

    private int mInitialHeight =0;

    public IntrepidMenu(Context context) {
        super(context);
        mInitialHeight = this.getLayoutParams().height;
    }

    public IntrepidMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInitialHeight = this.getMeasuredHeight();


    }

    public IntrepidMenu (Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        mInitialHeight = this.getLayoutParams().height;
    }

    @Override
    public void onScrollChanged(int x, int y, int oldx, int oldy){
        super.onScrollChanged(x, y, oldx, oldy);

//        final float scale = getContext().getResources().getDisplayMetrics().density;
//        int dps = 10;
//        int width = this.getWidth();
//        int pixels = (int) (dps * scale + 0.5f);
//
//        this.setLayoutParams(new RelativeLayout.LayoutParams(width, y));
//        this.setScrollY(0);
    }
    private int maxScroll = 0;
    private float mLastMotionY;
    private int mDeltaY = 0;
    private int mLastDeltaY = 0;
    private VelocityTracker mVelocityTracker;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        final int action = event.getAction();
        final float y = event.getRawY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:


                // Remember where the motion event started

                break;
            case MotionEvent.ACTION_MOVE:
                if (maxScroll == 0) {
                    maxScroll = this.getChildAt(0).getLayoutParams().height;
                }

                mDeltaY = (int) (mLastMotionY - y);

                mLastMotionY = y;
                Log.e("y", y+"is y");
                Log.e("delta", mDeltaY+" is deltay");
                if (mDeltaY < 100 && mDeltaY > -100) {
                    if (mDeltaY > 0 && this.getHeight()> convertDPtoPixels(300)){

                    }else {
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                                this.getLayoutParams();
                        int minHeight =  Math.min(this.getLayoutParams().height + mDeltaY, convertDPtoPixels(300));
                        params.height = Math.max(minHeight,convertDPtoPixels(25));

                        this.setLayoutParams(params);
                    }
                }


                break;
            case MotionEvent.ACTION_UP:
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000);
                int velocityY= (int) velocityTracker.getYVelocity();
                Log.e("VELOCITY", velocityY+"");
                if (velocityY > 600) {
                    snapToBottom();


                } else if (velocityY < -600) {
                    snapToTop();
                } else {
                    if (this.getHeight() < convertDPtoPixels(150)) {
                        snapToBottom();
                    }else{
                        snapToTop();
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

    private void snapToBottom() {

            TranslateAnimation anim = new TranslateAnimation(0, 0, 0, (this.getHeight() - convertDPtoPixels(25)));
            anim.setDuration(300);
            final ScrollView scroller = this;
            this.startAnimation(anim);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    scroller.setDrawingCacheEnabled(true);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                            scroller.getLayoutParams();
                    params.height = convertDPtoPixels(25);
                    scroller.setLayoutParams(params);
                    scroller.setDrawingCacheEnabled(false);
                    scroller.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

    }

    private void snapToTop(){
        int initial_position = this.getHeight();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                this.getLayoutParams();
        params.height = convertDPtoPixels(300);
        this.setLayoutParams(params);

        TranslateAnimation anim = new TranslateAnimation(0, 0, convertDPtoPixels(300)-initial_position, 0);
        anim.setDuration(300);
        this.startAnimation(anim);
    }

    private int convertDPtoPixels(int dp){
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (dp * scale + 0.5f);
        return pixels;
    }

    public static void setupMenu(final Context context, final Activity activity){
        FrameLayout settingsButton = (FrameLayout)activity.findViewById(R.id.settings_menu_btn);
        settingsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(context, SettingsActivity.class);
                activity.startActivity(mIntent);
            }
        });
        FrameLayout tripsButton = (FrameLayout)activity.findViewById(R.id.trips_menu_btn);
        tripsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(context, TripPagesActivity.class);
                activity.startActivity(mIntent);
            }
        });

    }


}
