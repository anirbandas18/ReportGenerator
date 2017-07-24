package com.sss.report.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sss.report.core.tags.Key;

@Entity
@Table(name = "field_permissions")
public class FieldPermissionEntity  extends ProfileEntity{
	private Boolean editable;
	@Key(name = "field")
	private String field;
	private Boolean readable;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
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

}
