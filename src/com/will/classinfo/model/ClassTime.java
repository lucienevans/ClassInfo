package com.will.classinfo.model;

import java.io.Serializable;

public class ClassTime implements Serializable{
	private int day=0;
	private int time=0;
	private int singleOrDouble=0;
	private String location=null;
	public ClassTime(int day, int time, int singleOrDouble, String location) {
		super();
		this.day = day;
		this.time = time;
		this.singleOrDouble = singleOrDouble;
		this.location = location;
	}
	public ClassTime() {
		// TODO Auto-generated constructor stub
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getSingleOrDouble() {
		return singleOrDouble;
	}
	public void setSingleOrDouble(int singleOrDouble) {
		this.singleOrDouble = singleOrDouble;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getTimeStr(int mornClassNum,int afterClassNum,int evenClassNum){
		if(this.time<=mornClassNum){
			return "早上第"+time+"节";
		}else if(this.time<=(afterClassNum+mornClassNum)){
			return "下午第"+(time-mornClassNum)+"节";
		}else{
			return "晚上第"+(time-afterClassNum-mornClassNum)+"节";
		}
	}
	@Override
	public String toString() {
		return "ClassTime [day=" + day + ", time=" + time + ", singleOrDouble=" + singleOrDouble + ", location=" + location + "]";
	}
	
}
