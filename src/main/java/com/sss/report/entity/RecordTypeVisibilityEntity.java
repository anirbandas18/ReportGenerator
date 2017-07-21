package com.sss.report.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "record_type_visibilities")    
public class RecordTypeVisibilityEntity extends ProfileEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	private Boolean default_;
	private Boolean visible;
	private String recordType;
	private Boolean personAccountDefault;
	
	public Boolean getPersonAccountDefault() {
		return personAccountDefault;
	}
	public void setPersonAccountDefault(Boolean personAccountDefault) {
		this.personAccountDefault = personAccountDefault;
	}
	public Boolean getDefault_() {
		return default_;
	}
	public void setDefault_(Boolean default_) {
		this.default_ = default_;
	}
	public Boolean getVisible() {
		return visible;
	}
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
	public String getRecordType() {
		return recordType;
	}
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
	@Override
	public String toString() {
		return "default=" + default_ + "/visible=" + visible + "/personAccountDefault=" + personAccountDefault;
	}
	
	
	
}
