package com.bcinfo.tripaway.bean;

public class Series {
private String id;
private String title;
private boolean isCheck=false;
public boolean isCheck() {
	return isCheck;
}


public void setCheck(boolean isCheck) {
	this.isCheck = isCheck;
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
}
