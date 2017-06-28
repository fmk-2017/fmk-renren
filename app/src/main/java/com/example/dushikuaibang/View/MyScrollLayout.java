package com.example.dushikuaibang.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.example.dushikuaibang.Interface.OnViewChangeListener;
import com.example.dushikuaibang.Layout.PercentRelativeLayout;

public class MyScrollLayout extends PercentRelativeLayout {
    private static final int SNAP_VELOCITY = 600;
    private static final int TOUCH_STATE_REST = 0;
    private static final int TOUCH_STATE_SCROLLING = 1;
    private VelocityTracker mVelocityTracker;
    private Scroller mScroller;
    private int mCurScreen;
    private int mDefaultScreen = 0;
    private float mLastMotionY;
    private int mTouchSlop;
    private int mTouchState = TOUCH_STATE_REST;

    private OnViewChangeListener mOnViewChangeListener;

    public MyScrollLayout(Context context) {
        super(context);
        init(context);
    }

    public MyScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyScrollLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mCurScreen = mDefaultScreen;
        mScroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int childTop = 0;
            final int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View childView = getChildAt(i);
                if (childView.getVisibility() != View.GONE) {
                    final int childHeight = childView.getMeasuredHeight();
                    childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
                    childTop = childTop + childHeight;
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY)
            throw new IllegalStateException("ScrollLayout only can run at EXACTLY mode!");
        final int hightModed = MeasureSpec.getMode(heightMeasureSpec);
        if (hightModed != MeasureSpec.EXACTLY)
            throw new IllegalStateException("ScrollLayout only can run at EXACTLY mode!");

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }
        scrollTo(mCurScreen * height, 0);
    }

    public void snapToDestination() {
        final int screenHeight = getHeight();
        final int destScreen = (getScrollY() + screenHeight / 2) / screenHeight;
        snapToScreen(destScreen);
    }

    public void snapToScreen(int whichScreen) {
        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
        if (getScrollY() != (whichScreen * getHeight())) {
            final int delta = whichScreen * getHeight() - getScrollY();
            mScroller.startScroll(0, getScrollY(), 0, delta, Math.abs(delta) * 2);
            mCurScreen = whichScreen;
            invalidate();
            if (mOnViewChangeListener != null) {
                mOnViewChangeListener.OnViewChange(mCurScreen);
            }
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null)
            mVelocityTracker = VelocityTracker.obtain();
        mVelocityTracker.addMovement(event);
        final int action = event.getAction();
        final float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                    mVelocityTracker.addMovement(event);
                }
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY = (int) (mLastMotionY - y);
                if (IsCanMove(deltaY)) {
                    if (mVelocityTracker != null)
                        mVelocityTracker.addMovement(event);
                    mLastMotionY = y;
                    scrollBy(deltaY, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                int velocityY = 0;
                if (mVelocityTracker != null) {
                    mVelocityTracker.addMovement(event);
                    mVelocityTracker.computeCurrentVelocity(1000);
                    velocityY = (int) mVelocityTracker.getYVelocity();
                }
                if (velocityY > SNAP_VELOCITY && mCurScreen > 0)
                    snapToScreen(mCurScreen - 1);
                else if (velocityY < -SNAP_VELOCITY && mCurScreen < getChildCount() - 1)
                    snapToScreen(mCurScreen + 1);
                else snapToDestination();

                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                mTouchState = TOUCH_STATE_REST;
                break;
            default:
                break;
        }
        return true;
    }

    private boolean IsCanMove(int deltaY) {
        if (getScrollY() <= 0 && deltaY < 0) {
            return false;
        }
        if (getScrollY() >= (getChildCount() - 1) * getWidth() && deltaY > 0) {
            return false;
        }
        return true;
    }

    public void SetOnViewChangeListener(OnViewChangeListener listener) {
        mOnViewChangeListener = listener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && mTouchState != TOUCH_STATE_REST)
            return true;
        final float y = ev.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = y;
                mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
                break;
            case MotionEvent.ACTION_MOVE:
                final int yDiff = (int) Math.abs(mLastMotionY - y);
                if (yDiff > mTouchSlop)
                    mTouchState = TOUCH_STATE_SCROLLING;
                break;
            case MotionEvent.ACTION_UP:
                mTouchState = TOUCH_STATE_REST;
                break;
        }
        return mTouchState != TOUCH_STATE_REST;
    }
}
