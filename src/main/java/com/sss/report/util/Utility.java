package com.sss.report.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.Table;

import org.reflections.Reflections;

import com.sss.report.core.tags.DAO;
import com.sss.report.core.tags.Key;
import com.sss.report.entity.ProfileEntity;

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
	
	private static List<Class<? extends ProfileEntity>> entityClassList;
	private static List<Class<?>> repositoryClassList;
	private static final String basePackageOfEntities = "com.sss.report.entity";
	private static final String basePackageOfRepositories = "com.sss.report.dao";
	
	
	public static List<Class<? extends ProfileEntity>> getEntityClassList() {
		return entityClassList;
	}

	public static List<Class<?>> getRepositoryClassList() {
		return repositoryClassList;
	}
	
	public static void configure() {
		System.setProperty("derby.system.home", System.getProperty("user.dir"));
		System.out.println(System.getProperty("derby.system.home"));
		Reflections entityPackage = new Reflections(basePackageOfEntities);
		entityClassList = new ArrayList<>(entityPackage.getSubTypesOf(ProfileEntity.class));
		Collections.sort(entityClassList, classComparator);
		Reflections repositoryPackage = new Reflections(basePackageOfRepositories);
		repositoryClassList = new ArrayList<>(repositoryPackage.getTypesAnnotatedWith(DAO.class));
		Collections.sort(repositoryClassList, classComparator);
	}
	
	
	private static Comparator<Class<?>> classComparator = new Comparator<Class<?>>() {

		@Override
		public int compare(Class<?> o1, Class<?> o2) {
			String simpleName1 = o1.getSimpleName();
			String simpleName2 = o2.getSimpleName();
			return simpleName1.compareTo(simpleName2);
		}
		
	};

	
	public static String getKeyNameFromEntity(Object entity) throws IllegalArgumentException, IllegalAccessException {
		String name = "";
		Class<?> entityClass = entity.getClass();
		List<Field> declaredFields = new ArrayList<>(Arrays.asList(entityClass.getDeclaredFields()));
		Field field = declaredFields.stream().filter(f -> f.isAnnotationPresent(Key.class)).findAny().orElse(null);
		if(field != null) {
			field.setAccessible(true);
			name = (String) field.get(entity);
			field.setAccessible(false);
		}
		return name;
	}
	
	public static Integer searchEntities(List<Class<? extends ProfileEntity>> list, String item) throws InstantiationException, IllegalAccessException {
		Integer low = 0;
		Integer high = list.size() - 1;
		Integer position = -1;
		item = item.toLowerCase();
		while (low <= high) {
			Integer mid = (high + low) / 2;
			Class<? extends ProfileEntity> midElement = list.get(mid);
			Table annotation = midElement.getDeclaredAnnotation(Table.class);
			String formattedTableName = Utility.formatTableName(annotation.name());
			if (formattedTableName.compareTo(item) == 0) {
				position = mid;
				//propertyNames.add(item);
				break;
			} else if (formattedTableName.compareTo(item) > 0) {
				high = mid - 1;
			} else if (formattedTableName.compareTo(item) < 0) {
				low = mid + 1;
			}
		}
		return position;
	}

	public static Integer searchRepositories(List<Class<?>> list, String item)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Integer low = 0;
		Integer high = list.size() - 1;
		Integer position = -1;
		item = item.toLowerCase();
		while (low <= high) {
			Integer mid = (high + low) / 2;
			Class<?> midElement = list.get(mid);
			DAO dao = midElement.getDeclaredAnnotation(DAO.class);
			Class<? extends ProfileEntity> entity = dao.forEntity();
			Table annotation = entity.getDeclaredAnnotation(Table.class);
			String formattedTableName = Utility.formatTableName(annotation.name());
			if (formattedTableName.compareTo(item) == 0) {
				position = mid;
				break;
			} else if (formattedTableName.compareTo(item) > 0) {
				high = mid - 1;
			} else if (formattedTableName.compareTo(item) < 0) {
				low = mid + 1;
			}
		}
		return position;
	}

}
