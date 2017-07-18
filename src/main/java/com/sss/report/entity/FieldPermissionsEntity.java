package com.sss.report.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "field_permissions")
public class FieldPermissionsEntity implements Comparable<FieldPermissionsEntity>{
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "name", nullable = false)
	private ProfileEntity profile;
	private Boolean editable;
	private String field;
	private Boolean readable;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Boolean getReadable() {
		return readable;
	}

	@Override
	public String toString() {
		return "editable=" + editable + "/readable=" + readable;
	}

	public void setReadable(Boolean readable) {
		this.readable = readable;
	}

	public ProfileEntity getProfile() {
		return profile;
	}

	public void setProfile(ProfileEntity profile) {
		this.profile = profile;
	}

	@Override
	public int compareTo(FieldPermissionsEntity o) {
		Integer x = this.profile.getName().compareTo(o.getProfile().getName());
		Integer y = this.field.compareTo(o.getField());
		return Integer.compare(x, y);
	}

}
