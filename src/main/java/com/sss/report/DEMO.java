package com.sss.report;

import java.io.File;

public class DEMO {

	public static void main(String[] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		// TODO Auto-generated method stub
		String f = args[0];
		System.out.println(f.substring(f.lastIndexOf(File.separatorChar) + 1));
	}

}
