package com.sss.report.core;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.sss.report.entity.FieldPermissionsEntity;

public class HibernateUtil {

	private static SessionFactory sessionFactory;

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void create() {
		String hibernateConfigFilePath = "hibernate-create.cfg.xml";
		System.out.println("Creating database from : " + hibernateConfigFilePath);
		buildSessionFactory(hibernateConfigFilePath);
	}

	private static void buildSessionFactory(String hibernateConfigFilePath) {
		try {
			Configuration configuration = new Configuration();
			configuration.configure(hibernateConfigFilePath);
			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties())
					.build();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static void shutdown() {
		sessionFactory.close();
		String hibernateConfigFilePath = "hibernate-shutdown.cfg.xml";
		System.out.println("Shutting down database from : " + hibernateConfigFilePath);
		buildSessionFactory(hibernateConfigFilePath);
		sessionFactory.close();
	}

}
