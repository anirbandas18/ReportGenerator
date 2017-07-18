package com.sss.report.service;

import java.io.BufferedWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.sss.report.core.Constants;
import com.sss.report.dao.FieldPermissionsDAO;
import com.sss.report.entity.FieldPermissionsEntity;

public class CSVService implements Callable<String> {

	private static final String CSV_DELIMITTER = ",";
	
	public static final String CSV_EXTENSION = ".csv";

	private String mode;

	private String header;

	public CSVService(String mode, String header) {
		super();
		this.mode = mode;
		this.header = header;
	}

	@Override
	public String call() throws Exception {
		FieldPermissionsDAO fieldPermissionsDAO = new FieldPermissionsDAO();
		List<FieldPermissionsEntity> fieldPermissions = new ArrayList<>();
		if(mode.equals(Constants.profile.toString())) {
			fieldPermissions = fieldPermissionsDAO.retrieveByProfile(header);
		} else if (mode.equals(Constants.property.toString())) {
			fieldPermissions = fieldPermissionsDAO.retrieveByField(header);
		} 
		StringWriter sw = new StringWriter();
		BufferedWriter bw = new BufferedWriter(sw);
		bw.write(CSV_DELIMITTER + header);
		bw.newLine();
		for (FieldPermissionsEntity fpe : fieldPermissions) {
			String line = "";
			if(mode.equals(Constants.profile.toString())) {
				line = fpe.getField() + CSV_DELIMITTER;
			} else if (mode.equals(Constants.property.toString())) {
				line = fpe.getProfile().toString() + CSV_DELIMITTER;
			} else {
				line = line + CSV_DELIMITTER;
			}
			line = line + fpe.toString();
			bw.write(line);
			bw.newLine();
		}
		bw.flush();
		return sw.toString();
	}

}
