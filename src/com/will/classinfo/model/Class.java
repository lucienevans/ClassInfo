package com.will.classinfo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.will.classinfo.config.Config;

public class Class implements Serializable{
	private String className=null;
	private String teacher=null;
	private int startWeek=0;
	private int endWeek=0;
	private int examWeek=0;
	private List<ClassTime> times=null;
	
	public int position=0;
	
	
	public Class(){
		this.times=new ArrayList<ClassTime>();
	}
	public void addClassTime(int day,int time,String location,int singOrDouble){
		this.times.add(new ClassTime(day, time, singOrDouble, location));
	}
	public void addClassTime(ClassTime time){
		this.times.add(time);
	}
	public Class(Class mClass){
		this.className=mClass.getClassName();
		this.teacher=mClass.getTeacher();
		this.startWeek=mClass.getStartWeek();
		this.endWeek=mClass.getEndWeek();
		this.examWeek=mClass.getExamWeek();
		this.times=new ArrayList<ClassTime>();
		for(int i=0;i<mClass.getTimeNum();i++){
			ClassTime time=mClass.getTime(i);
			this.addClassTime(time.getDay(),time.getTime(),time.getLocation(),time.getSingleOrDouble());
		}
	}
	public void removeClassTime(int position){
		this.times.remove(position);
	}
	public void modefyClassTime(int position,ClassTime time){
		this.times.remove(position);
		this.times.add(position, time);
	}
	public void removeAllTimes(){
		this.times.clear();
	}
	public int getTimeNum(){
		return this.times.size();
	}
	public ClassTime getTime(int position){
		return times.get(position);
	}
	
	
	public void setClassName(String className) {
		this.className = className;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	public String getClassName() {
		return className;
	}
	public String getTeacher() {
		return teacher;
	}
	public int getStartWeek() {
		return startWeek;
	}
	public void setStartWeek(int startWeek) {
		this.startWeek = startWeek;
	}
	public int getEndWeek() {
		return endWeek;
	}
	public void setEndWeek(int endWeek) {
		this.endWeek = endWeek;
	}
	public int getExamWeek() {
		return examWeek;
	}
	public void setExamWeek(int examWeek) {
		this.examWeek = examWeek;
	}
	@Override
	public String toString() {
		return "Class [className=" + className + ", teacher=" + teacher + ", startWeek=" + startWeek + ", endWeek=" + endWeek + ", examWeek=" + examWeek + ", times=" + times + "]";
	}
	
}
