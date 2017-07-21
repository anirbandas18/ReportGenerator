package com.sss.report.core;
/*package com.sss.report.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sss.report.dao.FieldPermissionsDAO;
import com.sss.report.dao.TabVisibilitiesDAO;
import com.sss.report.entity.FieldPermissionsEntity;
import com.sss.report.entity.TabVisibilitiesEntity;
import com.sss.report.model.ProfileMetadata;

public class XMLParser implements Callable<ProfileMetadata> {
	
	private String xmlFilePath;

	private XMLStreamReader xmlStreamReader;
	
	public XMLParser(String xmlFilePath) {
		this.xmlFilePath = xmlFilePath;
	}

	private Boolean isFieldPermissions(String name) {
		String target = Constants.fieldPermissions.toString();
		return name.equalsIgnoreCase(target);
	}

	private Boolean isTabVisibilities(String name) {
		String target = Constants.tabVisibilities.toString();
		return name.equalsIgnoreCase(target);
	}
	
	private void configureXMLParser() throws IllegalArgumentException, FileNotFoundException, FactoryConfigurationError, XMLStreamException{
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		xmlInputFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);
		InputStream xmlInputStream = new FileInputStream(xmlFilePath);
		xmlStreamReader = xmlInputFactory.createXMLStreamReader(xmlInputStream) ;
	}
	
	@Override
	public ProfileMetadata call() throws Exception {
		// TODO Auto-generated method stub
		configureXMLParser();
		Set<Constants> properties = new TreeSet<>();
		Boolean relatedToFieldPermissions = Boolean.FALSE;
		Boolean relatedToTabVisibilities = Boolean.FALSE;
		FieldPermissionsDAO fieldPermissionsDAO = new FieldPermissionsDAO();
		FieldPermissionsEntity fieldPermissions = new FieldPermissionsEntity();
		TabVisibilitiesEntity tabVisibilities = new TabVisibilitiesEntity();
		TabVisibilitiesDAO tabVisibilitiesDAO = new TabVisibilitiesDAO();
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
				if(isTabVisibilities(name)) {
					tabVisibilities = new TabVisibilitiesEntity();
					relatedToTabVisibilities = Boolean.TRUE;
				} else {
					try {
						TabVisibilitiesEntity.class.getDeclaredField(name);
						relatedToTabVisibilities = Boolean.TRUE;
					} catch (NoSuchFieldException e) {
						relatedToTabVisibilities = Boolean.FALSE;
					}
				}
				break;
			case XMLStreamReader.END_ELEMENT:
				name = xmlStreamReader.getLocalName();
				if(isFieldPermissions(name)) {
					fieldPermissions.setProfile(Utility.getXMLFileName(xmlFilePath));
					Integer id = fieldPermissionsDAO.create(fieldPermissions);
					System.out.println(id + " " + fieldPermissions.getField());
					properties.add(Constants.fieldPermissions);
					relatedToFieldPermissions = Boolean.FALSE;
				}
				if(isTabVisibilities(name)) {
					tabVisibilities.setProfile(Utility.getXMLFileName(xmlFilePath));
					Integer id = tabVisibilitiesDAO.create(tabVisibilities);
					System.out.println(id + " " + tabVisibilities.getTab());
					properties.add(Constants.tabVisibilities);
					relatedToTabVisibilities = Boolean.FALSE;
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
				if (relatedToTabVisibilities && value.trim().length() != 0) {
					Field field = TabVisibilitiesEntity.class.getDeclaredField(name);
					field.setAccessible(true);
					field.set(tabVisibilities, value.toString());
					field.setAccessible(false);
				}
				break;
			}
			xmlStreamReader.next();
		}
		xmlStreamReader.close();
		ProfileMetadata profileMetadata = new ProfileMetadata();
		profileMetadata.setName(Utility.getXMLFileName(xmlFilePath));
		profileMetadata.setProperties(properties);
		return profileMetadata;
	}

}
*/