package com.sss.report.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sss.report.core.tags.Field;
import com.sss.report.core.tags.Key;

@Entity
@Table(name = "field_permissions")
public class FieldPermissionEntity  extends ProfileEntity{
	@Field(isShort = true)
	private Boolean editable;
	@Key(name = "field")
	private String field;
	@Field(isShort = true)
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((field == null) ? 0 : field.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FieldPermissionEntity other = (FieldPermissionEntity) obj;
		if (field == null) {
			if (other.field != null)
				return false;
		} else if (!field.equals(other.field))
			return false;
		return true;
	}

	
	
}
