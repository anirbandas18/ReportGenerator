package com.sss.report.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sss.report.core.Constants;
import com.sss.report.dao.FieldPermissionsDAO;
import com.sss.report.dao.TabVisibilitiesDAO;
import com.sss.report.entity.FieldPermissionsEntity;
import com.sss.report.entity.TabVisibilitiesEntity;

public class ProfileService implements Callable<Long> {
	
	private String profileName;
	
	private Set<Constants> properties;
	
	private Map<Constants,String> reportPaths;
	
	public ProfileService(String profileName, Set<Constants> properties, Map<Constants,String> reportPaths) {
		this.profileName = profileName;
		this.reportPaths = reportPaths;
		this.properties = properties;
	}

	@Override
	public Long call() throws Exception {
		FieldPermissionsDAO fieldPermissionsDAO = new FieldPermissionsDAO();
		TabVisibilitiesDAO tabVisibilitiesDAO = new TabVisibilitiesDAO();
		ExecutorService threadPool = Executors.newCachedThreadPool();
		List<Object> data = new ArrayList<>();
		for(Constants property : properties) {
			switch(property) {
			case fieldPermissions :
				data = (List<Object>) fieldPermissionsDAO.retrieveByProfile(profileName);
				break;
			case tabVisibilities : 
				List<TabVisibilitiesEntity> tabVisibilitiesList = tabVisibilitiesDAO.retrieveByProfile(profileName);
				break;
			default :
				break;
			}
			CSVService csvService = new CSVService(profileName, data, property);
		}
		return null;
	}

}
