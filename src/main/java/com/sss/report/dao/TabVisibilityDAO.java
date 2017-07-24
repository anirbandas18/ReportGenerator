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
import com.sss.report.entity.TabVisibilityEntity;
import com.sss.report.util.HibernateUtil;;

@DAO(forEntity = TabVisibilityEntity.class)
public class TabVisibilityDAO   {
	
	public Integer create(TabVisibilityEntity tabVisibility) {
		SessionFactory sessionfactory = HibernateUtil.getSessionFactory();
		Session session = sessionfactory.openSession();
		Transaction tx = null;
		Integer id = null;
		try {
			tx = session.beginTransaction();
			id = (Integer) session.save(tabVisibility);
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

	public List<TabVisibilityEntity> findByProfile(String profileName) {
		SessionFactory sessionfactory = HibernateUtil.getSessionFactory();
		Session session = sessionfactory.openSession();
		Transaction tx = null;
		List<TabVisibilityEntity> entities = new ArrayList<>();
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(TabVisibilityEntity.class);
			ProjectionList projections = Projections.projectionList();
			projections.add(Projections.property("tab"), "tab");
			projections.add(Projections.property("visibility"), "visibility");
			criteria.setProjection(projections);
			criteria.add(Restrictions.like("profile", profileName));
			criteria.addOrder(Order.asc("tab"));
			criteria.setResultTransformer(Transformers.aliasToBean(TabVisibilityEntity.class));
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
			Criteria criteria = session.createCriteria(TabVisibilityEntity.class);
			ProjectionList projList = Projections.projectionList();
			projList.add(Projections.property("tab"));
			criteria.setProjection(Projections.distinct(projList));
			criteria.addOrder(Order.asc("tab"));
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
	
	public List<TabVisibilityEntity> findByKeyProperty(String key) {
		SessionFactory sessionfactory = HibernateUtil.getSessionFactory();
		Session session = sessionfactory.openSession();
		Transaction tx = null;
		List<TabVisibilityEntity> entities = new ArrayList<>();
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(TabVisibilityEntity.class);
			ProjectionList projections = Projections.projectionList();
			projections.add(Projections.property("profile"), "profile");
			projections.add(Projections.property("visibility"), "visibility");
			criteria.setProjection(projections);
			criteria.add(Restrictions.like("tab", key));
			criteria.addOrder(Order.asc("profile"));
			criteria.setResultTransformer(Transformers.aliasToBean(TabVisibilityEntity.class));
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
