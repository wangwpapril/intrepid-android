package com.swishlabs.intrepid_android.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Scroller;

/**
 * Created by ryanracioppo on 2015-04-09.
 */
public class IntrepidMenu extends ViewGroup {



    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    private int mScrollY = 0;


    private View mViewYourProfileRootview;

    public void setViewYourProfileFragment(View rootView){
        mViewYourProfileRootview = rootView;

    }




    private int mCurrentScreen = 0;
    private double mLastDelta = 0;

    private float mLastMotionY;

    private static final String LOG_TAG = "DragableSpace";

    private static final int SNAP_VELOCITY = 600;

    private final static int TOUCH_STATE_REST = 0;
    private final static int TOUCH_STATE_SCROLLING = 1;

    private int mTouchState = TOUCH_STATE_REST;

    private int mTouchSlop = 0;

    public IntrepidMenu(Context context) {
        super(context);
        mScroller = new Scroller(context);
        mScroller.setFriction(0.3F);

        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        this.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.FILL_PARENT));
    }

    public int getCurrentScreen() {
        return mCurrentScreen;
    }

    public IntrepidMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);

        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        this.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT ,
                ViewGroup.LayoutParams.FILL_PARENT));

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        /*
         * This method JUST determines whether we want to intercept the motion.
         * If we return true, onTouchEvent will be called and we do the actual
         * scrolling there.
         */

        /*
         * Shortcut the most recurring case: the user is in the dragging state
         * and he is moving his finger. We want to intercept this motion.
         */
        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE)
                && (mTouchState != TOUCH_STATE_REST)) {
            return true;
        }

        final float y = ev.getY();

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                /*
                 * mIsBeingDragged == false, otherwise the shortcut would have caught it. Check
                 * whether the user has moved far enough from his original down touch.
                 */

                /*
                 * Locally do absolute value. mLastMotionX is set to the y value
                 * of the down event.
                 */
                final int xDiff = (int) Math.abs(y - mLastMotionY);

                boolean xMoved = xDiff > mTouchSlop;

                if (xMoved) {
                    // Scroll if the user moved far enough along the X axis
                    mTouchState = TOUCH_STATE_SCROLLING;
                }
                break;

            case MotionEvent.ACTION_DOWN:
                // Remember location of down touch
                mLastMotionY = y;

                /*
                 * If being flinged and user touches the screen, initiate drag;
                 * otherwise don't.  mScroller.isFinished should be false when
                 * being flinged.
                 */
                mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                // Release the drag
                mTouchState = TOUCH_STATE_REST;
                break;
        }

        /*
         * The only time we want to intercept motion events is if we are in the
         * drag mode.
         */
        return mTouchState != TOUCH_STATE_REST;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        final int action = event.getAction();
        final float y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.i(LOG_TAG, "event : down");
                /*
                 * If being flinged and user touches, stop the fling. isFinished
                 * will be false if being flinged.
                 */
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                // Remember where the motion event started
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:

                // Log.i(LOG_TAG,"event : move");
                // if (mTouchState == TOUCH_STATE_SCROLLING) {
                // Scroll to follow the motion event
                final int deltaY = (int) (mLastMotionY - y);
                mLastMotionY = y;

                TranslateAnimation anim = new TranslateAnimation(0, 0, 0, -deltaY);
                Log.d("Dragable", deltaY+" is deltaY");
                anim.setDuration(1);
                anim.setFillAfter(true);
//

//                this.startAnimation(anim);

                this.setY(getY() - deltaY);


                // }
                break;
            case MotionEvent.ACTION_UP:
                Log.i(LOG_TAG, "event : up");

                // if (mTouchState == TOUCH_STATE_SCROLLING) {
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000);
                int velocityY = (int) velocityTracker.getYVelocity();

                if (velocityY > SNAP_VELOCITY && mCurrentScreen > 0) {
                    // Fling hard enough to move left
//                    snapToScreen(mCurrentScreen - 1);


                } else if (velocityY < -SNAP_VELOCITY
                        && mCurrentScreen < getChildCount() - 1) {
                    // Fling hard enough to move right
//                    snapToScreen(mCurrentScreen + 1);
                } else {
//
                }

                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                // }
                mTouchState = TOUCH_STATE_REST;
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.i(LOG_TAG, "event : cancel");
                mTouchState = TOUCH_STATE_REST;
        }
        mScrollY = this.getScrollY();

        return true;
    }




    private void snapToDestination() {
        final int screenWidth = getWidth();
        final int whichScreen = (mScrollY + (screenWidth / 2)) / screenWidth;
        Log.i(LOG_TAG, "from des");
        snapToScreen(whichScreen);
    }

    public void snapToScreen(int whichScreen) {
        Log.i(LOG_TAG, "snap To Screen " + whichScreen);
        mCurrentScreen = whichScreen;
        final int newX = whichScreen * getWidth();
        final int delta = newX - mScrollY;
        mScroller.startScroll(mScrollY, 0, delta, 0, Math.abs(delta) * 1);
        invalidate();
    }

    public void setToScreen(int whichScreen) {
        Log.i(LOG_TAG, "set To Screen " + whichScreen);
        mCurrentScreen = whichScreen;
        final int newX = whichScreen * getWidth();
        mScroller.startScroll(newX, 0, 0, 0, 10);
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                final int childWidth = child.getMeasuredWidth();
                child.layout(childLeft, 0, childLeft + childWidth, child
                        .getMeasuredHeight());
                childLeft += childWidth;
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        if (widthMode != MeasureSpec.EXACTLY) {
//            throw new IllegalStateException("error mode.");
//        }
//
//        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        if (heightMode != MeasureSpec.EXACTLY) {
//            throw new IllegalStateException("error mode.");
//        }

        // The children are given the same width and height as the workspace
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }
        Log.i(LOG_TAG, "moving to screen "+mCurrentScreen);
        scrollTo(mCurrentScreen * width, 0);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mScrollY = mScroller.getCurrX();
            scrollTo(mScrollY, 0);
            postInvalidate();
        }
    }
}
