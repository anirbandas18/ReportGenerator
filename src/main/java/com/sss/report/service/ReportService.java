package com.sss.report.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import com.sss.report.core.Constants;
import com.sss.report.dao.FieldPermissionsDAO;
import com.sss.report.dao.ProfileDAO;
import com.sss.report.entity.ProfileEntity;

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
		Long start = System.currentTimeMillis();
		switch(mode) {
		case profile :
			ProfileDAO profileDAO = new ProfileDAO();
			Set<ProfileEntity> fields = profileDAO.retrieveAll();
			for(ProfileEntity pe : fields) {
				csvService = new CSVService(mode.toString(), pe.getName());
				FutureTask<String> csvTask = new FutureTask<String>(csvService);
				threadPool.submit(csvTask);
				reportContent = csvTask.get();
				String fileName = pe.getName().split("\\.")[0] + CSVService.CSV_EXTENSION;
				Path filePath = Files.write(Paths.get(dirLocation, fileName), reportContent.getBytes());
				System.out.println(filePath);
			}
			break;
		case property :
			FieldPermissionsDAO fieldPermissionsDAO = new FieldPermissionsDAO();
			Set<String> uniqueFields = fieldPermissionsDAO.retrieveUniqueFields();
			for(String fieldName : uniqueFields) {
				csvService = new CSVService(mode.toString(), fieldName);
				FutureTask<String> csvTask = new FutureTask<String>(csvService);
				threadPool.submit(csvTask);
				reportContent = csvTask.get();
				String fileName = fieldName + CSVService.CSV_EXTENSION;
				Path filePath = Files.write(Paths.get(dirLocation, fileName), reportContent.getBytes());
				System.out.println(filePath);
			}
			break;
		default : 
			break;
		}
		threadPool.shutdown();
		Long end = System.currentTimeMillis();
		System.out.println((end -start) + " miliseconds");
		return dirLocation;
	}

}
