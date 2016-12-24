package com.bcinfo.tripaway.view.ScrollView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.listener.PersonalScrollViewListener;
import com.bcinfo.tripaway.listener.Pullable;
import com.bcinfo.tripaway.utils.LogUtil;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

/**
 * 通用带下拉滚动界面
 * @function
 * @author     JiangCS  
 * @version   1.0, 2015年3月23日 下午3:51:25
 */
public class PersonInfoScrollView extends ScrollView implements Pullable
{
    private static final String TAG = "ProductDetailScrollView";
    /**
     * PersonalScrollViewListener接口对象
     * 
     */
    private PersonalScrollViewListener mScrollListener;
    private View inner;// 孩子View
    private float touchY;// 点击时Y坐标
    private float deltaY;// Y轴滑动的距离
    private float initTouchY;// 首次点击的Y坐标
    private boolean shutTouch = false;// 是否关闭ScrollView的滑动.
    private Rect normal = new Rect();// 矩形(这里只是个形式，只是用于判断是否需要动画.)
    private boolean isMoveing = false;// 是否开始移动.
    private ImageView imageView;// 背景图控件.
    private int initTop, initBottom;// 初始高度
    private int current_Top, current_Bottom;// 拖动时时高度。
    private int current_img_move = 0;//当前inner移动距离
    private int current_inner_move = 0;//当前inner移动距离
    private int img_move_d = 0;//恢复时图片一次移动距离
    private int inner_move_d = 0;//恢复时inner一次移动距离
    private PullListener mPullListener;
    
    private boolean isBacking=false;
    // 状态：上部，下部，默认
    private enum State
    {
        UP, DOWN, NOMAL
    };
    // 默认状态
    private State state = State.NOMAL;

    public PersonalScrollViewListener getScrollListener()
    {
        return mScrollListener;
    }

    public void setScrollListener(PersonalScrollViewListener scrollListener)
    {
        this.mScrollListener = scrollListener;
    }

    public PersonInfoScrollView(Context context)
    {
        super(context);
    }

    public PersonInfoScrollView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public PersonInfoScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int positionX, int positionY, int prePositionX, int prePositionY)
    {
        super.onScrollChanged(positionX, positionY, prePositionX, prePositionY);
        // 当自定义ScrollView控件滚动时自动调用onScrollChanged()方法 这时将方法的执行交给外部来完成
        if (mScrollListener != null)
        {
            // 判断接口对象是否为空，若不为空，那么调用onScrollChanged()方法
            mScrollListener.onScrollChanged(this, positionX, positionY, prePositionX, prePositionY);
        }
        else
        {
            return;
        }
    }

    public void setPullListener(PullListener turnListener)
    {
        this.mPullListener = turnListener;
    }

    // 注入背景图
    public void setImageView(ImageView imageView)
    {
        this.imageView = imageView;
    }

    /***
     * 根据 XML 生成视图工作完成.该函数在生成视图的最后调用，在所有子视图添加完之后. 即使子类覆盖了 onFinishInflate
     * 方法，也应该调用父类的方法，使该方法得以执行.
     */
    @Override
    protected void onFinishInflate()
    {
        if (getChildCount() > 0)
        {
            inner = getChildAt(0).findViewById(R.id.product_layout_container);
        }
    }

    /** touch 事件处理 **/
    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        if (inner != null)
        {
            commOnTouchEvent(ev);
        }
        // ture：禁止控件本身的滑动.
        if (shutTouch)
            return true;
        else
            return super.onTouchEvent(ev);
    }

    /***
     * 触摸事件
     * 
     * @param ev
     */
    public void commOnTouchEvent(MotionEvent ev)
    {
        int action = ev.getAction();
        if (0 == initTouchY)
        {
            initTouchY = ev.getY();
        }
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
            	if(isBacking)return;
                mHandler.removeMessages(1);
                break;
            case MotionEvent.ACTION_UP:
            	if(isBacking)return;
                /** 回缩动画 **/
                if (isNeedAnimation())
                {
                    img_move_d = (initTop - current_Top) / 15;
                    inner_move_d = current_inner_move / 15;
                    mHandler.sendEmptyMessageDelayed(1, 10);
                }
                if (getScrollY() == 0)
                {
                    state = State.NOMAL;
                }
                isMoveing = false;
                initTouchY = 0;
                touchY = 0;
                shutTouch = false;
                break;
            /***
             * 排除出第一次移动计算，因为第一次无法得知deltaY的高度， 然而我们也要进行初始化，就是第一次移动的时候让滑动距离归0.
             * 之后记录准确了就正常执行.
             */
            case MotionEvent.ACTION_MOVE:
            	if(isBacking)return;
                touchY = ev.getY();
                deltaY = touchY - initTouchY;// 滑动距离
                /** 对于首次Touch操作要判断方位：UP OR DOWN **/
                if (deltaY < 0 && state == State.NOMAL)
                {
                    state = State.UP;
                }
                else if (deltaY > 0 && state == State.NOMAL)
                {
                    state = State.DOWN;
                }
                if (state == State.UP)
                {
                    deltaY = deltaY < 0 ? deltaY : 0;
                    isMoveing = false;
                    shutTouch = false;
                }
                else if (state == State.DOWN)
                {
                    if (getScrollY() <= deltaY)
                    {
                        shutTouch = true;
                        isMoveing = true;
                    }
                    deltaY = deltaY < 0 ? 0 : deltaY;
                }
                if (isMoveing)
                {
                    // 初始化头部矩形
                    if (normal.isEmpty())
                    {
                        // 保存正常的布局位置
                        normal.set(inner.getLeft(), inner.getTop(), inner.getRight(), inner.getBottom());
                        current_Top = initTop = imageView.getTop();
                        current_Bottom = initBottom = imageView.getBottom();
                    }
                    // 移动布局(手势移动的1/3)
                    float inner_move_H = deltaY / 4;
                    current_inner_move = (int) inner_move_H;
                    //                    LogUtil.i(TAG, "ACTION_MOVE", "inner_move_H=" + inner_move_H);
                    inner.layout(normal.left, (int) (normal.top + inner_move_H), normal.right,
                            (int) (normal.bottom + inner_move_H));
                    /** image_bg **/
                    float image_move_H = deltaY / 4;
                    current_img_move = (int) image_move_H;
                    current_Top = (int) (initTop - image_move_H);
                    current_Bottom = (int) (initBottom + image_move_H);
                    imageView.layout(imageView.getLeft(), initTop, imageView.getRight(), current_Bottom);
                    RelativeLayout ls=(RelativeLayout)imageView.getParent();
                    if(ls!=null&&ls.getChildAt(1)!=null&&ls.getChildAt(1)!=inner){
                    	View view=ls.getChildAt(1);
                    	view.layout(imageView.getLeft(), initTop, imageView.getRight(), current_Bottom);
                    }
                    mPullListener.onPull(current_inner_move);
                }
                break;
            default:
                break;
        }
    }
    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            // TODO Auto-generated method stub
            switch (msg.what)
            {
                case 1:
                    current_img_move = current_img_move - img_move_d;
                    current_inner_move = current_inner_move - inner_move_d;
                    if (current_img_move > 0 && current_inner_move > 0)
                    {
                    	isBacking=true;
                        imageView.layout(imageView.getLeft(), initTop , imageView.getRight(),
                                initBottom + current_img_move);
                        inner.layout(normal.left, normal.top + current_inner_move, normal.right, normal.bottom
                                + current_inner_move);
                        RelativeLayout ls=(RelativeLayout)imageView.getParent();
                        if(ls!=null&&ls.getChildAt(1)!=null&&ls.getChildAt(1)!=inner){
                        	View view=ls.getChildAt(1);
                        	view.layout(imageView.getLeft(), initTop , imageView.getRight(),
                                    initBottom + current_img_move);
                        }
                        mHandler.sendEmptyMessageDelayed(1, 10);
                    }
                    else
                    {
                        imageView.layout(imageView.getLeft(), (int) initTop, imageView.getRight(), (int) initBottom);
                        inner.layout(normal.left, normal.top, normal.right, normal.bottom);
                        normal.setEmpty();
                        RelativeLayout ls=(RelativeLayout)imageView.getParent();
                        if(ls!=null&&ls.getChildAt(1)!=null&&ls.getChildAt(1)!=inner){
                        	View view=ls.getChildAt(1);
                        	view.layout(imageView.getLeft(), (int) initTop, imageView.getRight(), (int) initBottom);
                        }
                        isBacking=false;
                    }
                    mPullListener.onPull(current_inner_move);
                    break;
                case 2:
                    break;
                default:
                    break;
            }
        }
    };

    /** 是否需要开启动画 **/
    public boolean isNeedAnimation()
    {
        return !normal.isEmpty();
    }
    /***
     * 执行翻转
     * 
     * @author jia
     * 
     */
    public interface PullListener
    {
        /** 必须达到一定程度才执行 **/
        void onPull(int distance);
    }
	@Override
    public boolean canPullDown()
    {
        if (getScrollY() == 0)
            return true;
        else
            return false;
    }
    
    @Override
    public boolean canPullUp()
    {
        if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
            return true;
        else
            return false;
    }
}
