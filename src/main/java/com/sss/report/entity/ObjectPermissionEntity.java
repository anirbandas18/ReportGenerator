package com.sss.report.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sss.report.core.tags.Key;

@Entity
@Table(name = "object_permissions")
public class ObjectPermissionEntity extends ProfileEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return object + "[allowCreate=" + allowCreate + "/allowRead=" + allowRead + "/allowEdit=" + allowEdit + "/allowDelete="
				+ allowDelete + "/modifyAllRecords=" + modifyAllRecords + "/viewAllrecords=" + viewAllRecords + "]";
	}

	private Boolean allowCreate;
	private Boolean allowRead;
	private Boolean allowEdit;
	private Boolean allowDelete;
	private Boolean modifyAllRecords;
	private Boolean viewAllRecords;
	@Key(name = "object")
	private String object;

	public Boolean getAllowCreate() {
		return allowCreate;
	}

	public void setAllowCreate(Boolean allowCreate) {
		this.allowCreate = allowCreate;
	}

	public Boolean getAllowRead() {
		return allowRead;
	}

	public void setAllowRead(Boolean allowRead) {
		this.allowRead = allowRead;
	}

	public Boolean getAllowEdit() {
		return allowEdit;
	}

	public void setAllowEdit(Boolean allowEdit) {
		this.allowEdit = allowEdit;
	}

	public Boolean getAllowDelete() {
		return allowDelete;
	}

	public void setAllowDelete(Boolean allowDelete) {
		this.allowDelete = allowDelete;
	}

	public Boolean getModifyAllRecords() {
		return modifyAllRecords;
	}

	public void setModifyAllRecords(Boolean modifyAllRecords) {
		this.modifyAllRecords = modifyAllRecords;
	}

	public Boolean getViewAllRecords() {
		return viewAllRecords;
	}

	public void setViewAllRecords(Boolean viewAllRecords) {
		this.viewAllRecords = viewAllRecords;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((object == null) ? 0 : object.hashCode());
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
		ObjectPermissionEntity other = (ObjectPermissionEntity) obj;
		if (object == null) {
			if (other.object != null)
				return false;
		} else if (!object.equals(other.object))
			return false;
		return true;
	}

}
