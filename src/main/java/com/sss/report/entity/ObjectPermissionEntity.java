package com.sss.report.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
				+ allowDelete + "/modifyRecords=" + modifyRecords + "/viewAllrecords=" + viewAllrecords;
	}
	private Boolean allowCreate;
	private Boolean allowRead;
	private Boolean allowEdit;
	private Boolean allowDelete;
	private Boolean modifyRecords;
	private Boolean viewAllrecords;
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
	public Boolean getModifyRecords() {
		return modifyRecords;
	}
	public void setModifyRecords(Boolean modifyRecords) {
		this.modifyRecords = modifyRecords;
	}
	public Boolean getViewAllrecords() {
		return viewAllrecords;
	}
	public void setViewAllrecords(Boolean viewAllrecords) {
		this.viewAllrecords = viewAllrecords;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}

}
