package com.bcinfo.tripaway.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tencent.wxop.stat.t;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class RichText implements Parcelable{
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

	private Type type;
	private Map<String, String> attrs=new HashMap<String, String>();
	private Integer start;
	public Integer getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	private int end;

	public RichText(Type type) {
		// TODO Auto-generated constructor stub
		this.type=type;
	}
	
	private String uri;
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
	private Bitmap bitmap;
	
	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	private String text;
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "start:"+start+" end:"+end+" type:"+type+" text:"+text+" attrs:"+attrs;
	}
	public enum Type{
		PIC,
		LITPIC,
		TEXT,
		TOPIC,
		MUSIC,
		BOOK,
		FILM;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		out.writeValue(type);
		out.writeInt(start);
		out.writeInt(end);
		out.writeString(uri);
		out.writeMap(attrs);
		out.writeString(text);
//		out.writeParcelable(bitmap, 0);
	}
	
	public static final Parcelable.Creator<RichText> CREATOR = new Parcelable.Creator<RichText>() {
		public RichText createFromParcel(Parcel source) {
			return new RichText(source);
		}

		public RichText[] newArray(int size) {
			return new RichText[size];
		}
	};
	
	public RichText(Parcel in) {
		type = (Type) in.readValue(Type.class.getClassLoader());
		start = in.readInt();
		end = in.readInt();
		uri=in.readString();
		attrs=in.readHashMap(HashMap.class.getClassLoader());
		text=in.readString();
//		bitmap=in.readParcelable(Bitmap.class.getClassLoader());
	}
}
