package com.sss.report.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sss.report.core.XMLParser;
import com.sss.report.dao.FieldPermissionsDAO;
import com.sss.report.dao.ProfileDAO;
import com.sss.report.entity.FieldPermissionsEntity;
import com.sss.report.entity.ProfileEntity;

public class XMLServices {

	
	public Set<ProfileEntity> parseProfiles(String xmlRepositoryLocation) throws IOException, InterruptedException, ExecutionException, XMLStreamException {
		Set<ProfileEntity> profileSet = new TreeSet<>(); 
		Path xmlRepositoryPath = Paths.get(xmlRepositoryLocation);
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		xmlInputFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);
		ExecutorService threadPool = Executors.newCachedThreadPool();
		ProfileDAO profileDAO = new ProfileDAO();
		FieldPermissionsDAO fieldPermissionsDAO = new FieldPermissionsDAO();
		DirectoryStream<Path> xmlFilesFromRepository = Files.newDirectoryStream(xmlRepositoryPath);
		for(Path xmlFile : xmlFilesFromRepository) {
			String xmlFilePath = xmlFile.toString();
			String xmlFileName = xmlFilePath.substring(xmlFilePath.lastIndexOf(File.separatorChar) + 1);
			
			ProfileEntity profile = new ProfileEntity();
			profile.setName(xmlFileName);
			profileDAO.create(profile);
			
			InputStream xmlInputStream = new FileInputStream(xmlFilePath);
			XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(xmlInputStream);
			
			XMLParser xmlParser = new XMLParser(xmlStreamReader, profile);
			FutureTask<Set<FieldPermissionsEntity>> xmlParsingTask = new FutureTask<>(xmlParser);
			threadPool.submit(xmlParsingTask);
			Set<FieldPermissionsEntity> fieldPermissionsSet = xmlParsingTask.get();
			
			for(FieldPermissionsEntity fpe : fieldPermissionsSet) {
				System.out.println(fieldPermissionsDAO.create(fpe) + " " + fpe.getField());
			}
			profileSet.add(profile);
		}
		threadPool.shutdown();
		return profileSet;
	}
	
}
