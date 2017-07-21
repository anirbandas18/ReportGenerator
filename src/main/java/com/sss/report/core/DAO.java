package com.sss.report.core;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.sss.report.entity.ProfileEntity;

@Retention(RUNTIME)
@Target(TYPE)
public @interface DAO {

	public Class<? extends ProfileEntity> forEntity();
	
}
