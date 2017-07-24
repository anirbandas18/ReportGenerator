package com.sss.report.metadata.model;

public class DirectoryMetadata {
	
	private String mode;
	private String parentLocation;
	private String propertyName;
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getParentLocation() {
		return parentLocation;
	}
	public void setParentLocation(String parentLocation) {
		this.parentLocation = parentLocation;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	@Override
	public String toString() {
		return "DirectoryMetadata [mode=" + mode + ", parentLocation=" + parentLocation + ", propertyName="
				+ propertyName + "]";
	}
	
	

}
