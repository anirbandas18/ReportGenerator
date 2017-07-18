package com.sss.report.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "field_permissions")
public class FieldPermissionsEntity /*implements Comparable<FieldPermissionsEntity>*/{
	private Boolean editable;
	private String field;
	private Boolean readable;
	private String profile;
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

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/*@Override
	public int compareTo(FieldPermissionsEntity o) {
		Integer x = this.profile.getName().compareTo(o.getProfile().getName());
		Integer y = this.field.compareTo(o.getField());
		return Integer.compare(x, y);
	}
*/
}
