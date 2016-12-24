package com.bcinfo.tripaway.view.slidebuttompanel;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.ScrollView.ProductDetailScrollView1;



/**
 * Created by NeXT on 15/8/3.
 */
@SuppressLint({ "NewApi", "ClickableViewAccessibility" }) public class SlideBottomPanel extends FrameLayout {

    private static final int TAG_BACKGROUND = 1;
    private static final int TAG_PANEL = 2;

    private static final int DEFAULT_BACKGROUND_ID = -1;
    private static final int DEFAULT_TITLE_HEIGHT_NO_DISPLAY = 60;
    private static final int DEFAULT_PANEL_HEIGHT = 380;
    private static final int DEFAULT_MOVE_DISTANCE_TO_TRIGGER = 30;
    private static final int DEFAULT_ANIMATION_DURATION = 250;
    private static final int MAX_CLICK_TIME = 300;
    private static final boolean DEFAULT_FADE = true;
    private static final boolean DEFAULT_BOUNDARY = true;
    private static final boolean DEFAULT_HIDE_PANEL_TITLE = false;

    private static float MAX_CLICK_DISTANCE = 5;

    private int mChildCount;
    private float mDensity;
    private boolean isAnimating = false;
    private boolean isPanelShowing = false;

    private float xVelocity;
    private float yVelocity;
    private float mTouchSlop;
    private int mMaxVelocity;
    private int mMinVelocity;
    private VelocityTracker mVelocityTracker;

    private int mMeasureHeight;
    private float firstDownX;
    private float firstDownY;
    private float downY;
    private float deltaY;
    private long mPressStartTime;
    private boolean isDragging = false;

    private int mBackgroundId;
    private float mPanelHeight;
    private float mTitleHeightNoDisplay;
    private float mMoveDistanceToTrigger;
    private int mAnimationDuration;
    private boolean mIsFade = true;
    private boolean mBoundary = true;
    private boolean mHidePanelTitle = false;
    private boolean isPanelOnTouch = false;
    private int[] firstTopViewLocations = new int[2];

    private Interpolator mOpenAnimationInterpolator = new AccelerateInterpolator();
    private Interpolator mCloseAnimationInterpolator = new AccelerateInterpolator();

    private Context mContext;
    private DarkFrameLayout mDarkFrameLayout;
    private ProductDetailScrollView1 scrollView1;
    private RelativeLayout upLayout;

    public SlideBottomPanel(Context context) {
        this(context, null);
    }

    public SlideBottomPanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public void setPanelHeight(float height){
    	mPanelHeight = height;
    }

    public SlideBottomPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        mDensity = getResources().getDisplayMetrics().density;

        ViewConfiguration vc = ViewConfiguration.get(mContext);
        mMaxVelocity = vc.getScaledMaximumFlingVelocity();
        mMinVelocity = vc.getScaledMinimumFlingVelocity();
        mTouchSlop = vc.getScaledTouchSlop();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlideBottomPanel, defStyleAttr, 0);

        mBackgroundId = a.getResourceId(R.styleable.SlideBottomPanel_sbp_background_layout, DEFAULT_BACKGROUND_ID);
//        mPanelHeight = a.getDimension(R.styleable.SlideBottomPanel_sbp_panel_height, dp2px(DEFAULT_PANEL_HEIGHT));
        mBoundary = a.getBoolean(R.styleable.SlideBottomPanel_sbp_boundary, DEFAULT_BOUNDARY);
        MAX_CLICK_DISTANCE = mTitleHeightNoDisplay = a.getDimension(R.styleable.SlideBottomPanel_sbp_title_height_no_display,dp2px(DEFAULT_TITLE_HEIGHT_NO_DISPLAY));
        mMoveDistanceToTrigger = a.getDimension(R.styleable.SlideBottomPanel_sbp_move_distance_trigger, dp2px(DEFAULT_MOVE_DISTANCE_TO_TRIGGER));
        mAnimationDuration = a.getInt(R.styleable.SlideBottomPanel_sbp_animation_duration, DEFAULT_ANIMATION_DURATION);
        mHidePanelTitle = a.getBoolean(R.styleable.SlideBottomPanel_sbp_hide_panel_title, DEFAULT_HIDE_PANEL_TITLE);
        mIsFade = a.getBoolean(R.styleable.SlideBottomPanel_sbp_fade, DEFAULT_FADE);

        a.recycle();

        initBackgroundView();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mChildCount = getChildCount();
        int t = (int) (mMeasureHeight - mTitleHeightNoDisplay);
        for (int i = 0; i < mChildCount; i++) {
            View childView = getChildAt(i);
            if(i == 1){
            	RelativeLayout secView = (RelativeLayout)childView;
            	upLayout = (RelativeLayout)secView.getChildAt(0);
            	upLayout.getChildAt(1).getLocationInWindow(firstTopViewLocations);
            	scrollView1 = 	(ProductDetailScrollView1)((FrameLayout)secView.getChildAt(1)).getChildAt(0);
//            	((FrameLayout)secView.getChildAt(1)).getChildAt(1).setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						hidePanel();
//					}
//				});
            }
            if (childView.getTag() == null || (Integer) childView.getTag() != TAG_BACKGROUND) {
                childView.layout(0, t, childView.getMeasuredWidth(), childView.getMeasuredHeight() + t);
                childView.setTag(TAG_PANEL);
            } else if ( (Integer) childView.getTag() == TAG_BACKGROUND){
                childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
                childView.setPadding(0, 0, 0,0);
            }
        }
    }

    public float getDownChildMaxY(){
    	return mMeasureHeight - mTitleHeightNoDisplay;
    }
    
    public float getDownChildMinY(){
    	return mMeasureHeight - mPanelHeight;
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isDragging;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasureHeight = getMeasuredHeight();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        initVelocityTracker(ev);
        boolean isConsume = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isConsume = handleActionDown(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                handleActionMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                handleActionUp(ev);
                releaseVelocityTracker();
                break;
        }
        return isConsume || super.dispatchTouchEvent(ev);
    }

    private void initBackgroundView() {
        if (mBackgroundId != -1) {
            mDarkFrameLayout = new DarkFrameLayout(mContext);
            mDarkFrameLayout.addView(LayoutInflater.from(mContext).inflate(mBackgroundId, null));
            mDarkFrameLayout.setTag(TAG_BACKGROUND);
            mDarkFrameLayout.setSlideBottomPanel(this);
            addView(mDarkFrameLayout);
        }
    }

    private void handleActionUp(MotionEvent event) {
        if (!isPanelOnTouch) {
            return;
        }
        long pressDuration = System.currentTimeMillis() - mPressStartTime;
        computeVelocity();
        if (!isPanelShowing && ((event.getY() - firstDownY) < 0 && (Math.abs(event.getY() - firstDownY) > mMoveDistanceToTrigger))
                    || (yVelocity < 0 && Math.abs(yVelocity) > Math.abs(xVelocity) && Math.abs(yVelocity) > mMinVelocity)) {
            displayPanel();
        } else if (!isPanelShowing && pressDuration < MAX_CLICK_TIME &&
                distance(firstDownX, firstDownY, event.getX(), event.getY()) < MAX_CLICK_DISTANCE) {
            displayPanel();
        } else if (!isPanelShowing && isDragging && ((event.getY() - firstDownY > 0) ||
                Math.abs(event.getY() - firstDownY) < mMoveDistanceToTrigger)){
            hidePanel();
        } 

        if (isPanelShowing) {
            View mPanel = findViewWithTag(TAG_PANEL);
            float currentY = ViewHelper.getY(mPanel);
            if (currentY < (mMeasureHeight - mPanelHeight) ||
                    currentY < (mMeasureHeight - mPanelHeight + mMoveDistanceToTrigger)) {
                ObjectAnimator.ofFloat(mPanel, "y", currentY, mMeasureHeight - mPanelHeight)
                        .setDuration(mAnimationDuration).start();
            } else if (currentY > mMeasureHeight - mPanelHeight + mMoveDistanceToTrigger&&scrollView1.getScrollY() == 0){
                hidePanel();
            }
        }

        isPanelOnTouch = false;
        isDragging = false;
        deltaY = 0;
    }

    private void handleActionMove(MotionEvent event) {
        if (!isPanelOnTouch) {
            return;
        }
        if (isPanelShowing && supportScrollInView((int) (firstDownY - event.getY()))) {
            return;
        }
        computeVelocity();
        if (Math.abs(xVelocity) > Math.abs(yVelocity)) {
            return;
        }
        if (!isDragging && Math.abs(event.getY() - firstDownY) > mTouchSlop
                && Math.abs(event.getX() - firstDownX) < mTouchSlop) {
            isDragging = true;
            downY = event.getY();
        }
        if (isDragging) {
            deltaY = event.getY() - downY;
            downY = event.getY();

            View touchingView = findViewWithTag(TAG_PANEL);
//            hidePanelTitle(touchingView);

            if (mDarkFrameLayout != null && mIsFade) {
                float currentY = ViewHelper.getY(touchingView);
                if (currentY > mMeasureHeight - mPanelHeight &&
                        currentY < mMeasureHeight - mTitleHeightNoDisplay){
//                    mDarkFrameLayout.fade(
//                            (int)((1 - currentY / (mMeasureHeight - mTitleHeightNoDisplay)) * DarkFrameLayout.MAX_ALPHA));
                }
            }
            if (!mBoundary) {
                touchingView.offsetTopAndBottom((int)deltaY);
            } else {
                float touchingViewY = ViewHelper.getY(touchingView);
                if (touchingViewY + deltaY <= mMeasureHeight - mPanelHeight) {
                    touchingView.offsetTopAndBottom((int) (mMeasureHeight - mPanelHeight - touchingViewY));
                } else if (touchingViewY + deltaY >= mMeasureHeight - mTitleHeightNoDisplay) {
                    touchingView.offsetTopAndBottom((int) (mMeasureHeight - mTitleHeightNoDisplay - touchingViewY));
                } else {
                	if(!(deltaY<0&&isPanelShowing&&scrollView1.getScrollX()<=0))
                    touchingView.offsetTopAndBottom((int) deltaY);
                }
            }
        }
    }

    private boolean handleActionDown(MotionEvent event) {
        boolean isConsume = false;
        mPressStartTime = System.currentTimeMillis();
        firstDownX = event.getX();
        firstDownY = downY = event.getY();
        if((!isPanelShowing &&downY < mMeasureHeight - DensityUtil.dip2px(mContext, 10)&&( firstDownX <= (firstTopViewLocations[0])|| firstDownX >= (firstTopViewLocations[0]+DensityUtil.dip2px(mContext, 120))))||((upLayout.getVisibility() == View.INVISIBLE)&&!isPanelShowing)){
        	return false;
        }
        System.out.print(22222);
        if (!isPanelShowing && downY > mMeasureHeight - mTitleHeightNoDisplay) {
            isPanelOnTouch = true;
            isConsume = true;
        } else if (isPanelShowing && downY > mMeasureHeight - mPanelHeight) {
            isPanelOnTouch = true;
        } else if (isPanelShowing && downY < mMeasureHeight - mPanelHeight &&scrollView1.getScrollY() == 0) {
          //  hidePanel();
         //   isPanelOnTouch = false;
        }
        return isConsume;
    }

    private void hidePanel() {
        if (isAnimating) {
            return;
        }
        final View mPanel = findViewWithTag(TAG_PANEL);
        final int topBound = getPaddingTop();
        final int t = (int)(mMeasureHeight - mTitleHeightNoDisplay);
        ValueAnimator animator = ValueAnimator.ofFloat(
                ViewHelper.getY(mPanel), mMeasureHeight - mTitleHeightNoDisplay);
        animator.setInterpolator(mCloseAnimationInterpolator);
        animator.setTarget(mPanel);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewHelper.setY(mPanel, value);
                if(value==mMeasureHeight - mTitleHeightNoDisplay)
                {
                	System.out.print(2222);
                }
                if (mDarkFrameLayout != null && mIsFade && value < t) {
                    mDarkFrameLayout.fade(0);
                }
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
                isPanelShowing = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isAnimating = false;
                isPanelShowing = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animator.start();
        //直接显示 Title ，隐藏 Panel 的动画会出现明显的跳动
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showPanelTitle(mPanel);
            }
        }, mAnimationDuration / 5 * 4);
//        showPanelTitle(mPanel);
    }

    public void displayPanel() {
        if (isPanelShowing || isAnimating) {
            return;
        }
        if (mIsFade || mDarkFrameLayout != null) {
            mDarkFrameLayout.fade(false);
        }
        final View mPanel = findViewWithTag(TAG_PANEL);
        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getY(mPanel), mMeasureHeight - mPanelHeight)
                .setDuration(mAnimationDuration);
        animator.setTarget(mPanel);
        animator.setInterpolator(mOpenAnimationInterpolator);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewHelper.setY(mPanel, value);
                if (mDarkFrameLayout != null && mIsFade
                        && mDarkFrameLayout.getCurrentAlpha() != DarkFrameLayout.MAX_ALPHA) {
//                    mDarkFrameLayout.fade(
//                            (int) ((1 - value / (mMeasureHeight - mTitleHeightNoDisplay)) * DarkFrameLayout.MAX_ALPHA));
                }
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isAnimating = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animator.start();
        isPanelShowing = true;
        hidePanelTitle(mPanel);
       
    }

    private void showPanelTitle(View panel) {
    	if(mHidePanelTitle && panel instanceof RelativeLayout){
    		((RelativeLayout) panel).getChildAt(0).setVisibility(View.VISIBLE);
//    		FrameLayout frameLayout = (FrameLayout)((RelativeLayout) panel).getChildAt(1);
//    		frameLayout.getChildAt(1).setVisibility(View.GONE);
    	}
    }

    private void hidePanelTitle(View panel) {
//        if (panel instanceof FrameLayout && mHidePanelTitle) {
//            try {
//                ((FrameLayout) panel).getChildAt(1).setVisibility(View.GONE);
//            } catch (NullPointerException e) {
//                e.printStackTrace();
//            }
//        }
    	if(mHidePanelTitle && panel instanceof RelativeLayout){
    		((RelativeLayout) panel).getChildAt(0).setVisibility(View.GONE);
//    		FrameLayout frameLayout = (FrameLayout)((RelativeLayout) panel).getChildAt(1);
//    		frameLayout.getChildAt(1).setVisibility(View.VISIBLE);
    	}
    }

    public void hide() {
        if(!isPanelShowing) return;
        hidePanel();
    }

    private void computeVelocity() {
        //units是单位表示， 1代表px/毫秒, 1000代表px/秒
        mVelocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
        xVelocity = mVelocityTracker.getXVelocity();
        yVelocity = mVelocityTracker.getYVelocity();
    }

    @SuppressLint("NewApi") private boolean supportScrollInView(int direction) {

        View view = findViewWithTag(TAG_PANEL);
        if (view instanceof ViewGroup) {
            View childView = findTopChildUnder((ViewGroup) view, firstDownX, firstDownY);
            if (childView == null) {
                return false;
            }
            if (childView instanceof AbsListView) {
                AbsListView absListView = (AbsListView) childView;
                if (Build.VERSION.SDK_INT >= 19) {
                    return absListView.canScrollList(direction);
                } else {
                    return absListViewCanScrollList(absListView,direction);
                }
            } else if (childView instanceof ScrollView) {
                ScrollView scrollView = (ScrollView) childView;
                if (Build.VERSION.SDK_INT >= 14) {
                    return scrollView.canScrollVertically(direction);
                } else {
                    return scrollViewCanScrollVertically(scrollView, direction);
                }

            } else if (childView instanceof ViewGroup){
                View grandchildView = findTopChildUnder((ViewGroup) childView, firstDownX, firstDownY);
                if (grandchildView == null) {
                    return false;
                }
                if (grandchildView instanceof ViewGroup) {
                    if (grandchildView instanceof AbsListView) {
                        AbsListView absListView = (AbsListView) grandchildView;
                        if (Build.VERSION.SDK_INT >= 19) {
                            return absListView.canScrollList(direction);
                        } else {
                            return absListViewCanScrollList(absListView,direction);
                        }
                    } else if (grandchildView instanceof ScrollView) {
                        ScrollView scrollView = (ScrollView) grandchildView;
                        if (Build.VERSION.SDK_INT >= 14) {
                            return scrollView.canScrollVertically(direction);
                        } else {
                            return scrollViewCanScrollVertically(scrollView, direction);
                        }
                    }
                }
            }


        }
        return false;
    }

    private View findTopChildUnder(ViewGroup parentView, float x, float y) {
        int childCount = parentView.getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            final View child = parentView.getChildAt(i);
            if (x >= child.getLeft() && x < child.getRight() &&
                    y >= child.getTop() + mMeasureHeight - mPanelHeight &&
                    y < child.getBottom()  + mMeasureHeight - mPanelHeight) {
                return child;
            }
        }
        return null;
    }

    /**
     *  Copy From ScrollView (API Level >= 14)
     * @param direction Negative to check scrolling up, positive to check
     *                  scrolling down.
     *   @return true if the scrollView can be scrolled in the specified direction,
     *         false otherwise
     */
    private  boolean scrollViewCanScrollVertically(ScrollView scrollView,int direction) {
        final int offset = Math.max(0, scrollView.getScrollY());
        final int range = computeVerticalScrollRange(scrollView) - scrollView.getHeight();
        if (range == 0) return false;
        if (direction < 0) { //scroll up
            return offset > 0;
        } else {//scroll down
            return offset < range - 1;
        }
    }

    /**
     * Copy From ScrollView (API Level >= 14)
     * <p>The scroll range of a scroll view is the overall height of all of its
     * children.</p>
     */
    private int computeVerticalScrollRange(ScrollView scrollView) {
        final int count = scrollView.getChildCount();
        final int contentHeight = scrollView.getHeight() - scrollView.getPaddingBottom() - scrollView.getPaddingTop();
        if (count == 0) {
            return contentHeight;
        }

        int scrollRange = scrollView.getChildAt(0).getBottom();
        final int scrollY = scrollView.getScrollY();
        final int overScrollBottom = Math.max(0, scrollRange - contentHeight);
        if (scrollY < 0) {
            scrollRange -= scrollY;
        } else if (scrollY > overScrollBottom) {
            scrollRange += scrollY - overScrollBottom;
        }

        return scrollRange;
    }

    /**
     * Copy From AbsListView (API Level >= 19)
     * @param absListView AbsListView
     * @param direction Negative to check scrolling up, positive to check
     *                  scrolling down.
     * @return true if the list can be scrolled in the specified direction,
     *         false otherwise
     */
    private boolean absListViewCanScrollList(AbsListView absListView,int direction) {
        final int childCount = absListView.getChildCount();
        if (childCount == 0) {
            return false;
        }
        final int firstPosition = absListView.getFirstVisiblePosition();
        if (direction > 0) {//can scroll down
            final int lastBottom = absListView.getChildAt(childCount - 1).getBottom();
            final int lastPosition = firstPosition + childCount;
            return lastPosition < absListView.getCount() || lastBottom > absListView.getHeight() - absListView.getPaddingTop();
        } else {//can scroll  up
            final int firstTop = absListView.getChildAt(0).getTop();
            return firstPosition > 0 || firstTop < absListView.getPaddingTop();
        }
    }

    private void initVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    private void releaseVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private double distance(float x1, float y1, float x2, float y2) {
        float deltaX = x1 - x2;
        float deltaY = y1 - y2;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

//    private int px2dp(int pxValue) {
//        return (int) (pxValue / mDensity + 0.5f);
//    }

    private int dp2px(int dpValue) {
        return (int) (dpValue * mDensity + 0.5f);
    }

    public boolean isPanelShowing() {
        return isPanelShowing;
    }
}