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
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import com.sss.report.core.tags.DAO;
import com.sss.report.entity.FieldPermissionEntity;
import com.sss.report.entity.LayoutAssignmentEntity;
import com.sss.report.util.HibernateUtil;;

@DAO(forEntity = LayoutAssignmentEntity.class)
public class LayoutAssignmentDAO   {
	
	public Integer create(LayoutAssignmentEntity layoutAssignment) {
		SessionFactory sessionfactory = HibernateUtil.getSessionFactory();
		Session session = sessionfactory.openSession();
		Transaction tx = null;
		Integer id = null;
		try {
			tx = session.beginTransaction();
			id = (Integer) session.save(layoutAssignment);
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

	public List<LayoutAssignmentEntity> findByProfile(String profileName) {
		SessionFactory sessionfactory = HibernateUtil.getSessionFactory();
		Session session = sessionfactory.openSession();
		Transaction tx = null;
		List<LayoutAssignmentEntity> entities = new ArrayList<>();
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(LayoutAssignmentEntity.class);
			ProjectionList projections = Projections.projectionList();
			projections.add(Projections.property("layout"), "layout");
			String sql = "coalesce (recordType, '') as recordType";
			String [] columnAliases = { "recordType" };
			Type [] types = { StandardBasicTypes.STRING };
			projections.add(Projections.sqlProjection(sql, columnAliases, types));
			criteria.setProjection(projections);
			criteria.add(Restrictions.like("profile", profileName));
			criteria.addOrder(Order.asc("layout"));
			criteria.setResultTransformer(Transformers.aliasToBean(LayoutAssignmentEntity.class));
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
			projList.add(Projections.property("layout"));
			criteria.setProjection(Projections.distinct(projList));
			criteria.addOrder(Order.asc("layout"));
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
