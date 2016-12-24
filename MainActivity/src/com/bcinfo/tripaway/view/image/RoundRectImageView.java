package com.bcinfo.tripaway.view.image;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 自定义圆角边框ImageView
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月16日 17:55:11
 */
public class RoundRectImageView extends ImageView
{

    public RoundRectImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

    }

    public RoundRectImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

    }

    public RoundRectImageView(Context context)
    {
        super(context);

    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        Path clipPath = new Path();
        int w = this.getWidth();
        int h = this.getHeight();
        clipPath.addRoundRect(new RectF(0, 0, w, h), 10.0f, 10.0f, Path.Direction.CW);
        canvas.clipPath(clipPath);
        super.onDraw(canvas);

    }
}
