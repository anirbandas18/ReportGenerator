package com.sss.report.core;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.xml.stream.XMLStreamReader;

import com.sss.report.entity.FieldPermissionsEntity;
import com.sss.report.entity.ProfileEntity;

public class XMLParser implements Callable<Set<FieldPermissionsEntity>> {

	private XMLStreamReader xmlStreamReader;

	private  ProfileEntity profile;

	public XMLParser(XMLStreamReader xmlStreamReader, ProfileEntity profile) {
		this.xmlStreamReader = xmlStreamReader;
		this.profile = profile;
	}

	private Boolean isFieldPermissions(String name) {
		String target = Constants.fieldPermissions.toString();
		return name.equalsIgnoreCase(target);
	}

	@Override
	public Set<FieldPermissionsEntity> call() throws Exception {
		// TODO Auto-generated method stub
		Set<FieldPermissionsEntity> fieldPermissionsSet = new LinkedHashSet<>();
		Boolean relatedToFieldPermissions = Boolean.FALSE;
		FieldPermissionsEntity fieldPermissions = new FieldPermissionsEntity();
		String name = "";
		while (xmlStreamReader.hasNext()) {
			switch (xmlStreamReader.next()) {
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
					fieldPermissions.setProfile(profile);
					fieldPermissionsSet.add(fieldPermissions);
					relatedToFieldPermissions = Boolean.FALSE;
				}
				break;
			case XMLStreamReader.CHARACTERS:
				String value = xmlStreamReader.getText();
				if (relatedToFieldPermissions && value.trim().length() != 0) {
					//System.out.println(name + " = " + value);
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
		}
		xmlStreamReader.close();
		return fieldPermissionsSet;
	}

}
