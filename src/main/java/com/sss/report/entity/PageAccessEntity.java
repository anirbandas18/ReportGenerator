package com.sss.report.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sss.report.core.tags.Field;
import com.sss.report.core.tags.Key;

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
	@Key(name = "apexPage")
	private String apexPage;
	@Field(isShort = true)
	private Boolean enabled;
	public String getApexPage() {
		return apexPage;
	}
	public void setApexPage(String apexPage) {
		this.apexPage = apexPage;
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apexPage == null) ? 0 : apexPage.hashCode());
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
		PageAccessEntity other = (PageAccessEntity) obj;
		if (apexPage == null) {
			if (other.apexPage != null)
				return false;
		} else if (!apexPage.equals(other.apexPage))
			return false;
		return true;
	}
	
	
	
}
