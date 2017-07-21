package com.sss.report.entity;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class ProfileEntity implements Comparable<ProfileEntity> {
	private String profile;

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	@Override
	public int compareTo(ProfileEntity pe) {
		return this.profile.compareTo(pe.getProfile());
	}

	@Override
	public String toString() {
		return "profile=" + profile;
	}

}
