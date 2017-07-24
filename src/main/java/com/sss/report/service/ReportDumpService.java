package com.sss.report.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.sss.report.entity.ProfileEntity;
import com.sss.report.metadata.model.ProfileMetadata;
import com.sss.report.metadata.model.ReportMetadata;
import com.sss.report.util.Utility;

public class ReportDumpService {
	
	private ReportMetadata reportMetadata;
	private List<Class<?>> repositoryList;
	private static final String FIND_BY_PROFILE = "findByProfile";
	private static final String SQL_LIKE_SPECIFIER = "%";
	
	public ReportDumpService(ReportMetadata reportMetadata) {
		super();
		this.reportMetadata = reportMetadata;
		this.repositoryList = Utility.getRepositoryClassList();
	}
	private String createDirectories(String propertyName) throws IOException {
		Path reportPathByProperty = Paths.get(reportMetadata.getReportDumpLocation(), reportMetadata.getMode().toString(), propertyName);
		if(!Files.exists(reportPathByProperty)) {
			reportPathByProperty = Files.createDirectories(reportPathByProperty);
		}
		return reportPathByProperty.toString();
	}
	
	private String csv(String header, List<Class<? extends ProfileEntity>> entities) throws IOException, IllegalArgumentException, IllegalAccessException {
		StringWriter sw = new StringWriter();
		BufferedWriter bw = new BufferedWriter(sw);
		bw.write(Utility.CSV_DELIMITTER + header);
		bw.newLine();
		for(Object entity : entities) {
			String name = Utility.getKeyNameFromEntity(entity);
			String line = name + Utility.CSV_DELIMITTER + entity.toString();
			bw.write(line);
			bw.newLine();
		}
		bw.flush();
		String content = sw.toString();
		return content;
	}
	
	@SuppressWarnings("unchecked")
	private Integer profile(String propertyName) throws InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, IOException, ClassNotFoundException {
		ProfileMetadata profileMetadata = reportMetadata.getProfileMetadata();
		String parentReportDumpLocationForProperty = createDirectories(propertyName);
		int noOfReports = 0;
		for(String profileName : profileMetadata.getProfileNames()) {
			String reportName = profileName + Utility.CSV_EXTENSION;
			Path reportLocationForProfile = Paths.get(parentReportDumpLocationForProperty, reportName);
			Long start = System.currentTimeMillis();
			int index = Utility.searchRepositories(repositoryList, propertyName);
			Class<?> daoClass = repositoryList.get(index);
			Object dao = daoClass.newInstance();
			Method findByProfile = daoClass.getDeclaredMethod(FIND_BY_PROFILE, String.class);
			String param = SQL_LIKE_SPECIFIER + profileName + SQL_LIKE_SPECIFIER;
			List<Class<? extends ProfileEntity>> entities = (List<Class<? extends ProfileEntity>>) findByProfile.invoke(dao, param);
			String content = csv(profileName, entities);
			byte[] bytes = content.getBytes();
			reportLocationForProfile = Files.write(reportLocationForProfile, bytes);
			Long end = System.currentTimeMillis();
			Long duration = end - start;
			noOfReports++;
			System.out.println(reportLocationForProfile.toString() + " of size " + Utility.humanReadableByteCount(Utility.bytesToLong(bytes)) + " generated in " + Utility.milisecondsToSeconds(duration) + " seconds");
		}
		return noOfReports;
	}
	
	public void generate(ReportMetadata reportMetadata) throws Exception {
		ProfileMetadata profileMetadata = reportMetadata.getProfileMetadata();
		Long start = System.currentTimeMillis();
		int noOfReports = 0;
		for(String propertyName : profileMetadata.getProfileProperties()) {
			switch(reportMetadata.getMode()) {
			case profile : 
				noOfReports = noOfReports + profile(propertyName);
				break;
			case properties : // 
				break;
			}
		}
		Long end = System.currentTimeMillis();
		System.out.println(noOfReports + " reports dumped at " + reportMetadata.getReportDumpLocation() + " in " + Utility.milisecondsToSeconds(end - start) + " seconds");
	}
	
}
