package com.sss.report.service;

import java.nio.ByteBuffer;


public class FileService  {

	private static final String fileNameExtensionSeparator = ".";
	private static final String fileSizeInBytes = " B";
	private static final String fileSizesSIUnits = "kMGTPE";
	private static final String fileSizesMetricUnit = "KMGTPE";
	private static final String fileSizeMetricUnitSpecifier = "i";
	private static final Integer siUnitBlockSize = 1000;
	private static final Integer metricUnitBlockSize = 1024;

	public static String humanReadableByteCount(Long bytes) {
		boolean si = false;
	    int unit = si ? siUnitBlockSize : metricUnitBlockSize;
	    if (bytes < unit) return bytes + fileSizeInBytes;
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = (si ? fileSizesSIUnits : fileSizesMetricUnit).charAt(exp-1) + (si ? "" : fileSizeMetricUnitSpecifier);
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}

	public static Long bytesToLong(byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		buffer.put(bytes);
		buffer.flip();
		return buffer.getLong();
	}

	
	public static String getFileNameWithoutExtension(String fullyQualifiedFileName) {
		String fileNameWithoutExt = fullyQualifiedFileName.substring(0, fullyQualifiedFileName.lastIndexOf(fileNameExtensionSeparator));
		return fileNameWithoutExt;
	}

	public static String getFileExtension(String fullyQualifiedFileName) {
		String fileExt = fullyQualifiedFileName.substring(fullyQualifiedFileName.lastIndexOf(fileNameExtensionSeparator) + 1);
		return fileExt;
	}

}
