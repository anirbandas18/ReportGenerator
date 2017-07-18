package com.sss.report.service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javax.xml.stream.XMLStreamException;

import com.sss.report.core.XMLParser;
import com.sss.report.entity.ProfileEntity;

public class XMLServices {

	
	public Set<ProfileEntity> parseProfiles(String xmlRepositoryLocation) throws IOException, InterruptedException, ExecutionException, XMLStreamException {
		Set<ProfileEntity> profileSet = new TreeSet<>(); 
		Path xmlRepositoryPath = Paths.get(xmlRepositoryLocation);
		ExecutorService threadPool = Executors.newCachedThreadPool();
		DirectoryStream<Path> xmlFilesFromRepository = Files.newDirectoryStream(xmlRepositoryPath);
		for(Path xmlFile : xmlFilesFromRepository) {
			String xmlFilePath = xmlFile.toString();
			XMLParser xmlParser = new XMLParser(xmlFilePath);
			FutureTask<ProfileEntity> xmlParsingTask = new FutureTask<>(xmlParser);
			threadPool.submit(xmlParsingTask);
			ProfileEntity profile = xmlParsingTask.get();
			profileSet.add(profile);
		}
		threadPool.shutdown();
		return profileSet;
	}
	
}
