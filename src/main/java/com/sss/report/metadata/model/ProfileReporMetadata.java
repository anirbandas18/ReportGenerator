package com.sss.report.metadata.model;

import java.util.List;

public class ProfileReporMetadata {
	
	private String parentReportDumpLocation;
	private List<String> profileNames;
	public String getParentReportDumpLocation() {
		return parentReportDumpLocation;
	}
	public void setParentReportDumpLocation(String parentReportDumpLocation) {
		this.parentReportDumpLocation = parentReportDumpLocation;
	}
	public List<String> getProfileNames() {
		return profileNames;
	}
	public void setProfileNames(List<String> profileNames) {
		this.profileNames = profileNames;
	}
	@Override
	public String toString() {
		return "ProfileReporMetadata [parentReportDumpLocation=" + parentReportDumpLocation + ", profileNames="
				+ profileNames + "]";
	}
	

}
