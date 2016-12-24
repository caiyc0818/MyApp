package com.bcinfo.tripaway.utils;

import java.util.Map;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.BufferType;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.ContentActivity;
import com.bcinfo.tripaway.activity.RegisterActivity;
import com.bcinfo.tripaway.bean.RichText;
import com.bcinfo.tripaway.bean.RichText.Type;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.view.emoji.EmojiconSpan;
import com.bcinfo.tripaway.view.span.TextClickSpan;
import com.bcinfo.tripaway.view.span.TextForeGroundColorSpan;
import com.bcinfo.tripaway.view.text.LinkArrowKeyMovementMethod;
import com.bcinfo.tripaway.view.text.NoLineClickSpan;


public class TextStyleUtil {

	public static void addImageSpan(TextView tv, String text, int resourceId) {
		SpannableString spanString = new SpannableString(text);
		spanString.setSpan(new EmojiconSpan(tv.getContext(), resourceId,
				(int) tv.getTextSize(), (int) tv.getTextSize()), 0, text
				.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		if(tv instanceof EditText){
			Editable editable=tv.getEditableText();
			EditText tv1=(EditText)tv;
			if(tv1.getSelectionStart()!=-1)
				editable.insert(tv1.getSelectionStart(), spanString);
				else
					tv.append(spanString);
		}else
		tv.append(spanString);
	}

	public static void addImageSpan(TextView tv, String text, int resourceId,
			int size) {
		SpannableString spanString = new SpannableString(text);
		spanString.setSpan(new EmojiconSpan(tv.getContext(), resourceId, size,
				size), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		if(tv instanceof EditText){
			Editable editable=tv.getEditableText();
			EditText tv1=(EditText)tv;
			if(tv1.getSelectionStart()!=-1)
				editable.insert(tv1.getSelectionStart(), spanString);
				else
					tv.append(spanString);
		}else
		tv.append(spanString);
	}

	public static void addImageSpan(TextView tv, String text,
			Drawable drawable, String uri) {
		SpannableString spanString = new SpannableString(text);
		spanString.setSpan(
				new EmojiconSpan(tv.getContext(), drawable, (int) tv
						.getTextSize(), (int) tv.getTextSize(), uri), 0, text
						.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		if(tv instanceof EditText){
			Editable editable=tv.getEditableText();
			EditText tv1=(EditText)tv;
			if(tv1.getSelectionStart()!=-1)
				editable.insert(tv1.getSelectionStart(), spanString);
				else
					tv.append(spanString);
		}else
		tv.append(spanString);
	}
	
	public static void insertImageSpan(TextView tv, String text,
			Drawable drawable, String uri,int start) {
		SpannableString spanString = new SpannableString(text);
		spanString.setSpan(
				new EmojiconSpan(tv.getContext(), drawable, (int) tv
						.getTextSize(), (int) tv.getTextSize(), uri), 0, text
						.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		Editable editable =tv.getEditableText();
		if (!(editable instanceof Editable)) {
			tv.setText(editable, BufferType.EDITABLE);
		}
		if(start<tv.getText().length())
		editable.replace(start, start+1, spanString);
	}

	public static void insertImageSpan(TextView tv, String text,
			Drawable drawable, String uri,int size,int start) {
		SpannableString spanString = new SpannableString(text);
		spanString.setSpan(new EmojiconSpan(tv.getContext(), drawable, size,
				size, uri), 0, text.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		Editable editable =tv.getEditableText();
		if (!(editable instanceof Editable)) {
			tv.setText(editable, BufferType.EDITABLE);
		}
		if(start<tv.getText().length())
//			editable.insert(start, spanString);
		editable.replace(start, start+1, spanString);
	}
	
	
	public static void addImageSpan(TextView tv, String text,
			Drawable drawable, int size, String uri) {
		SpannableString spanString = new SpannableString(text);
		spanString.setSpan(new EmojiconSpan(tv.getContext(), drawable, size,
				size, uri), 0, text.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		if(tv instanceof EditText){
			Editable editable=tv.getEditableText();
			EditText tv1=(EditText)tv;
			if(tv1.getSelectionStart()!=-1)
			editable.insert(tv1.getSelectionStart(), spanString);
			else
				tv.append(spanString);
		}else
		tv.append(spanString);
	}

	public static void addForegroundColorSpan(TextView tv, String text) {
		SpannableString spanString = new SpannableString(text);
		TextForeGroundColorSpan span = new TextForeGroundColorSpan(Color.BLUE);
		if(tv.getTag()!=null){
			span.setTextId(tv.getTag().toString());
		}
		spanString.setSpan(span, 0, text.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		if(tv instanceof EditText){
			Editable editable=tv.getEditableText();
			EditText tv1=(EditText)tv;
			if(tv1.getSelectionStart()!=-1)
				editable.insert(tv1.getSelectionStart(), spanString);
				else
					tv.append(spanString);
		}else
		tv.append(spanString);
	}
	
	public static void addForegroundColorSpan(TextView tv, String text,String id) {
		SpannableString spanString = new SpannableString(text);
		TextForeGroundColorSpan span = new TextForeGroundColorSpan(Color.BLUE);
			span.setTextId(id);
		spanString.setSpan(span, 0, text.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		if(tv instanceof EditText){
			Editable editable=tv.getEditableText();
			EditText tv1=(EditText)tv;
			if(tv1.getSelectionStart()!=-1)
			editable.insert(tv1.getSelectionStart(), spanString);
			else {
				tv.requestFocus();
				tv.requestFocusFromTouch();
				editable.insert(editable.length(), spanString);
			}
		}else
		tv.append(spanString);
	}

	public static void addClickableSpan(final TextView tv, Bundle bundle) {
		String text = bundle.getString("name1");
		if (text == null || text.equals(""))
			return;
		TextClickSpan span = new TextClickSpan(Color.parseColor("#0f81eb"));
		Map<String, String> attrs = span.getAttrs();
		attrs.put("desc", bundle.getString("desc"));
		attrs.put("name1", bundle.getString("name1"));
		attrs.put("name2", bundle.getString("name2"));
		span.setUri(bundle.getString("uri"));
		int type = bundle.getInt("type", 0);
		if (type == 1) {
			span.setType(Type.MUSIC);
		} else if (type == 2) {
			span.setType(Type.BOOK);
		} else if (type == 3) {
			span.setType(Type.FILM);
		}

		SpannableString spanString = new SpannableString(text);
		spanString.setSpan(span, 0, text.length(),
				Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		if(tv instanceof EditText){
			Editable editable=tv.getEditableText();
			EditText tv1=(EditText)tv;
			if(tv1.getSelectionStart()!=-1)
			editable.insert(tv1.getSelectionStart(), spanString);
			else
				tv.append(spanString);
		}else
		tv.append(spanString);
		if(tv.getMovementMethod()==null||!(tv.getMovementMethod() instanceof LinkArrowKeyMovementMethod))
		tv.setMovementMethod(LinkArrowKeyMovementMethod.getInstance());
	}
	
	public static void addClickableSpan(final TextView tv, RichText richText) {
		Map<String, String> attrs = richText.getAttrs();
		String text = richText.getText();
		if (text == null || text.equals(""))
			return;
		TextClickSpan span = new TextClickSpan(Color.parseColor("#0f81eb"));
		attrs.put("name1", richText.getText());
		if(attrs.containsKey("name")){
			attrs.put("name2",attrs.get("name"));
			attrs.remove("name");
		}
		span.setAttrs(attrs);
		span.setUri(richText.getUri());
			span.setType(richText.getType());
		SpannableString spanString = new SpannableString(text);
		spanString.setSpan(span, 0, text.length(),
				Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		if(tv instanceof EditText){
			Editable editable=tv.getEditableText();
			EditText tv1=(EditText)tv;
			if(tv1.getSelectionStart()!=-1)
				editable.insert(tv1.getSelectionStart(), spanString);
				else
					tv.append(spanString);
			
		}else
		tv.append(spanString);
		if(tv.getMovementMethod()==null||!(tv.getMovementMethod() instanceof LinkArrowKeyMovementMethod))
		tv.setMovementMethod(LinkArrowKeyMovementMethod.getInstance());
	}
}
