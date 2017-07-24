package com.sss.report.metadata.model;

import com.sss.report.core.tags.Mode;

public class ReportMetadata {
	
	private Mode mode;
	private String reportDumpLocation;
	private ProfileMetadata profileMetadata;
	public Mode getMode() {
		return mode;
	}
	public void setMode(Mode mode) {
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
