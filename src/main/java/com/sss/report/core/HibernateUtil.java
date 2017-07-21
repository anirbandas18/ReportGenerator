package com.sss.report.core;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {

	private static SessionFactory sessionFactory;
	
	private static Boolean createDB;

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void start(Boolean createDB) {
		HibernateUtil.createDB = createDB;
		String hibernateConfigFilePath = "hibernate-derby-create.cfg.xml";// "hibernate-mysql.cfg.xml"  "hibernate-derby-create.cfg.xml";
		System.out.println("Creating database from : " + hibernateConfigFilePath);
		buildSessionFactory(hibernateConfigFilePath);
	}

	private static void buildSessionFactory(String hibernateConfigFilePath) {
		try {
			Configuration configuration = new Configuration();
			configuration.configure(hibernateConfigFilePath);
			if(createDB) {
				configuration.setProperty("hibernate.hbm2ddl.auto", "create");
			}
			/*configuration.addAnnotatedClass(ClassAccessEntity.class);
			configuration.addAnnotatedClass(FieldPermissionEntity.class);
			configuration.addAnnotatedClass(LayoutAssignmentEntity.class);
			configuration.addAnnotatedClass(ObjectPermissionEntity.class);
			configuration.addAnnotatedClass(PageAccessEntity.class);
			configuration.addAnnotatedClass(ProfileEntity.class);
			configuration.addAnnotatedClass(RecordTypeVisibilityEntity.class);
			configuration.addAnnotatedClass(TabVisibilityEntity.class);
			configuration.addAnnotatedClass(UserPermissionEntity.class);*/
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
		String hibernateConfigFilePath = "hibernate-derby-shutdown.cfg.xml";
		System.out.println("Shutting down database from : " + hibernateConfigFilePath);
		buildSessionFactory(hibernateConfigFilePath);
		sessionFactory.close();
	}

}
