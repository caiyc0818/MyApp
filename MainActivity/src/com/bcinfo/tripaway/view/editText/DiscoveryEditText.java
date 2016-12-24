package com.bcinfo.tripaway.view.editText;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.utils.DensityUtil;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

/**
 * 自定义发现-搜索输入框
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年2月5日 9:55:56
 */
public class DiscoveryEditText extends EditText implements OnFocusChangeListener
{
    // 定义一个drawable对象 用于显示在EditText的右边
    public Drawable mdrawable;

    // 静态变量TEMP 表示系统可接受的触摸容差
    private static int TEMP=10;
    // 定义boolean型变量 用于判断当前自定义的EditText是否获得了焦点 默认为false

    public boolean hasFocus;

    public boolean isHasFocus()
    {

        return hasFocus;
    }

    public void setHasFocus(boolean hasFocus)
    {

        this.hasFocus = hasFocus;
    }

    public DiscoveryEditText(Context context)
    {

        this(context, null);
    }

    // 最终调用的是含有三个参数的构造方法

    public DiscoveryEditText(Context context, AttributeSet attrs, int defStyle)
    {

        super(context, attrs, defStyle);
        init();
    }

    public DiscoveryEditText(Context context, AttributeSet attrs)
    {

        this(context, attrs, android.R.attr.editTextStyle);

    }

    // init()方法用于初始化自定义的EditText控件
    public void init()
    {

        mdrawable = this.getCompoundDrawables()[2]; // 获得editText控件右边的drawable对象
        if (mdrawable == null)
        {
            // if 判断 如果mdrawable为null,那么就加载指定id的drawable资源

            mdrawable = this.getContext().getResources().getDrawable(R.drawable.login_delete);

        }

        // 设置mdrawable的bounds参数
        /*
         * Specify a bounding rectangle for the Drawable. This is where the
         * drawable will draw when its draw() method is called.
         * 具体为drawable图片对象指明一个矩形区域，分别设置区域的left,top,right,bottom这四个参数
         */
//        mdrawable.setBounds(-2, 0, 65, 65);
        mdrawable.setBounds(DensityUtil.dip2px(this.getContext(), -10), DensityUtil.dip2px(this.getContext(), 0), DensityUtil.dip2px(this.getContext(), 15), DensityUtil.dip2px(this.getContext(), 25));
        // 初始状态是不可见的
        showDrawableVisible(false);

        // 定义EditText的focus焦点监听事件 当焦点发生改变的时候会回调监听方法

        this.setOnFocusChangeListener(this);

        // 定义EditText的text文本内容改变的监听事件 当editText中的文本内容发生改变的时候会回调监听方法

        // this.addTextChangedListener(this);

    }

    // 定义EditText右边的drawable对象是否可见的方法
    public void showDrawableVisible(boolean isVisible)
    {

        Drawable drawable = null;
        // if判断 isVisible是否为true 如果为true,那么显示右边的删除图标；为false,隐藏右边的删除图标
        if (isVisible)
        {
            drawable = mdrawable;
        }
        // 设置自定义EditText周围的drawable
        this.setCompoundDrawables(this.getCompoundDrawables()[0], this.getCompoundDrawables()[1], drawable,
                this.getCompoundDrawables()[3]);

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {

        this.hasFocus = hasFocus;
        EditText edit1 = (EditText) v;
        // 判断是否有焦点
        if (hasFocus)
        {
            // 继续判断该有焦点的editText中是否有内容 如果有的话，那么显示删除图标；否则，就不显示
            if (TextUtils.isEmpty(edit1.getText().toString().trim()))
            {
                showDrawableVisible(false);
            }
            else
            {

                showDrawableVisible(true);
            }

        }
        else
        {

            showDrawableVisible(false);

        }

    }

    /*
     * @Override public void beforeTextChanged(CharSequence s, int start, int
     * count, int after) {
     * 
     * }
     */
    /*
     * 当文本框的内容输入之后回调afterTextChanged()方法
     */
    /*
     * @Override public void afterTextChanged(Editable s) {
     * 
     * // if判断 是否获得了焦点 if (hasFocus) { // if判断 text文本内容的长度是否大于0 如果大于0
     * 那么就显示右边的删除图标 if (s.length() > 0) { showDrawableVisible(true); } else {
     * showDrawableVisible(false); } } else { showDrawableVisible(false); } }
     * 
     * // 当editText中的文本内容发送改变的时候，回调onTextChanged()方法
     * 
     * @Override public void onTextChanged(CharSequence text, int start, int
     * lengthBefore, int lengthAfter) {
     * 
     * super.onTextChanged(text, start, lengthBefore, lengthAfter);
     * 
     * 
     * }
     */
    // 重写editText的ontouchEvent()触摸监听的方法

    /*
     * 要实现带有删除功能的editText控件 必须要实现下面的ontouchEvent()触摸方法
     * 
     * 以及上面的有关focus,textchange之类的监听事件
     */
    /*
     * 实现触摸touch方法的主要思想是: 1.当手指在editText控件上抬起的时候，要获取手指抬起的那个点的x,y坐标；
     * 2.当x坐标在大于editText的宽度 减去 ( 删除图标的左边缘 离edittext的右边缘的距离 )的范围，并且 小于edittext的宽度
     * 减去 (删除图标的右边缘 离 edittext的右边缘的距离)的范围;这是在x坐标上面的限制条件 ;
     * 
     * 3.y坐标的条件是: 首先需要获得删除图标的上边缘距离edittext的上边缘的距离 distance y坐标的范围需要在distance和
     * distance+删除图标自身高度的范围之内 这样才有效
     * 
     * 4.只有当x坐标和y坐标在上面要求的范围之内的话，那么这次的触摸位置才算得上是真正模仿点击到了删除图标
     */

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        // 首先 if判断 如果edittext右边的删除图标不为null 并且触摸的动作是Action_Up手指抬起的操作 那么进行内部操作
        // 判断触摸点的x,y坐标的位置 来
        // 判断是否是仿点击了删除图标

        if (this.getCompoundDrawables()[2] != null && event.getAction() == MotionEvent.ACTION_UP)
        {
            // 开始判断 触摸点的x,y坐标
            // 手指抬起时的x坐标
            float pointX = event.getX();
            // 手指抬起时的y坐标
            float pointY = event.getY();
            // 定义一个局部变量
            boolean flag = false;
            if ((pointX > (this.getWidth() - this.getTotalPaddingRight()-TEMP)) && (pointX < (getWidth() - getPaddingRight()+TEMP)))
            {
                // 如果x坐标满足这个情况 那么继续判断y坐标的位置
                // getIntrinsicHeight()方法是获得editText右边的drawable对象的真实高度
                int distance = (this.getHeight() - this.getCompoundDrawables()[2].getIntrinsicHeight()) / 2;

                if ((pointY > distance-TEMP) && (pointY < distance + getCompoundDrawables()[2].getIntrinsicHeight()+TEMP))
                {

                    flag = true;

                }

            }
            // 最后判断flag的布尔值 如果flag为true,那么
            if (flag)
            {
                // 如果flag为true的话 那么表示当前手指抬起的位置是触摸到了删除图标 所以此时是模仿相当于点击了删除图标
                // 清除ediText中的文本内容

                // 设置文本为空，会立即重绘这个edittext控件

                this.setText(null);

                // 设置自定义EditText上面的动画效果

                // setDynamic();

            }
        }
        return super.onTouchEvent(event);

    }

}
