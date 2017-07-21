package com.sss.report.core;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utility {

	public static final String CSV_DELIMITTER = ",";
	public static final String PROPERTIES = "properties";
	public static final String PROFILE_EXTENSION = ".profile";
	public static final String CSV_EXTENSION = ".csv";
	public static final String FILE_NAME = "fileName";
	public static final String TABLE_NAME_SEPARATOR = "_";
	public static final char FILE_EXTENSION_SEPARATOR = '.';

	public static String getXMLFileName(String xmlFilePath) {
		String xmlFileName = xmlFilePath.substring(xmlFilePath.lastIndexOf(File.separatorChar) + 1);
		return xmlFileName;
	}
	
	public static Long bytesToLong(byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		buffer.put(bytes);
		buffer.flip();
		return buffer.getLong();
	}

	public static String milisecondsToSeconds(Long ms) {
		Float sec = (ms != null ? ms : 0.0f)/ 1000.0f;
		return String.format("%.3f", sec);
	}


	public static String humanReadableByteCount(long bytes) {
		boolean si = false;
	    int unit = si ? 1000 : 1024;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}

	public static String getEquivalentCSVFileName(String fileName) {
		int lastPos = fileName.lastIndexOf('.');
		String csvFileName = lastPos != -1 ? fileName.substring(0, lastPos) : fileName;
		csvFileName = csvFileName + Utility.CSV_EXTENSION;
		return csvFileName;
	}

	public static String getChildDirName(String childDir) {
		int pos = childDir.lastIndexOf(File.separator);
		String childDirName = childDir.substring(pos + 1);
		return childDirName;
	}

	public static Path createDir(String dirPath) throws IOException {
		System.out.println(dirPath);
		Path csvRepositoryDir = Paths.get(dirPath);
		if (!Files.exists(csvRepositoryDir)) {
			csvRepositoryDir = Files.createDirectory(csvRepositoryDir);
		}
		return csvRepositoryDir;
	}

	public static boolean isFieldPresent(Object bean, String fieldName, String fieldValue) throws IllegalArgumentException, IllegalAccessException {
		Class<?> clazz = bean.getClass();
		boolean flag = true;
		try {
			Field f = clazz.getDeclaredField(fieldName);
			f.setAccessible(true);
			Object value = f.get(bean);
			flag = value.equals(fieldValue);
			f.setAccessible(false);
		} catch (NoSuchFieldException e) {
			flag = false;
		}
		return flag;
	}
	
	public static Object getSimpleFieldByName(Object bean, String fieldName) throws IllegalArgumentException, IllegalAccessException {
		Class<?> clazz = bean.getClass();
		Object value = new Object();
		try {
			Field f = clazz.getDeclaredField(fieldName);
			f.setAccessible(true);
			value = f.get(bean);
			f.setAccessible(false);
		} catch (NoSuchFieldException e) {
			//value = null;
		}
		return value;
	}

	public static List<Object> getComplexFieldByName(Object bean, String fieldName)
			throws IllegalArgumentException, IllegalAccessException {
		Class<?> clazz = bean.getClass();
		List<Field> f = new ArrayList<Field>(Arrays.asList(clazz.getDeclaredFields()));
		Field item = f.stream().filter(z -> z.getName().equals(fieldName)).findFirst().orElse(null);
		List<Object> content = new ArrayList<>();
		if (item != null) {
			item.setAccessible(true);
			content = (List<Object>) item.get(bean);
			item.setAccessible(false);
		}
		return content;
	}

	public static String getFileNameWithoutExtension(String fullyQualifiedFileName) {
		String fileNameWithoutExt = fullyQualifiedFileName.substring(0, fullyQualifiedFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR));
		return fileNameWithoutExt;
	}

	public static String getFileExtension(String fullyQualifiedFileName) {
		String fileExt = fullyQualifiedFileName.substring(fullyQualifiedFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR) + 1);
		return fileExt;
	}
	
	public static String formatTableName(String name) {
		String tokens[] = name.split(TABLE_NAME_SEPARATOR);
		String formattedName = String.join("", tokens);
		return formattedName;
	}

}
