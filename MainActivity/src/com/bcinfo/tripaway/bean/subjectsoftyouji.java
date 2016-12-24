package com.bcinfo.tripaway.bean;

public class subjectsoftyouji {
/*
 * 主题页的数据
 */
	private String abstracts;
	private String title;
	private String cover;

	public subjectsoftyouji() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getAbstracts() {
		return abstracts;
	}

	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	@Override
	public String toString() {
		return "subjectsoftyouji [abstracts=" + abstracts + ", title=" + title + ", cover=" + cover + "]";
	}

	public subjectsoftyouji(String abstracts, String title, String cover) {
		super();
		this.abstracts = abstracts;
		this.title = title;
		this.cover = cover;
	}


}
