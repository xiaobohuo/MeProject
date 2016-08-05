package com.dom.a10174987.meproject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by 10174987 on 2016/8/4.
 */

public class HorizontalLayoutEx extends ViewGroup {

    private Scroller scroller;
    private VelocityTracker velocityTracker;

    private int mLastX = 0;
    private int mLastY = 0;
    private int mLastInterceptX = 0;
    private int mLastInterceptY = 0;
    private int mChildIndex = 0;
    private int mChildWidth;

    private int childCount;

    public HorizontalLayoutEx(Context context) {
        this(context, null);
    }

    public HorizontalLayoutEx(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalLayoutEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        scroller = new Scroller(getContext());
        velocityTracker = VelocityTracker.obtain();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        int childCount = getChildCount();

        if (childCount == 0) {
            setMeasuredDimension(0, 0);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            View child = getChildAt(0);
            setMeasuredDimension(child.getMeasuredWidth() * childCount, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            View child = getChildAt(0);
            setMeasuredDimension(widthSpecSize, child.getMeasuredHeight());
        } else {
            View child = getChildAt(0);
            setMeasuredDimension(childCount * child.getMeasuredWidth(), child.getMeasuredHeight());
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;
        childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            mChildWidth = child.getMeasuredWidth();
            child.layout(childLeft, 0, childLeft + child.getMeasuredWidth(), child.getMeasuredHeight());
            childLeft += child.getMeasuredWidth();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                    intercept = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getX() - mLastInterceptX) > Math.abs(ev.getY() - mLastInterceptY)) {
                    intercept = true;
                } else {
                    intercept = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
        }
        mLastInterceptX = (int) ev.getX();
        mLastInterceptY = (int) ev.getY();
        mLastX = (int) ev.getX();
        mLastY = (int) ev.getY();
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        velocityTracker.addMovement(event);
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = x - mLastX;
                int dy = y - mLastY;
                scrollBy(-dx, 0);
                break;
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                velocityTracker.computeCurrentVelocity(1000);
                float xVelocity = velocityTracker.getXVelocity();
                if (Math.abs(xVelocity) > 100) {
                    mChildIndex = xVelocity > 0 ? mChildIndex - 1 : mChildIndex + 1;
                } else {
                    mChildIndex = (scrollX + mChildWidth / 2) / mChildWidth;
                }
                mChildIndex = Math.max(0, Math.min(mChildIndex, childCount - 1));
                int detaX = mChildIndex * mChildWidth -scrollX;
                smoothScrollBy(detaX, 0);
                velocityTracker.clear();
                break;
        }
        mLastX = x;
        mLastY = y;

        return true;
    }

    private void smoothScrollBy(int dx, int dy) {
        scroller.startScroll(getScrollX(), 0, dx, 0, 600);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        velocityTracker.recycle();
        super.onDetachedFromWindow();
    }
}
