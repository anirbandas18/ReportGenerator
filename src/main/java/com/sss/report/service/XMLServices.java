package com.sss.report.service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javax.xml.stream.XMLStreamException;

import com.sss.report.core.XMLParser;
import com.sss.report.entity.FieldPermissionsEntity;

public class XMLServices {

	
	public Map<String,List<FieldPermissionsEntity>> parseProfiles(String xmlRepositoryLocation) throws IOException, InterruptedException, ExecutionException, XMLStreamException {
		Map<String,List<FieldPermissionsEntity>> profileSet = new TreeMap<>(); 
		Path xmlRepositoryPath = Paths.get(xmlRepositoryLocation);
		ExecutorService threadPool = Executors.newCachedThreadPool();
		DirectoryStream<Path> xmlFilesFromRepository = Files.newDirectoryStream(xmlRepositoryPath);
		for(Path xmlFile : xmlFilesFromRepository) {
			String xmlFilePath = xmlFile.toString();
			XMLParser xmlParser = new XMLParser(xmlFilePath);
			FutureTask<List<FieldPermissionsEntity>> xmlParsingTask = new FutureTask<>(xmlParser);
			threadPool.submit(xmlParsingTask);
			List<FieldPermissionsEntity> fieldPermissionsList = xmlParsingTask.get();
			profileSet.put(xmlFilePath, fieldPermissionsList);
		}
		threadPool.shutdown();
		return profileSet;
	}
	
}
