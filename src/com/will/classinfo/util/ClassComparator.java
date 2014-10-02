package com.will.classinfo.util;

import java.util.Comparator;

import com.will.classinfo.model.Class;

public class ClassComparator implements Comparator<Class>{

	@Override
	public int compare(Class lhs, Class rhs) {
		// TODO Auto-generated method stub
		return (lhs.getTime(0).getTime()>rhs.getTime(0).getTime())?1:-1;
	}

}
