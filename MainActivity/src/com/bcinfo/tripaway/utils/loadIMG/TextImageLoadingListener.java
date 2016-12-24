package com.bcinfo.tripaway.utils.loadIMG;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bcinfo.tripaway.TripawayApplication;
import com.bcinfo.tripaway.utils.BitmapUtil;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.TextStyleUtil;
import com.bcinfo.tripaway.view.emoji.EmojiconSpan;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class TextImageLoadingListener implements ImageLoadingListener {

	private int start;
	private TextView tv;

	private int w;

	public TextImageLoadingListener(int start, TextView tv, int w) {
		// TODO Auto-generated constructor stub
		this.start = start;
		this.tv = tv;
		this.w = w;
	}

	@Override
	public void onLoadingStarted(String imageUri, View view) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onLoadingFailed(String imageUri, View view,
			FailReason failReason) {
		// TODO Auto-generated method stub
		Log.e("fail", imageUri+" "+imageUri);
	}

	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		// TODO Auto-generated method stub
		Log.e("sucess", tv.getText()+" "+imageUri+" "+start);
		String uri = (String) tv.getTag();
//		if (!StringUtils.isEmpty(uri) && imageUri.endsWith(uri)) {
			Editable editable = tv.getEditableText();
			boolean isCheck=false;
//			if(editable!=null){
//			EmojiconSpan[] emojiconSpans = editable.getSpans(start, start+1,
//					EmojiconSpan.class);
//			if(emojiconSpans!=null&&emojiconSpans.length==1){
//				if(emojiconSpans[0].getUri().equals(uri)){
//					isCheck=true;
//				}
//			}
//			}
//			if (!isCheck) {
				Drawable db = BitmapUtil.scaleSqureImage(loadedImage, w, w);
				TextStyleUtil.insertImageSpan(tv, " ", db, uri, start);
//			}
//		}
	}

	@Override
	public void onLoadingCancelled(String imageUri, View view) {
		// TODO Auto-generated method stub
		Log.e("cancel", tv.getText()+" "+imageUri+" "+imageUri);
	}

}
