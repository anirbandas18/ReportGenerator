package com.sss.report;

import java.util.Arrays;

public class Demo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*String d = "Yes,Yes,Yes,Yes,Yes,Yes,Yes,Yes,Yes,No,No,No,Yes,Yes,Yes,No,No,No,Yes,Yes,Yes,No,No,No,Yes,Yes,Yes,No,No,No,No,Yes,No,No,No,Yes,Yes,Yes,Yes,No,No,Yes,No,Yes,No,No,No,No,Yes,Yes,Yes,No,No,No,Yes,Yes,Yes,No,No,Yes,Yes,Yes,Yes,No,No,No,No,Yes,No,No,No,Yes,Yes,Yes,Yes,Yes,No,No,Yes,Yes,No,No,No,No,No,Yes,No,No,No,Yes,No,Yes,No,No,No,Yes,No,Yes,Yes,Yes,No,No,Yes,Yes,Yes,Yes,Yes,Yes,Yes,Yes,Yes,No,No,Yes,No,Yes,No,No,No,Yes,Yes,Yes,Yes,No,No,No,Yes,Yes,Yes,No,No,No,Yes,Yes,No,No,No,No,Yes,Yes,Yes,No,No,No,Yes,Yes,Yes,No,No,No,Yes,Yes,Yes,No,No,No,Yes,Yes,Yes,No,No,No,Yes,Yes,Yes,No,No,No,Yes,Yes,Yes,No,No,No,No,Yes,No,No,No,No,Yes,Yes,Yes,No,No,Yes,No,Yes,No,No,No,No,Yes,Yes,Yes,No,No,No,No,Yes,No,No,No,Yes,Yes,Yes,Yes,No,No,No,No,Yes,No,No,No,No,Yes,Yes,Yes,No,No,No";
		String t[] = d.split(",");
		System.out.println(t.length);*/
		String s = ".pt";
		String t[] = s.split("\\.");
		System.out.println(Arrays.toString(t) + " " + t.length);
	}

}
