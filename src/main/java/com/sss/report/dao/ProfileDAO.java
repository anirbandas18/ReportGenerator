package com.sss.report.dao;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.sss.report.core.HibernateUtil;
import com.sss.report.entity.ProfileEntity;

public class ProfileDAO {
	
	public Boolean create(ProfileEntity profile) {
		SessionFactory sessionfactory = HibernateUtil.getSessionFactory();
		Session session = sessionfactory.getCurrentSession();
		Transaction tx = null;
		Boolean flag = false;
		try {
			tx = session.beginTransaction();
			String before = profile.getName();
			session.save(profile);
			ProfileEntity after = session.get(ProfileEntity.class, before);
			flag = before.equalsIgnoreCase(after.getName());
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
		return flag;
	}
	
	public ProfileEntity retrieve(String profileName) {
		SessionFactory sessionfactory = HibernateUtil.getSessionFactory();
		Session session = sessionfactory.getCurrentSession();
		Transaction tx = null;
		ProfileEntity profile = new ProfileEntity();
		try {
			tx = session.beginTransaction();
			profile = session.get(ProfileEntity.class, profileName);
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
		return profile;
	}
	
	public Set<ProfileEntity> retrieveAll() {
		SessionFactory sessionfactory = HibernateUtil.getSessionFactory();
		Session session = sessionfactory.getCurrentSession();
		Transaction tx = null;
		Set<ProfileEntity> profileNames = new TreeSet<>();
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("from ProfileEntity order by name asc");
			List<ProfileEntity> profileNameList = query.list();
			profileNames.addAll(profileNameList);
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
		return profileNames;
	}

}
