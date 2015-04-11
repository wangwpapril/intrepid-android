package com.swishlabs.intrepid_android.customViews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
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


    @Override
    public boolean onTouchEvent(MotionEvent event) {
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
                        params.height = this.getLayoutParams().height + mDeltaY;
                        this.setLayoutParams(params);
                    }
                }


                break;
            case MotionEvent.ACTION_UP:
                if (this.getHeight() < convertDPtoPixels(150)){
                    TranslateAnimation anim = new TranslateAnimation(0, 0, 0, (this.getHeight()-convertDPtoPixels(10)));
                    anim.setDuration(600);
                    final ScrollView scroller = this;
                    this.startAnimation(anim);
                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                                    scroller.getLayoutParams();
                            params.height = convertDPtoPixels(10);
                            scroller.setLayoutParams(params);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                }else{
                    
                }
                break;
            case MotionEvent.ACTION_CANCEL:

             }
       return true;
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
