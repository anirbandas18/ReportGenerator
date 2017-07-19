package com.sss.report.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import com.sss.report.core.Constants;
import com.sss.report.core.Utility;
import com.sss.report.dao.FieldPermissionsDAO;

public class ReportService implements Callable<String> {
	
	private CSVService csvService;
	
	private String mode;
	
	private String reportRepositoryLocation;
	
	public ReportService(String mode, String reportRepositoryLocation) {
		this.mode = mode;
		this.reportRepositoryLocation = reportRepositoryLocation; 
	}
	
	private String createDirectories() throws IOException {
		Path dirPath = Paths.get(reportRepositoryLocation, mode.toString());
		if(!Files.exists(dirPath)) {
			return Files.createDirectories(dirPath).toString();
		}
		return dirPath.toString();
	}

	@Override
	public String call() throws Exception {
		String reportContent = "";
		String dirLocation = createDirectories();
		Constants mode = Constants.valueOf(this.mode);
		ExecutorService threadPool = Executors.newCachedThreadPool();
		FieldPermissionsDAO fieldPermissionsDAO = new FieldPermissionsDAO();
		Long start = System.currentTimeMillis();
		Set<String> uniqueResults = new TreeSet<>();
		switch(mode) {
		case profile :
			/*Set<String> uniqueProfiles = fieldPermissionsDAO.retrieveUniqueProfiles();
			for(String profileName : uniqueProfiles) {
				csvService = new CSVService(mode.toString(), profileName);
				FutureTask<String> csvTask = new FutureTask<String>(csvService);
				threadPool.submit(csvTask);
				reportContent = csvTask.get();
				String fileName = profileName.split("\\.")[0] + Utility.CSV_EXTENSION;
				Path filePath = Files.write(Paths.get(dirLocation, fileName), reportContent.getBytes());
				System.out.println(filePath);
			}*/
			uniqueResults = fieldPermissionsDAO.retrieveUniqueProfiles();
			break;
		case property :
			/*Set<String> uniqueFields = fieldPermissionsDAO.retrieveUniqueFields();
			for(String fieldName : uniqueFields) {
				csvService = new CSVService(mode.toString(), fieldName);
				FutureTask<String> csvTask = new FutureTask<String>(csvService);
				threadPool.submit(csvTask);
				reportContent = csvTask.get();
				String fileName = fieldName + Utility.CSV_EXTENSION;
				Path filePath = Files.write(Paths.get(dirLocation, fileName), reportContent.getBytes());
				System.out.println(filePath);
			}*/
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
		return dirLocation;
	}

}
