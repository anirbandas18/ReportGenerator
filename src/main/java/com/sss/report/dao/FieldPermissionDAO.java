package com.sss.report.dao;

import java.util.ArrayList;
import java.util.List;

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

import com.sss.report.core.tags.DAO;
import com.sss.report.entity.FieldPermissionEntity;
import com.sss.report.util.HibernateUtil;

@DAO(forEntity = FieldPermissionEntity.class)
public class FieldPermissionDAO  {

	public Integer create(FieldPermissionEntity fieldPermission) {
		SessionFactory sessionfactory = HibernateUtil.getSessionFactory();
		Session session = sessionfactory.openSession();
		Transaction tx = null;
		Integer id = null;
		try {
			tx = session.beginTransaction();
			id = (Integer) session.save(fieldPermission);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			if(session.isOpen()) {
				session.close();
			}
		}
		return id;
	}
	
	public List<FieldPermissionEntity> findByProfile(String profileName) {
		SessionFactory sessionfactory = HibernateUtil.getSessionFactory();
		Session session = sessionfactory.openSession();
		Transaction tx = null;
		List<FieldPermissionEntity> entities = new ArrayList<>();
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(FieldPermissionEntity.class);
			ProjectionList projections = Projections.projectionList();
			projections.add(Projections.property("editable"), "editable");
			projections.add(Projections.property("field"), "field");
			projections.add(Projections.property("readable"), "readable");
			criteria.setProjection(projections);
			criteria.add(Restrictions.like("profile", profileName));
			criteria.addOrder(Order.asc("field"));
			criteria.setResultTransformer(Transformers.aliasToBean(FieldPermissionEntity.class));
			entities = criteria.list();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			if(session.isOpen()) {
				session.close();
			}
		}
		return entities;
	}
	
	public List<String> findAllDistinct() {
		SessionFactory sessionfactory = HibernateUtil.getSessionFactory();
		Session session = sessionfactory.openSession();
		Transaction tx = null;
		List<String> distinct = new ArrayList<>();
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(FieldPermissionEntity.class);
			ProjectionList projList = Projections.projectionList();
			projList.add(Projections.property("field"));
			criteria.setProjection(Projections.distinct(projList));
			criteria.addOrder(Order.asc("field"));
			distinct = criteria.list();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			if(session.isOpen()) {
				session.close();
			}
		}
		return distinct;
	}
	
	public List<FieldPermissionEntity> findByKeyProperty(String key) {
		SessionFactory sessionfactory = HibernateUtil.getSessionFactory();
		Session session = sessionfactory.openSession();
		Transaction tx = null;
		List<FieldPermissionEntity> entities = new ArrayList<>();
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(FieldPermissionEntity.class);
			ProjectionList projections = Projections.projectionList();
			projections.add(Projections.property("profile"), "profile");
			projections.add(Projections.property("readable"), "readable");
			projections.add(Projections.property("editable"), "editable");
			criteria.setProjection(projections);
			criteria.add(Restrictions.like("field", key));
			criteria.addOrder(Order.asc("profile"));
			criteria.setResultTransformer(Transformers.aliasToBean(FieldPermissionEntity.class));
			entities = criteria.list();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			if(session.isOpen()) {
				session.close();
			}
		}
		return entities;
	}
	
	
}
