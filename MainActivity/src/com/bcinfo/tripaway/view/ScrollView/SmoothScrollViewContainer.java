package com.bcinfo.tripaway.view.ScrollView;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

/**
 * 自定义水平滚动条 实现了整屏滑动的效果
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年3月26日 20:31:22
 * 
 */
public class SmoothScrollViewContainer extends HorizontalScrollView
{
    private int subChildCount = 0; // 自定义ScrollView中的子View的数量
    private ViewGroup firstChild = null;// 外层的LinearLayout容器
    private int downX = 0;// 手指按下时的x坐标
    private int currentPage = 0;// 当前的页码
    private ArrayList<Integer> pointList = new ArrayList<Integer>();
    private int touchSlopDistance;

    public SmoothScrollViewContainer(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public SmoothScrollViewContainer(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public SmoothScrollViewContainer(Context context)
    {
        super(context);
        init();
    }

    private void init()
    {
        setHorizontalScrollBarEnabled(false);// 设置水平的scrollBar不可用
        ViewConfiguration viewConfig = ViewConfiguration.get(getContext());
        touchSlopDistance = viewConfig.getScaledTouchSlop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        receiveChildInfo();

    }

    public void receiveChildInfo()
    {

        firstChild = (ViewGroup) getChildAt(0);
        if (firstChild != null)
        {
            subChildCount = firstChild.getChildCount();

            for (int i = 0; i < subChildCount; i++)
            {
                if (firstChild.getChildAt(i).getWidth() > 0)
                {

                    pointList.add(firstChild.getChildAt(i).getLeft());
                }

            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        switch (ev.getAction())
        {
        case MotionEvent.ACTION_DOWN:
            downX = (int) ev.getX();
            break;
        case MotionEvent.ACTION_MOVE:
        {

        }
            break;
        case MotionEvent.ACTION_UP:

        case MotionEvent.ACTION_CANCEL:
        {
//            Toast.makeText(getContext(), "手指移动的距离:" + Math.abs((ev.getX() - downX)), 0).show();
            if (Math.abs((ev.getX() - downX)) > getWidth() / 2 && Math.abs((ev.getX() - downX)) > touchSlopDistance)
            {

                if (ev.getX() - downX > 0)
                {
//                    Toast.makeText(getContext(), "滑到前一页", 0).show();
                    smoothScrollToPrePage();
                }
                else
                {
//                    Toast.makeText(getContext(), "滑到后一页", 0).show();
                    smoothScrollToNextPage();
                }
            }
            else
            {
                smoothScrollToCurrent();
//                Toast.makeText(getContext(), "滑到当前页", 0).show();
            }
            Toast.makeText(getContext(), currentPage+"", 0).show();
            return true;
        }
        }
        return super.onTouchEvent(ev);
    }

    private void smoothScrollToCurrent()
    {
        smoothScrollTo(pointList.get(currentPage), 0);
    }

    private void smoothScrollToNextPage()
    {
        if (currentPage < subChildCount - 1)
        {
            currentPage++;
            smoothScrollTo(pointList.get(currentPage), 0);
        }
    }

    private void smoothScrollToPrePage()
    {
        if (currentPage > 0)
        {
            currentPage--;
            smoothScrollTo(pointList.get(currentPage), 0);
        }
    }

    /**
     * ��һҳ
     */
    public void nextPage()
    {
        smoothScrollToNextPage();
    }

    /**
     * ��һҳ
     */
    public void prePage()
    {
        smoothScrollToPrePage();
    }

    /**
     * ��ת��ָ����ҳ��
     * 
     * @param page
     * @return
     */
    public boolean gotoPage(int page)
    {
        if (page > 0 && page < subChildCount - 1)
        {
            smoothScrollTo(pointList.get(page), 0);
            currentPage = page;
            return true;
        }
        return false;
    }
}
