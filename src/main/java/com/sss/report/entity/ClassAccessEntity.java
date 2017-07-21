package com.sss.report.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "class_accesses")
public class ClassAccessEntity extends ProfileEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	private String apexClass;
	private Boolean enabled;
	
	public String getApexClass() {
		return apexClass;
	}
	public void setApexClass(String apexClass) {
		this.apexClass = apexClass;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	@Override
	public String toString() {
		return "enabled=" + enabled;
	}
	
	
	
}
