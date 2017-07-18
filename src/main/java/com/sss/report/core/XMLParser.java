package com.sss.report.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sss.report.dao.FieldPermissionsDAO;
import com.sss.report.entity.FieldPermissionsEntity;

public class XMLParser implements Callable<List<FieldPermissionsEntity>> {
	
	private String xmlFilePath;

	private XMLStreamReader xmlStreamReader;
	
	public XMLParser(String xmlFilePath) {
		this.xmlFilePath = xmlFilePath;
	}

	private Boolean isFieldPermissions(String name) {
		String target = Constants.fieldPermissions.toString();
		return name.equalsIgnoreCase(target);
	}

	
	private void configureXMLParser() throws IllegalArgumentException, FileNotFoundException, FactoryConfigurationError, XMLStreamException{
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		xmlInputFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);
		InputStream xmlInputStream = new FileInputStream(xmlFilePath);
		xmlStreamReader = xmlInputFactory.createXMLStreamReader(xmlInputStream) ;
	}
	
	private String getXMLFileName() {
		String xmlFileName = xmlFilePath.substring(xmlFilePath.lastIndexOf(File.separatorChar) + 1);
		return xmlFileName;
	}
	
	@Override
	public List<FieldPermissionsEntity> call() throws Exception {
		// TODO Auto-generated method stub
		configureXMLParser();
		List<FieldPermissionsEntity> fieldPermissionsSet = new ArrayList<>();
		Boolean relatedToFieldPermissions = Boolean.FALSE;
		FieldPermissionsDAO fieldPermissionsDAO = new FieldPermissionsDAO();
		FieldPermissionsEntity fieldPermissions = new FieldPermissionsEntity();
		String name = "";
		while (xmlStreamReader.hasNext()) {
			switch (xmlStreamReader.getEventType()) {
			case XMLStreamReader.START_ELEMENT:
				name = xmlStreamReader.getLocalName();
				if(isFieldPermissions(name)) {
					fieldPermissions = new FieldPermissionsEntity();
					relatedToFieldPermissions = Boolean.TRUE;
				} else {
					try {
						FieldPermissionsEntity.class.getDeclaredField(name);
						relatedToFieldPermissions = Boolean.TRUE;
					} catch (NoSuchFieldException e) {
						relatedToFieldPermissions = Boolean.FALSE;
					} 
				}
				break;
			case XMLStreamReader.END_ELEMENT:
				name = xmlStreamReader.getLocalName();
				if(isFieldPermissions(name)) {
					fieldPermissions.setProfile(getXMLFileName());
					Integer id = fieldPermissionsDAO.create(fieldPermissions);
					System.out.println(id + " " + fieldPermissions.getField());
					fieldPermissionsSet.add(fieldPermissions);
					relatedToFieldPermissions = Boolean.FALSE;
				}
				break;
			case XMLStreamReader.CHARACTERS:
				String value = xmlStreamReader.getText();
				if (relatedToFieldPermissions && value.trim().length() != 0) {
					Field field = FieldPermissionsEntity.class.getDeclaredField(name);
					field.setAccessible(true);
					try {
						field.set(fieldPermissions, value.toString());
					} catch (IllegalArgumentException e) {
						field.set(fieldPermissions, Boolean.valueOf(value));
					}
					field.setAccessible(false);
				}
				break;
			}
			xmlStreamReader.next();
		}
		xmlStreamReader.close();
		return fieldPermissionsSet;
	}

}
