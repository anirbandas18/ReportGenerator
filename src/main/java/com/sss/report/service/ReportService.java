package com.sss.report.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

import com.sss.report.core.Constants;
import com.sss.report.core.Utility;
import com.sss.report.dao.FieldPermissionsDAO;
import com.sss.report.dao.TabVisibilitiesDAO;
import com.sss.report.model.ReportMetadata;

public class ReportService {
	
	private CSVService csvService;
	
	private ReportMetadata reportMetadata;
	
	public ReportService(ReportMetadata reportMetadata) {
		this.reportMetadata = reportMetadata;
	}
	
	private 
	
	private Map<Constants,String> createDirectories() throws IOException {
		Map<Constants,String> reportPaths = new TreeMap<>();
		Set<Set<Constants>> propertiesCollection = profileSet.stream().map(x -> x.getProperties()).collect(Collectors.toSet());
		Set<Constants> properties = propertiesCollection.stream().flatMap(Set::stream).collect(Collectors.toSet());
		for(Constants property : properties) {
			Path dirPath = Paths.get(reportRepositoryLocation, mode.toString(), property.toString());
			if(!Files.exists(dirPath)) {
				dirPath = Files.createDirectories(dirPath);
			}
			reportPaths.put(property, dirPath.toString());
		}
		return reportPaths;
	}

	public List<String> call() throws Exception {
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
		/*for(String result : uniqueResults) {
			csvService = new CSVService(mode.toString(), result);
			FutureTask<String> csvTask = new FutureTask<String>(csvService);
			threadPool.submit(csvTask);
			reportContent = csvTask.get();
			String fileName = result + Utility.CSV_EXTENSION;
			byte[] bytes = reportContent.getBytes();
			Path filePath = Paths.get(dirLocation, fileName);
			filePath = Files.write(filePath, bytes);
			System.out.println(filePath + " " + Utility.humanReadableByteCount(Utility.bytesToLong(bytes)));
		}*/
		threadPool.shutdown();
		Long end = System.currentTimeMillis();
		System.out.println("Reports generated in " + Utility.milisecondsToSeconds(end -start) + " seconds");
		List<String> reports = new ArrayList<String>(reportPaths.values());
		return reports;
	}

}
