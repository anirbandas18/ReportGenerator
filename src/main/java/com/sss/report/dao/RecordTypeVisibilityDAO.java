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
import com.sss.report.entity.RecordTypeVisibilityEntity;
import com.sss.report.util.HibernateUtil;;

@DAO(forEntity = RecordTypeVisibilityEntity.class)
public class RecordTypeVisibilityDAO   {
	
	public Integer create(RecordTypeVisibilityEntity recordTypeVisibility) {
		SessionFactory sessionfactory = HibernateUtil.getSessionFactory();
		Session session = sessionfactory.openSession();
		Transaction tx = null;
		Integer id = null;
		try {
			tx = session.beginTransaction();
			id = (Integer) session.save(recordTypeVisibility);
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
	
	public List<RecordTypeVisibilityEntity> findByProfile(String profileName) {
		SessionFactory sessionfactory = HibernateUtil.getSessionFactory();
		Session session = sessionfactory.openSession();
		Transaction tx = null;
		List<RecordTypeVisibilityEntity> entities = new ArrayList<>();
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(RecordTypeVisibilityEntity.class);
			ProjectionList projections = Projections.projectionList();
			projections.add(Projections.property("default_"), "default_");
			projections.add(Projections.property("visible"), "visible");
			projections.add(Projections.property("recordType"), "recordType");
			criteria.setProjection(projections);
			criteria.add(Restrictions.like("profile", profileName));
			criteria.addOrder(Order.asc("recordType"));
			criteria.setResultTransformer(Transformers.aliasToBean(RecordTypeVisibilityEntity.class));
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
			Criteria criteria = session.createCriteria(RecordTypeVisibilityEntity.class);
			ProjectionList projList = Projections.projectionList();
			projList.add(Projections.property("recordType"));
			criteria.setProjection(Projections.distinct(projList));
			criteria.addOrder(Order.asc("recordType"));
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
}
