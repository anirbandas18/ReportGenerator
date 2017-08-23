package com.sss.report.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.persistence.Id;

import com.sss.report.core.tags.Key;
import com.sss.report.core.tags.Mode;
import com.sss.report.entity.ProfileEntity;
import com.sss.report.metadata.model.ProfileMetadata;
import com.sss.report.metadata.model.ReportMetadata;
import com.sss.report.util.Utility;

public class ReportDumpService {
	
	private Comparator<Object> entityComparator = new Comparator<Object>() {
		
		@Override
		public int compare(Object o1, Object o2) {
			Field field1 = Arrays.asList(o1.getClass().getDeclaredFields()).stream().filter(f -> f.isAnnotationPresent(Key.class)).findAny().orElse(null);
			Field field2 = Arrays.asList(o2.getClass().getDeclaredFields()).stream().filter(f -> f.isAnnotationPresent(Key.class)).findAny().orElse(null);
			if(field1 == null || field2 == null) {
				return 0;
			} else {
				try {
					field1.setAccessible(true);
					field2.setAccessible(true);
					String v1 = (String) field1.get(o1);
					String v2 = (String) field2.get(o2);
					field1.setAccessible(false);
					field2.setAccessible(false);
					return v1.compareTo(v2);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
		}
	};

	private ReportMetadata reportMetadata;
	private List<Class<?>> repositoryList;
	private static final String FIND_BY_PROFILE = "findByProfile";
	private static final String FIND_UNIQUE_PROPERTY_KEYS = "findAllDistinct";
	private static final String FIND_BY_PROPERT_KEY = "findByKeyProperty";
	private static final String HEADER = "Profile NameSorted Ascending";

	public ReportDumpService(ReportMetadata reportMetadata) {
		super();
		this.reportMetadata = reportMetadata;
		this.repositoryList = Utility.getRepositoryClassList();
	}

	private String createDirectories(String propertyName) throws IOException {
		Path reportPathByProperty = Paths.get(reportMetadata.getReportDumpLocation(),reportMetadata.getMode().toString());
		if (!Files.exists(reportPathByProperty)) {
			reportPathByProperty = Files.createDirectories(reportPathByProperty);
		}
		if(reportMetadata.getFilter().isEmpty()) {
			reportPathByProperty = Paths.get(reportMetadata.toString(), propertyName);
			if(!Files.exists(reportPathByProperty)) {
				reportPathByProperty = Files.createDirectories(reportPathByProperty);
			}
		} 
		return reportPathByProperty.toString();
	}

	private String csv(String header, List<Class<? extends ProfileEntity>> entities)
			throws IOException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException,
			SecurityException, NoSuchMethodException, InvocationTargetException {
		StringWriter sw = new StringWriter();
		BufferedWriter bw = new BufferedWriter(sw);
		bw.write(Utility.CSV_DELIMITTER + header);
		bw.newLine();
		for (Object entity : entities) {
			String name = reportMetadata.getMode() == Mode.profile ? Utility.getKeyPropertyFromEntity(entity)
					: Utility.getProfileFromEntity(entity);
			String line = name + Utility.CSV_DELIMITTER + entity.toString();
			bw.write(line);
			bw.newLine();
		}
		bw.flush();
		String content = sw.toString();
		return content;
	}

	@SuppressWarnings("unchecked")
	private Integer profile(String propertyName) throws InstantiationException, IllegalAccessException,
			SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, IOException,
			ClassNotFoundException, NoSuchFieldException {
		ProfileMetadata profileMetadata = reportMetadata.getProfileMetadata();
		String parentReportDumpLocationForProperty = createDirectories(propertyName);
		int noOfReports = 0;
		for (String profileName : profileMetadata.getProfileNames()) {
			String reportName = profileName + Utility.CSV_EXTENSION;
			Path reportLocationForProfile = Paths.get(parentReportDumpLocationForProperty, reportName);
			Long start = System.currentTimeMillis();
			int index = Utility.searchRepositories(repositoryList, propertyName);
			Class<?> daoClass = repositoryList.get(index);
			Object dao = daoClass.newInstance();
			Method findByProfile = daoClass.getDeclaredMethod(FIND_BY_PROFILE, String.class);
			List<Class<? extends ProfileEntity>> entities = (List<Class<? extends ProfileEntity>>) findByProfile
					.invoke(dao, profileName);
			String content = csv(profileName, entities);
			byte[] bytes = content.getBytes();
			reportLocationForProfile = Files.write(reportLocationForProfile, bytes);
			Long end = System.currentTimeMillis();
			Long duration = end - start;
			noOfReports++;
			System.out.println(reportLocationForProfile.toString() + " of size "
					+ Utility.humanReadableByteCount(Utility.bytesToLong(bytes)) + " generated in "
					+ Utility.milisecondsToSeconds(duration) + " seconds");
		}
		return noOfReports;
	}

	@SuppressWarnings("unchecked")
	private Integer profileFilteredProperties(String propertyName) throws InstantiationException,
			IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException,
			InvocationTargetException, IOException, ClassNotFoundException, NoSuchFieldException {
		ProfileMetadata profileMetadata = reportMetadata.getProfileMetadata();
		String reportDumpFileLocationForProperty = createDirectories(propertyName);
		Path reportDumpFileForProperty = Paths.get(reportDumpFileLocationForProperty, propertyName + Utility.CSV_EXTENSION);
		int noOfReports = 0;
		Utility.redirectError(true);
		Long start = System.currentTimeMillis();
		Map<String, List<Class<? extends ProfileEntity>>> profileEntities = new TreeMap<>();
		for (String profileName : profileMetadata.getProfileNames()) {
			int index = Utility.searchRepositories(repositoryList, propertyName);
			Class<?> daoClass = repositoryList.get(index);
			Object dao = daoClass.newInstance();
			Method findByProfile = daoClass.getDeclaredMethod(FIND_BY_PROFILE, String.class);
			List<Class<? extends ProfileEntity>> sub = (List<Class<? extends ProfileEntity>>) findByProfile.invoke(dao,
					profileName);
			System.err.println(profileName);
			for(Object e : sub) {
				System.err.println(e);
			}
			Collections.sort(sub, entityComparator);
			profileEntities.put(profileName, sub);
		}
		Utility.redirectError(false);
		String content = csvFilteredProperties(HEADER, profileEntities);
		byte[] bytes = content.getBytes();
		Path reportLocationForProperty = Files.write(reportDumpFileForProperty, bytes);
		Long end = System.currentTimeMillis();
		Long duration = end - start;
		noOfReports++;
		System.out.println(reportLocationForProperty.toString() + " of size "
				+ Utility.humanReadableByteCount(Utility.bytesToLong(bytes)) + " generated in "
				+ Utility.milisecondsToSeconds(duration) + " seconds");
		return noOfReports;
	}

	private String csvFilteredProperties(String header,
			Map<String, List<Class<? extends ProfileEntity>>> profileEntities) throws IllegalArgumentException, IllegalAccessException, IOException, NoSuchMethodException, SecurityException, InvocationTargetException {
		StringWriter sw = new StringWriter();
		BufferedWriter bw = new BufferedWriter(sw);
		String headerLine = header + Utility.CSV_DELIMITTER;
		List<Class<? extends ProfileEntity>> valueList = profileEntities.values().stream().flatMap(List::stream).collect(Collectors.toList());
		Set<Class<? extends ProfileEntity>> valueSet = new TreeSet<>(entityComparator);
		valueSet.addAll(valueList);// using compareTo of parent instead of child
		for (Object entity : valueSet) {
			headerLine = headerLine + Utility.formatEntityMetadata(entity);
		}
		headerLine = headerLine.substring(0, headerLine.lastIndexOf(','));
		bw.write(headerLine);
		bw.newLine();
		Utility.redirectError(true);
		for (String key : profileEntities.keySet()) {
			String line = key + Utility.CSV_DELIMITTER;
			System.err.println(key);
			int count = 0, n = 0;
			List<Class<? extends ProfileEntity>> value = profileEntities.get(key);
			Object x = value.get(0);
			for (Field f : x.getClass().getDeclaredFields()) {
				Boolean isKey = f.isAnnotationPresent(Key.class);
				Boolean isId = f.isAnnotationPresent(Id.class);
				if (!(isKey || isId)) {
					n++;
				}
			}
			for(Object entity : valueSet) {
				//System.err.println(entity.toString());
				if(count < value.size()) {
					Object item = value.get(count);
					if(item.equals(entity)) {
						for (Field f : entity.getClass().getDeclaredFields()) {
							Boolean isKey = f.isAnnotationPresent(Key.class);
							Boolean isId = f.isAnnotationPresent(Id.class);
							if (!(isKey || isId)) {
								f.setAccessible(true);
								count++;
								if (f.getType() == Boolean.class) {
									Boolean flag = (Boolean) f.get(entity);
									line = line + (flag ? "Yes" : "No") + Utility.CSV_DELIMITTER;
								} else {
									line = line + f.get(entity) + Utility.CSV_DELIMITTER;
								}
								System.err.print(f.getName() + "=" + f.get(entity) + ",");
								f.setAccessible(false);
							} else if(isKey) {
								f.setAccessible(true);
								System.err.print(f.getName() + "=" + f.get(entity) + ",");
								f.setAccessible(false);
							}
						}
						System.err.println();
						count++;
					} else {
						line = line + String.join("", Collections.nCopies(n, Utility.CSV_DELIMITTER));
					} 
				} else {
					line = line + String.join("", Collections.nCopies(n, Utility.CSV_DELIMITTER));
				}
			}
			System.err.println(count);
			line = line.substring(0, line.lastIndexOf(','));
			bw.write(line);
			bw.newLine();
		}
		Utility.redirectError(false);
		bw.flush();
		String content = sw.toString();
		return content;
	}

	public void dump(ReportMetadata reportMetadata) throws Exception {
		ProfileMetadata profileMetadata = reportMetadata.getProfileMetadata();
		Long start = System.currentTimeMillis();
		int noOfReports = 0;
		for (String propertyName : profileMetadata.getProfileProperties()) {
			switch (reportMetadata.getMode()) {
			case profile:
				if (reportMetadata.getFilter().isEmpty()) {
					noOfReports = noOfReports + profile(propertyName);
				} else {
					noOfReports = noOfReports + profileFilteredProperties(propertyName);
				}
				break;
			case properties:
				noOfReports = noOfReports + properties(propertyName);
				break;
			}
		}
		Long end = System.currentTimeMillis();
		System.out.println(noOfReports + " reports dumped at " + reportMetadata.getReportDumpLocation() + " in "
				+ Utility.milisecondsToSeconds(end - start) + " seconds");
	}

	@SuppressWarnings("unchecked")
	private Integer properties(String propertyName) throws InstantiationException, IllegalAccessException,
			SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, IOException,
			ClassNotFoundException, NoSuchFieldException {
		String parentReportDumpLocationForProperty = createDirectories(propertyName);
		int noOfReports = 0;
		int index = Utility.searchRepositories(repositoryList, propertyName);
		Class<?> daoClass = repositoryList.get(index);
		Object dao = daoClass.newInstance();
		Method findUniqueKeysOfProperty = daoClass.getDeclaredMethod(FIND_UNIQUE_PROPERTY_KEYS);
		List<String> propertyKeys = (List<String>) findUniqueKeysOfProperty.invoke(dao);
		for (String key : propertyKeys) {
			String reportName = key + Utility.CSV_EXTENSION;
			Path reportLocationForProfile = Paths.get(parentReportDumpLocationForProperty, reportName);
			Long start = System.currentTimeMillis();
			Method findByKeyProperty = daoClass.getDeclaredMethod(FIND_BY_PROPERT_KEY, String.class);
			List<Class<? extends ProfileEntity>> entities = (List<Class<? extends ProfileEntity>>) findByKeyProperty
					.invoke(dao, key);
			String content = csv(key, entities);
			byte[] bytes = content.getBytes();
			reportLocationForProfile = Files.write(reportLocationForProfile, bytes);
			Long end = System.currentTimeMillis();
			Long duration = end - start;
			noOfReports++;
			System.out.println(reportLocationForProfile.toString() + " of size "
					+ Utility.humanReadableByteCount(Utility.bytesToLong(bytes)) + " generated in "
					+ Utility.milisecondsToSeconds(duration) + " seconds");
		}
		return noOfReports;
	}

}
