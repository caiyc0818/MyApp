package com.bcinfo.tripaway.bean;

import java.util.ArrayList;

public class DataBean {
private String type;
private ArrayList<Feed_Schema>  obj;
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public ArrayList<Feed_Schema> getObj() {
	return obj;
}
public void setObj(ArrayList<Feed_Schema> obj) {
	this.obj = obj;
}
public DataBean(String type, ArrayList<Feed_Schema> obj) {
	super();
	this.type = type;
	this.obj = obj;
}
public DataBean() {
	super();
	// TODO Auto-generated constructor stub
}





}
