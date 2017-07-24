package com.sss.report.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sss.report.core.tags.DAO;
import com.sss.report.entity.ProfileEntity;
import com.sss.report.util.Utility;

public class XMLParser implements Callable<Set<String>> {

	private String xmlFilePath;
	private XMLStreamReader xmlStreamReader;
	private static final String CREATE_ENTITY_METHOD_NAME = "create";
	private static final String PROFILE_NAME_COLUMN = "profile";
	private static final String FIELD_FORMATTER = "_";
	private Set<String> propertyNames;
	private List<Class<?>> repositoryClassList;
	private List<Class<? extends ProfileEntity>> entityClassList;
	private Boolean shouldParse;
	
	
	public XMLParser(Boolean shouldParse, String xmlFilePath) {
		this.xmlFilePath = xmlFilePath;
		this.propertyNames = new TreeSet<>();
		this.shouldParse = shouldParse;
	}

	
	private void configureParser() throws FileNotFoundException, XMLStreamException {
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		xmlInputFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);
		System.out.println(xmlFilePath);
		InputStream stream = new FileInputStream(xmlFilePath);
		xmlStreamReader = xmlInputFactory.createXMLStreamReader(stream);
		entityClassList = Utility.getEntityClassList();
		repositoryClassList = Utility.getRepositoryClassList();
	}

	@Override
	public Set<String> call() throws Exception {
		Object entityObject = new Object();
		Object repositoryObject = new Object();
		Class<? extends ProfileEntity> entity = ProfileEntity.class;
		Class<?> repository = repositoryObject.getClass();
		configureParser();
		Integer position = -1;
		String tagName = "", tagContent = "";
		Boolean currentTagBelongsToEntity = false;
		while (xmlStreamReader.hasNext()) {
			switch (xmlStreamReader.getEventType()) {
			case XMLStreamReader.START_ELEMENT:
				tagName = xmlStreamReader.getLocalName();
				position = Utility.searchEntities(entityClassList, tagName);
				if (position != -1) {
					entity = entityClassList.get(position);
					entityObject = entity.newInstance();
					propertyNames.add(tagName.toLowerCase());
				} else {
					try {
						entity.getDeclaredField(tagName);
						currentTagBelongsToEntity = true;
					} catch (NoSuchFieldException e) {
						tagName = tagName + FIELD_FORMATTER;
						try {
							entity.getDeclaredField(tagName);
							currentTagBelongsToEntity = true;
						} catch (NoSuchFieldException ex) {
							// swallow exception for skipping tag
							currentTagBelongsToEntity = false;
						}
					}
				}
				break;
			case XMLStreamReader.END_ELEMENT:
				tagName = xmlStreamReader.getLocalName();
				position = Utility.searchEntities(entityClassList, tagName);
				if (position != -1) {
					position = Utility.searchRepositories(repositoryClassList, tagName);
					if (position != -1) {
						String xmlFileName = Utility.getXMLFileName(xmlFilePath);
						String profileName = Utility.getFileNameWithoutExtension(xmlFileName);
						Field f = entity.getSuperclass().getDeclaredField(PROFILE_NAME_COLUMN);
						f.setAccessible(true);
						f.set(entityObject, profileName);
						f.setAccessible(false);
						repository = repositoryClassList.get(position);
						repositoryObject = repository.newInstance();
						DAO dao = repository.getDeclaredAnnotation(DAO.class);
						Method m = repository.getDeclaredMethod(CREATE_ENTITY_METHOD_NAME, dao.forEntity());
						if(shouldParse) {
							Object e = m.invoke(repositoryObject, entityObject);
							System.out.println("Persisted " + e + " " + entityObject.getClass().getSimpleName() + " for " + profileName);
						}
					}
				} else {
					currentTagBelongsToEntity = false;
				}
				break;
			case XMLStreamReader.CHARACTERS:
				tagContent = xmlStreamReader.getText();
				if(currentTagBelongsToEntity) {
					try {
						Field f = entity.getDeclaredField(tagName);
						f.setAccessible(true);
						if(f.getType() == Boolean.class) {
							f.set(entityObject, Boolean.valueOf(tagContent));
						} else {
							f.set(entityObject, tagContent);
						}
						f.setAccessible(false);
					} catch (NoSuchFieldException e) {
						// swallow exception for skipping tag content
					}
				}
				break;
			}
			xmlStreamReader.next();
		}
		return propertyNames;
	}

}
