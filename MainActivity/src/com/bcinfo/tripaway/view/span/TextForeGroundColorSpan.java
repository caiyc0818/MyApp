package com.bcinfo.tripaway.view.span;

import java.util.HashMap;
import java.util.Map;

import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.bcinfo.tripaway.bean.RichText.Type;
import com.bcinfo.tripaway.view.text.NoLineClickSpan;

public class TextForeGroundColorSpan extends ForegroundColorSpan{

	public TextForeGroundColorSpan(int color) {
		super(color);
		// TODO Auto-generated constructor stub
	}

	private Type type;
	private String textId;
	private Map<String, String> attrs=new HashMap<String, String>();

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

	public String getTextId() {
		return textId;
	}

	public void setTextId(String textId) {
		this.textId = textId;
	}

}
