package com.sss.report.service;

import java.io.BufferedWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.concurrent.Callable;

import com.sss.report.core.Constants;
import com.sss.report.core.Utility;
import com.sss.report.dao.FieldPermissionsDAO;
import com.sss.report.entity.TabVisibilitiesEntity;
import com.sss.report.entity.FieldPermissionsEntity;

public class CSVService implements Callable<String> {

	private List<Object> data;

	private String header;
	
	private Constants property;

	public CSVService(String header, List<Object> data, Constants property) {
		super();
		this.header = header;
		this.data = data;
		this.property = property;
	}

	@Override
	public String call() throws Exception {
		/*FieldPermissionsDAO fieldPermissionsDAO = new FieldPermissionsDAO();
		List<FieldPermissionsEntity> fieldPermissions = new ArrayList<>();
		if(mode.equals(Constants.profile.toString())) {
			fieldPermissions = fieldPermissionsDAO.retrieveByProfile(header);
		} else if (mode.equals(Constants.property.toString())) {
			fieldPermissions = fieldPermissionsDAO.retrieveByField(header);
		} */
		StringWriter sw = new StringWriter();
		BufferedWriter bw = new BufferedWriter(sw);
		bw.write(Utility.CSV_DELIMITTER + header);
		bw.newLine();
		for(Object object : data) {
			switch(property) {
			case fieldPermissions :
				FieldPermissionsEntity fieldPermissions = (FieldPermissionsEntity) object;
				bw.write(fieldPermissions.getField() + Utility.CSV_DELIMITTER + fieldPermissions.toString());
				bw.newLine();
				break;
			case tabVisibilities:
				TabVisibilitiesEntity tabVisibilities = (TabVisibilitiesEntity) object;
				bw.write(tabVisibilities.getTab() + Utility.CSV_DELIMITTER + tabVisibilities.toString());
				bw.newLine();
				break;
			default:
				break;
			}
		}
		/*for (FieldPermissionsEntity fpe : fieldPermissions) {
			String line = "";
			if(mode.equals(Constants.profile.toString())) {
				line = fpe.getField() + Utility.CSV_DELIMITTER;
			} else if (mode.equals(Constants.property.toString())) {
				line = fpe.getProfile().toString() + Utility.CSV_DELIMITTER;
			} else {
				line = line + Utility.CSV_DELIMITTER;
			}
			line = line + fpe.toString();
			bw.write(line);
			bw.newLine();
		}*/
		bw.flush();
		return sw.toString();
	}

}
