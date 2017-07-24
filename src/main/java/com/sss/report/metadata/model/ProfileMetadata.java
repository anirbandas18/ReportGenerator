package com.sss.report.metadata.model;

import java.util.Set;

public class ProfileMetadata {
	
	private Set<String> profileNames;
	private Set<String> profileProperties;
	public Set<String> getProfileNames() {
		return profileNames;
	}
	public void setProfileNames(Set<String> profileNames) {
		this.profileNames = profileNames;
	}
	public Set<String> getProfileProperties() {
		return profileProperties;
	}
	public void setProfileProperties(Set<String> profileProperties) {
		this.profileProperties = profileProperties;
	}
	@Override
	public String toString() {
		return "ProfileMetadata [profileNames=" + profileNames + ", profileProperties=" + profileProperties + "]";
	}
	
	

}
