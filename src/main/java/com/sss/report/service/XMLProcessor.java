package com.sss.report.service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javax.xml.stream.XMLStreamException;

import org.reflections.Reflections;

import com.sss.report.core.DAO;
import com.sss.report.core.Utility;
import com.sss.report.core.XMLParser;
import com.sss.report.entity.ProfileEntity;
import com.sss.report.model.ProfileMetadata;

public class XMLProcessor {

	
	private static final String basePackageOfEntities = "com.sss.report.entity";
	private static final String basePackageOfRepositories = "com.sss.report.dao";
	private static List<Class<? extends ProfileEntity>> entityClassList;
	private static List<Class<?>> repositoryClassList;
	
	static {
		
	}
	
	public static List<Class<? extends ProfileEntity>> getEntityClassList() {
		return entityClassList;
	}

	public static List<Class<?>> getRepositoryClassList() {
		return repositoryClassList;
	}

	private static Comparator<Class<?>> classComparator = new Comparator<Class<?>>() {

		@Override
		public int compare(Class<?> o1, Class<?> o2) {
			String simpleName1 = o1.getSimpleName();
			String simpleName2 = o2.getSimpleName();
			return simpleName1.compareTo(simpleName2);
		}
		
	};
	
	private static void configure() {
		Reflections entityPackage = new Reflections(basePackageOfEntities);
		entityClassList = new ArrayList<>(entityPackage.getSubTypesOf(ProfileEntity.class));
		Collections.sort(entityClassList, classComparator);
		Reflections repositoryPackage = new Reflections(basePackageOfRepositories);
		repositoryClassList = new ArrayList<>(repositoryPackage.getTypesAnnotatedWith(DAO.class));
		Collections.sort(repositoryClassList, classComparator);
	}
	
	public static ProfileMetadata parseProfiles(Boolean shouldParse, String xmlRepositoryLocation) throws IOException, InterruptedException, ExecutionException, XMLStreamException {
		configure();
		Set<String> masterProperties = new TreeSet<>();
		Set<String> profileNames = new TreeSet<>();
		Path xmlRepositoryPath = Paths.get(xmlRepositoryLocation);
		ExecutorService threadPool = Executors.newCachedThreadPool();
		DirectoryStream<Path> xmlFilesFromRepository = Files.newDirectoryStream(xmlRepositoryPath);
		int counter = 0;
		Long totalDuration = 0L;
		for(Path xmlFile : xmlFilesFromRepository) {
			String xmlFilePath = xmlFile.toString();
			System.out.println("Processing : " + Utility.getXMLFileName(xmlFilePath));
			FutureTask<Set<String>> xmlParsingTask = new FutureTask<>( new XMLParser(xmlFilePath));
			Long begin = System.currentTimeMillis();
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
