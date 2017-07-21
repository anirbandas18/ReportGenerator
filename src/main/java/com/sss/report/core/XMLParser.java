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

import javax.persistence.Table;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sss.report.entity.ProfileEntity;
import com.sss.report.service.XMLProcessor;

public class XMLParser implements Callable<Set<String>> {

	private String xmlFilePath;
	private XMLStreamReader xmlStreamReader;
	private static final String CREATE_ENTITY_METHOD_NAME = "create";
	private static final String PROFILE_NAME_COLUMN = "profile";
	private static final String FIELD_FORMATTER = "_";
	private List<Class<? extends ProfileEntity>> entityClassList;
	private List<Class<?>> repositoryClassList;
	private Set<String> propertyNames;
	
	
	public XMLParser(String xmlFilePath) {
		this.xmlFilePath = xmlFilePath;
		this.propertyNames = new TreeSet<>();
	}

	private Integer searchEntities(String item) throws InstantiationException, IllegalAccessException {
		Integer low = 0;
		Integer high = entityClassList.size() - 1;
		Integer position = -1;
		item = item.toLowerCase();
		while (low <= high) {
			Integer mid = (high + low) / 2;
			Class<? extends ProfileEntity> midElement = entityClassList.get(mid);
			Table annotation = midElement.getDeclaredAnnotation(Table.class);
			String formattedTableName = Utility.formatTableName(annotation.name());
			if (formattedTableName.compareTo(item) == 0) {
				position = mid;
				propertyNames.add(item);
				break;
			} else if (formattedTableName.compareTo(item) > 0) {
				high = mid - 1;
			} else if (formattedTableName.compareTo(item) < 0) {
				low = mid + 1;
			}
		}
		return position;
	}

	private Integer searchRepositories(String item)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Integer low = 0;
		Integer high = repositoryClassList.size() - 1;
		Integer position = -1;
		item = item.toLowerCase();
		while (low <= high) {
			Integer mid = (high + low) / 2;
			Class<?> midElement = repositoryClassList.get(mid);
			DAO dao = midElement.getDeclaredAnnotation(DAO.class);
			Class<? extends ProfileEntity> entity = dao.forEntity();
			Table annotation = entity.getDeclaredAnnotation(Table.class);
			String formattedTableName = Utility.formatTableName(annotation.name());
			if (formattedTableName.compareTo(item) == 0) {
				position = mid;
				break;
			} else if (formattedTableName.compareTo(item) > 0) {
				high = mid - 1;
			} else if (formattedTableName.compareTo(item) < 0) {
				low = mid + 1;
			}
		}
		return position;
	}

	private void configureParser() throws FileNotFoundException, XMLStreamException {
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		xmlInputFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);
		System.out.println(xmlFilePath);
		InputStream stream = new FileInputStream(xmlFilePath);
		xmlStreamReader = xmlInputFactory.createXMLStreamReader(stream);
		entityClassList = XMLProcessor.getEntityClassList();
		repositoryClassList = XMLProcessor.getRepositoryClassList();
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
				position = searchEntities(tagName);
				if (position != -1) {
					entity = entityClassList.get(position);
					entityObject = entity.newInstance();
					//System.out.println(tagName + " searchEntities");
				} else {
					try {
						entity.getDeclaredField(tagName);
						currentTagBelongsToEntity = true;
						//System.out.println(tagName + " !searchEntities");
					} catch (NoSuchFieldException e) {
						tagName = tagName.split(FIELD_FORMATTER)[0];
						try {
							entity.getDeclaredField(tagName);
							currentTagBelongsToEntity = true;
							System.out.println(tagName + " FIELD_FORMATTER");
						} catch (NoSuchFieldException ex) {
							// swallow exception for skipping tag
							currentTagBelongsToEntity = false;
						}
					}
				}
				break;
			case XMLStreamReader.END_ELEMENT:
				tagName = xmlStreamReader.getLocalName();
				// logger.info("END_ELEMENT : " + tagName);
				position = searchEntities(tagName);
				if (position != -1) {
					//System.out.print(tagName + " searchEntities");
					position = searchRepositories(tagName);
					if (position != -1) {
						//System.out.println(tagName + " searchRepositories");
						String xmlFileName = Utility.getXMLFileName(xmlFilePath);
						String profileName = xmlFileName.substring(0, xmlFileName.lastIndexOf(Utility.getFileNameWithoutExtension(xmlFileName)));
						Field f = entity.getSuperclass().getDeclaredField(PROFILE_NAME_COLUMN);
						f.setAccessible(true);
						f.set(entityObject, profileName);
						f.setAccessible(false);
						repository = repositoryClassList.get(position);
						repositoryObject = repository.newInstance();
						DAO dao = repository.getDeclaredAnnotation(DAO.class);
						Method m = repository.getDeclaredMethod(CREATE_ENTITY_METHOD_NAME, dao.forEntity());
						Object e = m.invoke(repositoryObject, entityObject);
						System.out.println("Persisted " + e + " " + entityObject.getClass().getSimpleName() + " for " + profileName);
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
						//System.out.println(tagContent + " declaredField");
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
