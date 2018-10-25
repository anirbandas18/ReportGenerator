package com.sss.report.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sss.report.core.tags.Field;
import com.sss.report.core.tags.Key;
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
	@Key(name = "apexClass")
	private String apexClass;
	@Field(isShort = true)
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apexClass == null) ? 0 : apexClass.hashCode());
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
		ClassAccessEntity other = (ClassAccessEntity) obj;
		if (apexClass == null) {
			if (other.apexClass != null)
				return false;
		} else if (!apexClass.equals(other.apexClass))
			return false;
		return true;
	}
	
	
	
}
