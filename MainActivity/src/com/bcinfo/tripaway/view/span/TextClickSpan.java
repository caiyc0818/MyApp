package com.bcinfo.tripaway.view.span;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bcinfo.tripaway.activity.SquareMusicOrBookOrMusicDetailActivity;
import com.bcinfo.tripaway.activity.SquarePicPublishedActivity;
import com.bcinfo.tripaway.activity.SquareSerialPublishedActivity;
import com.bcinfo.tripaway.bean.RichText.Type;
import com.bcinfo.tripaway.view.emoji.EmojiconSpan;
import com.bcinfo.tripaway.view.text.NoLineClickSpan;

public class TextClickSpan extends NoLineClickSpan{

	private Type type;
	private String uri;
	private Map<String, String> attrs=new HashMap<String, String>();
	public TextClickSpan(int linkColor) {
		super(linkColor);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onClick(View widget) {
		// TODO Auto-generated method stub
		Intent intent=new Intent(widget.getContext(),SquareMusicOrBookOrMusicDetailActivity.class);
		intent.putExtra("name1",attrs.get("name1"));
		intent.putExtra("name2",attrs.get("name2"));
		intent.putExtra("desc",attrs.get("desc"));
		if(attrs.containsKey("img")){
			intent.putExtra("img",attrs.get("img"));
		}
		intent.putExtra("uri",uri);
		if(type==Type.MUSIC){
			intent.putExtra("type",1);
		}else 
			if(type==Type.BOOK){
				intent.putExtra("type",2);
			}
			else if(type==Type.FILM){
				intent.putExtra("type",3);	
			}
		Editable editable=((TextView)widget).getEditableText();
		if(editable!=null){
			int start=editable.getSpanStart(this);
			int end=editable.getSpanEnd(this);
			if(widget instanceof EditText)
			((EditText)widget).setSelection(end);
			if(start-2>=0){
				EmojiconSpan[] emojiconSpans = editable.getSpans(start-2, start,
						EmojiconSpan.class);
				if(emojiconSpans!=null&&emojiconSpans.length==2){
					start=start-2;
				}
			}
			if(widget.getContext() instanceof SquarePicPublishedActivity){
				SquarePicPublishedActivity activity=(SquarePicPublishedActivity)widget.getContext();
				activity.clickSpanStart=start;
				activity.clickSpanEnd=end;
			}else if(widget.getContext() instanceof SquareSerialPublishedActivity){
				SquareSerialPublishedActivity activity=(SquareSerialPublishedActivity)widget.getContext();
				activity.clickSpanStart=start;
				activity.clickSpanEnd=end;
			}else{
				intent.putExtra("isEdit",false);
			}
		}
		((Activity)widget.getContext()).startActivityForResult(intent,1003);
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Map<String, String> getAttrs() {
		return attrs;
	}

	public void setAttrs(Map<String, String> attrs) {
		this.attrs = attrs;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

}
