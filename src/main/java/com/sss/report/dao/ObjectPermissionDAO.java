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
import com.sss.report.entity.ObjectPermissionEntity;
import com.sss.report.util.HibernateUtil;

@DAO(forEntity = ObjectPermissionEntity.class)
public class ObjectPermissionDAO   {

	public Integer create(ObjectPermissionEntity objectPermission) {
		SessionFactory sessionfactory = HibernateUtil.getSessionFactory();
		Session session = sessionfactory.openSession();
		Transaction tx = null;
		Integer id = null;
		try {
			tx = session.beginTransaction();
			id = (Integer) session.save(objectPermission);
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
	
	public List<ObjectPermissionEntity> findByProfile(String profileName) {
		SessionFactory sessionfactory = HibernateUtil.getSessionFactory();
		Session session = sessionfactory.openSession();
		Transaction tx = null;
		List<ObjectPermissionEntity> entities = new ArrayList<>();
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(ObjectPermissionEntity.class);
			ProjectionList projections = Projections.projectionList();
			projections.add(Projections.property("allowCreate"), "allowCreate");
			projections.add(Projections.property("allowRead"), "allowRead");
			projections.add(Projections.property("allowEdit"), "allowEdit");
			projections.add(Projections.property("allowDelete"), "allowDelete");
			projections.add(Projections.property("modifyAllRecords"), "modifyAllRecords");
			projections.add(Projections.property("viewAllRecords"), "viewAllRecords");
			projections.add(Projections.property("object"), "object");
			projections.add(Projections.property("profile"), "profile");
			criteria.setProjection(projections);
			criteria.add(Restrictions.like("profile", profileName));
			criteria.addOrder(Order.asc("object"));
			criteria.setResultTransformer(Transformers.aliasToBean(ObjectPermissionEntity.class));
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
			Criteria criteria = session.createCriteria(ObjectPermissionEntity.class);
			ProjectionList projList = Projections.projectionList();
			projList.add(Projections.property("object"));
			criteria.setProjection(Projections.distinct(projList));
			criteria.addOrder(Order.asc("object"));
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
	
	public List<ObjectPermissionEntity> findByKeyProperty(String key) {
		SessionFactory sessionfactory = HibernateUtil.getSessionFactory();
		Session session = sessionfactory.openSession();
		Transaction tx = null;
		List<ObjectPermissionEntity> entities = new ArrayList<>();
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(ObjectPermissionEntity.class);
			ProjectionList projections = Projections.projectionList();
			projections.add(Projections.property("profile"), "profile");
			projections.add(Projections.property("allowCreate"), "allowCreate");
			projections.add(Projections.property("allowRead"), "allowRead");
			projections.add(Projections.property("allowEdit"), "allowEdit");
			projections.add(Projections.property("allowDelete"), "allowDelete");
			projections.add(Projections.property("modifyAllRecords"), "modifyAllRecords");
			projections.add(Projections.property("viewAllRecords"), "viewAllRecords");
			criteria.setProjection(projections);
			criteria.add(Restrictions.like("object", key));
			criteria.addOrder(Order.asc("profile"));
			criteria.setResultTransformer(Transformers.aliasToBean(ObjectPermissionEntity.class));
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
