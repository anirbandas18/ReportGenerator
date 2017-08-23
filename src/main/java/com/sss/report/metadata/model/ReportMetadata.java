package com.sss.report.metadata.model;

import java.util.Set;

import com.sss.report.core.tags.Mode;

public class ReportMetadata {
	
	private Set<String> filter;
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
	
	public Set<String> getFilter() {
		return filter;
	}
	public void setFilter(Set<String> filter) {
		this.filter = filter;
	}
	@Override
	public String toString() {
		return "ReportMetadata [filter=" + filter + ", mode=" + mode + ", reportDumpLocation=" + reportDumpLocation
				+ ", profileMetadata=" + profileMetadata + "]";
	}
	

}
