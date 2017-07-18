package com.sss.report.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.sss.report.core.HibernateUtil;
import com.sss.report.entity.FieldPermissionsEntity;

public class FieldPermissionsDAO {
	
	public Integer create(FieldPermissionsEntity fieldPermissions) {
		SessionFactory sessionfactory = HibernateUtil.getSessionFactory();
		Session session = sessionfactory.getCurrentSession();
		Transaction tx = null;
		Integer id = null;
		try {
			tx = session.beginTransaction();
			id = (Integer) session.save(fieldPermissions);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			System.out.println(e.getClass() + " " + e.getMessage());
		} finally {
			if(session.isOpen()) {
				session.close();
			}
		}
		return id;
	}
	
	public Set<String> retrieveUniqueFields() {
		SessionFactory sessionfactory = HibernateUtil.getSessionFactory();
		Session session = sessionfactory.getCurrentSession();
		Transaction tx = null;
		Set<String> fieldPermissions = new TreeSet<>();;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(FieldPermissionsEntity.class);
			ProjectionList projList = Projections.projectionList();
			projList.add(Projections.property("field"));
			criteria.setProjection(Projections.distinct(projList));
			criteria.addOrder(Order.asc("field"));
			List<String> fieldPermissionsList = criteria.list();
			fieldPermissions.addAll(fieldPermissionsList);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			System.out.println(e.getClass() + " " + e.getMessage());
			e.printStackTrace();
		} finally {
			if(session.isOpen()) {
				session.close();
			}
		}
		return fieldPermissions;
	}
	
	public Set<String> retrieveUniqueProfiles() {
		SessionFactory sessionfactory = HibernateUtil.getSessionFactory();
		Session session = sessionfactory.getCurrentSession();
		Transaction tx = null;
		Set<String> fieldPermissions = new TreeSet<>();;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(FieldPermissionsEntity.class);
			ProjectionList projList = Projections.projectionList();
			projList.add(Projections.property("profile"));
			criteria.setProjection(Projections.distinct(projList));
			criteria.addOrder(Order.asc("profile"));
			List<String> fieldPermissionsList = criteria.list();
			fieldPermissions.addAll(fieldPermissionsList);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			System.out.println(e.getClass() + " " + e.getMessage());
			e.printStackTrace();
		} finally {
			if(session.isOpen()) {
				session.close();
			}
		}
		return fieldPermissions;
	}
	
	public List<FieldPermissionsEntity> retrieveByProfile(String profileName) {
		SessionFactory sessionfactory = HibernateUtil.getSessionFactory();
		Session session = sessionfactory.getCurrentSession();
		Transaction tx = null;
		List<FieldPermissionsEntity> fieldPermissionsList = new ArrayList<>();
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(FieldPermissionsEntity.class);
			ProjectionList projList = Projections.projectionList();
			projList.add(Projections.property("editable"), "editable");
			projList.add(Projections.property("readable"), "readable");
			projList.add(Projections.property("field"), "field");
			criteria.setProjection(projList);
			criteria.add(Restrictions.eq("profile", profileName));
			criteria.addOrder(Order.asc("field"));
			criteria.setResultTransformer(Transformers.aliasToBean(FieldPermissionsEntity.class));
			fieldPermissionsList = criteria.list();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			System.out.println(e.getClass() + " " + e.getMessage());
			e.printStackTrace();
		} finally {
			if(session.isOpen()) {
				session.close();
			}
		}
		return fieldPermissionsList;
	}
	
	public List<FieldPermissionsEntity> retrieveByField(String fieldName) {
		SessionFactory sessionfactory = HibernateUtil.getSessionFactory();
		Session session = sessionfactory.getCurrentSession();
		Transaction tx = null;
		List<FieldPermissionsEntity> fieldPermissionsList = new ArrayList<>();
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(FieldPermissionsEntity.class);
			ProjectionList projList = Projections.projectionList();
			projList.add(Projections.property("editable"), "editable");
			projList.add(Projections.property("readable"), "readable");
			projList.add(Projections.property("profile"), "profile");
			criteria.setProjection(projList);
			criteria.add(Restrictions.eq("field", fieldName));
			criteria.addOrder(Order.asc("profile"));
			criteria.setResultTransformer(Transformers.aliasToBean(FieldPermissionsEntity.class));
			fieldPermissionsList = criteria.list();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			System.out.println(e.getClass() + " " + e.getMessage());
			e.printStackTrace();
		} finally {
			if(session.isOpen()) {
				session.close();
			}
		}
		return fieldPermissionsList;
	}
}
