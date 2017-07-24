package com.sss.report.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sss.report.core.tags.Key;

@Entity
@Table(name = "object_permissions")
public class ObjectPermissionEntity extends ProfileEntity{
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
		return "allowCreate=" + allowCreate + "/allowRead=" + allowRead + "/allowEdit=" + allowEdit + "/allowDelete="
				+ allowDelete + "/modifyAllRecords=" + modifyAllRecords + "/viewAllrecords=" + viewAllRecords;
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

}
