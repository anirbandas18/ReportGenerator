package com.sss.report.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "page_accesses")
public class PageAccessEntity extends ProfileEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	private String apexPage;
	private String enabled;
	public String getApexPage() {
		return apexPage;
	}
	public void setApexPage(String apexPage) {
		this.apexPage = apexPage;
	}
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	@Override
	public String toString() {
		return "enabled=" + enabled;
	}
	
	
	
}
