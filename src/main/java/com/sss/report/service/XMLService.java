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
import com.sss.report.metadata.model.ProfileMetadata;
import com.sss.report.util.Utility;

public class XMLService {
	
	private String xmlRepositoryLocation;
	
	public XMLService(String xmlRepositoryLocation) {
		super();
		this.xmlRepositoryLocation = xmlRepositoryLocation;
	}

	public ProfileMetadata parseProfiles(Boolean shouldParse) throws IOException, InterruptedException, ExecutionException, XMLStreamException {
		Set<String> masterProperties = new TreeSet<>();
		Set<String> profileNames = new TreeSet<>();
		Path xmlRepositoryPath = Paths.get(xmlRepositoryLocation);
		ExecutorService threadPool = Executors.newCachedThreadPool();
		System.out.println(xmlRepositoryPath);
		DirectoryStream<Path> xmlFilesFromRepository = Files.newDirectoryStream(xmlRepositoryPath);
		int counter = 0;
		Long totalDuration = 0L;
		for(Path xmlFile : xmlFilesFromRepository) {
			String xmlFilePath = xmlFile.toString();
			FutureTask<Set<String>> xmlParsingTask = new FutureTask<>( new XMLParser(xmlFilePath));
			Long begin = System.currentTimeMillis();
			System.out.println("Processing : " + Utility.getXMLFileName(xmlFilePath));
			if(shouldParse) {
				threadPool.submit(xmlParsingTask);
				Set<String> childProperties = xmlParsingTask.get();
				masterProperties.addAll(childProperties);
			}
			Long end = System.currentTimeMillis();
			totalDuration = totalDuration + (end - begin);
			counter++;
			String profileName =  Utility.getXMLFileName(xmlFilePath);
			System.out.println("Processed : " + profileName + " in " + Utility.milisecondsToSeconds(end - begin) + " seconds");
			profileName = Utility.getFileNameWithoutExtension(profileName);
			profileNames.add(profileName);
		}
		threadPool.shutdown();
		System.out.println(counter + " profiles processed from  " + xmlRepositoryLocation + " in " + Utility.milisecondsToSeconds(totalDuration) + " seconds");
		ProfileMetadata metadata = new ProfileMetadata();
		metadata.setProfileProperties(masterProperties);
		metadata.setProfileNames(profileNames);
		return metadata;
	}
	
}
