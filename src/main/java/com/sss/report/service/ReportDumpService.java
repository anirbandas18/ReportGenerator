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
import java.util.Map;
import java.util.TreeMap;

import com.sss.report.entity.ProfileEntity;
import com.sss.report.metadata.model.ProfileMetadata;
import com.sss.report.metadata.model.ReportMetadata;
import com.sss.report.util.Utility;

public class ReportDumpService {
	
	private ReportMetadata reportMetadata;
	private List<Class<?>> repositoryList;
	private static final String FIND_BY_PROFILE = "findByProfile";
	
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
	
	private byte[] profileBasedCSV(String header, List<Class<? extends ProfileEntity>> entities) throws IOException, IllegalArgumentException, IllegalAccessException {
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
		byte[] bytes = content.getBytes();
		return bytes;
	}
	
	@SuppressWarnings("unchecked")
	private Map<Path, Long> profile(String propertyName) throws InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, IOException, ClassNotFoundException {
		Map<Path,Long> reportResult = new TreeMap<>(); 
		ProfileMetadata profileMetadata = reportMetadata.getProfileMetadata();
		String parentReportDumpLocationForProperty = createDirectories(propertyName);
		for(String profileName : profileMetadata.getProfileNames()) {
			String reportName = profileName + Utility.CSV_EXTENSION;
			Path reportLocationForProfile = Paths.get(parentReportDumpLocationForProperty, reportName);
			Long start = System.currentTimeMillis();
			int index = Utility.searchRepositories(repositoryList, propertyName);
			Class<?> daoClass = repositoryList.get(index);
			Object dao = daoClass.newInstance();
			Method findByProfile = daoClass.getDeclaredMethod(FIND_BY_PROFILE, String.class);
			List<Class<? extends ProfileEntity>> entities = (List<Class<? extends ProfileEntity>>) findByProfile.invoke(dao, "%" + profileName + "%");
			byte[] bytes = profileBasedCSV(profileName, entities);
			Files.write(reportLocationForProfile, bytes);
			Long end = System.currentTimeMillis();
			reportResult.put(reportLocationForProfile, end - start);
		}
		return reportResult;
	}
	
	public void process(ReportMetadata reportMetadata) throws Exception {
		ProfileMetadata profileMetadata = reportMetadata.getProfileMetadata();
		for(String propertyName : profileMetadata.getProfileProperties()) {
			switch(reportMetadata.getMode()) {
			case profile : // profile name list, dao list
				/*ProfileReporMetadata profileReport = new ProfileReporMetadata();
				profileReport.setProfileNames(new ArrayList<>(profileMetadata.getProfileNames()));
				profileReport.setParentReportDumpLocation(dumpLocationForProperty);*/
				
				/**
				 * call profile service
				 * for each profile name from profile list
				 * 		for each dao from dao list
				 * 			call retrieveByProfile(profile name) of dao
				 *  		dump entity list of respective call
				 *  		call csv processor in thread with dump to format dump into csv
				 * 			
				 */
				Map<Path,Long> reportResult = profile(propertyName);
				System.out.println(reportResult);
				break;
			case properties : // 
				break;
			}
		}
	}
	

	/*public List<String> call() throws Exception {
		String reportContent = "";
		Map<Constants,String> reportPaths = createDirectories();
		Constants mode = Constants.valueOf(this.mode);
		ExecutorService threadPool = Executors.newCachedThreadPool();
		FieldPermissionsDAO fieldPermissionsDAO = new FieldPermissionsDAO();
		TabVisibilitiesDAO tabVisibilitiesDAO = new TabVisibilitiesDAO();
		Long start = System.currentTimeMillis();
		Set<String> uniqueResults = new TreeSet<>();
		switch(mode) {
		case profile :
			for(String xmlFilePath : profileSet.keySet()) {
				String xmlFileName = Utility.getXMLFileName(xmlFilePath);
				ProfileService profileService = new ProfileService(xmlFileName, profileSet.get(xmlFilePath), reportPaths);
				FutureTask<Long> profileTask = new FutureTask<>(profileService);
				threadPool.submit(profileTask);
				Long duration = profileTask.get();
			}
			break;
		case property :
			uniqueResults = fieldPermissionsDAO.retrieveUniqueFields();
			break;
		default : 
			break;
		}
		for(String result : uniqueResults) {
			csvService = new CSVService(mode.toString(), result);
			FutureTask<String> csvTask = new FutureTask<String>(csvService);
			threadPool.submit(csvTask);
			reportContent = csvTask.get();
			String fileName = result + Utility.CSV_EXTENSION;
			byte[] bytes = reportContent.getBytes();
			Path filePath = Paths.get(dirLocation, fileName);
			filePath = Files.write(filePath, bytes);
			System.out.println(filePath + " " + Utility.humanReadableByteCount(Utility.bytesToLong(bytes)));
		}
		threadPool.shutdown();
		Long end = System.currentTimeMillis();
		System.out.println("Reports generated in " + Utility.milisecondsToSeconds(end -start) + " seconds");
		List<String> reports = new ArrayList<String>(reportPaths.values());
		return reports;
	}*/

}
