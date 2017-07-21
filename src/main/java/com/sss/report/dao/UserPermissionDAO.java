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

import com.sss.report.core.DAO;
import com.sss.report.core.HibernateUtil;
import com.sss.report.entity.UserPermissionEntity;;

@DAO(forEntity = UserPermissionEntity.class)
public class UserPermissionDAO   {
	
	public Integer create(UserPermissionEntity userPermission) {
		SessionFactory sessionfactory = HibernateUtil.getSessionFactory();
		Session session = sessionfactory.openSession();
		Transaction tx = null;
		Integer id = null;
		try {
			tx = session.beginTransaction();
			id = (Integer) session.save(userPermission);
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
	
	public List<UserPermissionEntity> findByProfile(String profileName) {
		SessionFactory sessionfactory = HibernateUtil.getSessionFactory();
		Session session = sessionfactory.openSession();
		Transaction tx = null;
		List<UserPermissionEntity> entities = new ArrayList<>();
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(UserPermissionEntity.class);
			ProjectionList projections = Projections.projectionList();
			projections.add(Projections.property("name"), "name");
			projections.add(Projections.property("enabled"), "enabled");
			criteria.setProjection(projections);
			criteria.add(Restrictions.like("profile", profileName));
			criteria.addOrder(Order.asc("name"));
			criteria.setResultTransformer(Transformers.aliasToBean(UserPermissionEntity.class));
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
