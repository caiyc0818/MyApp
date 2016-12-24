package com.bcinfo.tripaway.view.text;

import com.bcinfo.tripaway.view.span.TextForeGroundColorSpan;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.TextView;

public class LinkArrowKeyMovementMethod extends ArrowKeyMovementMethod {
	private static LinkArrowKeyMovementMethod mInstance;
	public int current=-1;
	@Override
	public boolean onTouchEvent(TextView widget, Spannable buffer,
			MotionEvent event) {
		int action = event.getAction();
		if (action == MotionEvent.ACTION_UP
				|| action == MotionEvent.ACTION_DOWN) {
			int x = (int) event.getX();
			int y = (int) event.getY();
			x -= widget.getTotalPaddingLeft();
			y -= widget.getTotalPaddingTop();
			x += widget.getScrollX();
			y += widget.getScrollY();
			Layout layout = widget.getLayout();
			int line = layout.getLineForVertical(y);
			int off = layout.getOffsetForHorizontal(line, x);
			ClickableSpan[] link = buffer.getSpans(off, off,
					ClickableSpan.class);
			TextForeGroundColorSpan[] textForeGroundColorSpan = buffer.getSpans(off, off,
					TextForeGroundColorSpan.class);
			if (link.length != 0) {
				if (action == MotionEvent.ACTION_UP&&x<layout.getLineWidth(line)&&x>0) {
					link[0].onClick(widget);
				} 
				return true;
			}if (textForeGroundColorSpan.length != 0) {
				if (action == MotionEvent.ACTION_UP&&x<layout.getLineWidth(line)&&x>0&&widget instanceof EditText) {
//					Selection.setSelection(buffer,
//							buffer.getSpanEnd(textForeGroundColorSpan[0]));
					int i=((EditText)widget).getSelectionStart();
					((EditText)widget).setSelection(buffer.getSpanEnd(textForeGroundColorSpan[0]));
					int j=((EditText)widget).getSelectionStart();
					System.out.print(222222);
				}
				return true;
			}  else {
				Selection.removeSelection(buffer);
			}
		}
		return super.onTouchEvent(widget, buffer, event);
	}

	
	public void onClick(TextView widget, Spannable buffer,
			MotionEvent event) {
		int action = event.getAction();
		if (action == MotionEvent.ACTION_UP) {
			int x = (int) event.getX();
			int y = (int) event.getY();
			x -= widget.getTotalPaddingLeft();
			y -= widget.getTotalPaddingTop();
			x += widget.getScrollX();
			y += widget.getScrollY();
			Layout layout = widget.getLayout();
			int line = layout.getLineForVertical(y);
			int off = layout.getOffsetForHorizontal(line, x);
			ClickableSpan[] link = buffer.getSpans(off, off,
					ClickableSpan.class);
			TextForeGroundColorSpan[] textForeGroundColorSpan = buffer.getSpans(off, off,
					TextForeGroundColorSpan.class);
			if (link.length != 0) {
				if (action == MotionEvent.ACTION_UP&&x<layout.getLineWidth(line)&&x>0) {
					((EditText)widget).setSelection(buffer.getSpanEnd(link[0]));
				} 
				return ;
			}if (textForeGroundColorSpan.length != 0) {
				if (action == MotionEvent.ACTION_UP&&x<layout.getLineWidth(line)&&x>0) {
					if(x<line/2)
						((EditText)widget).setSelection(buffer.getSpanStart(textForeGroundColorSpan[0]));
					else
					((EditText)widget).setSelection(buffer.getSpanEnd(textForeGroundColorSpan[0]));
				}
				return ;
			}  else {
//				Selection.removeSelection(buffer);
			}
		}
	}
	
	@Override
	public String toString() {
		return "my.handrite.text.method.ClickableArrowKeyMovementMethod";
	}

	public static MovementMethod getInstance() {
		if (mInstance == null)
			mInstance = new LinkArrowKeyMovementMethod();
		return mInstance;
	}
}
