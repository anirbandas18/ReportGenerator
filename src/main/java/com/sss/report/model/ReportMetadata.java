package com.sss.report.model;

public class ReportMetadata {
	
	private String mode;
	private String reportDumpLocation;
	private ProfileMetadata profileMetadata;
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getReportDumpLocation() {
		return reportDumpLocation;
	}
	public void setReportDumpLocation(String reportDumpLocation) {
		this.reportDumpLocation = reportDumpLocation;
	}
	public ProfileMetadata getProfileMetadata() {
		return profileMetadata;
	}
	public void setProfileMetadata(ProfileMetadata profileMetadata) {
		this.profileMetadata = profileMetadata;
	}
	@Override
	public String toString() {
		return "ReportMetadata [mode=" + mode + ", reportDumpLocation=" + reportDumpLocation + ", profileMetadata="
				+ profileMetadata + "]";
	}
	

}
