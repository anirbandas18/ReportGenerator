package com.sss.report.service;

import java.io.BufferedWriter;
import java.io.StringWriter;
import java.util.Set;
import java.util.concurrent.Callable;

import com.sss.report.core.Constants;
import com.sss.report.entity.FieldPermissionsEntity;

public class CSVService implements Callable<String> {

	private static final String CSV_DELIMITTER = ",";
	
	public static final String CSV_EXTENSION = ".csv";

	private String mode;

	private String header;

	private Set<FieldPermissionsEntity> fieldPermissions;

	public CSVService(String mode, String header, Set<FieldPermissionsEntity> fieldPermissions) {
		super();
		this.mode = mode;
		this.header = header;
		this.fieldPermissions = fieldPermissions;
	}

	@Override
	public String call() throws Exception {
		StringWriter sw = new StringWriter();
		BufferedWriter bw = new BufferedWriter(sw);
		bw.write(CSV_DELIMITTER + header);
		bw.newLine();
		for (FieldPermissionsEntity fpe : fieldPermissions) {
			/*String line = mode.equals(Constants.profile.toString()) ? fpe.toString()
					: mode.equals(Constants.property.toString()) ? fpe.getProfile().toString() : CSV_DELIMITTER;
			bw.write(line + CSV_DELIMITTER);*/
			String line = "";
			if(mode.equals(Constants.profile.toString())) {
				line = fpe.getField();
			} else if (mode.equals(Constants.property.toString())) {
				line = fpe.getProfile().toString();
			} else {
				line = line + CSV_DELIMITTER;
			}
			line = line + CSV_DELIMITTER + fpe.toString();
			bw.write(line);
			bw.newLine();
		}
		bw.flush();
		return sw.toString();
	}

}
