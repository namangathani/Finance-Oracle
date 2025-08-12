package com.oracle.dao.impl;

import java.util.List;

import com.oracle.beans.User;
import com.oracle.dao.UserDAO;
import jakarta.persistence.*;

public class UserDAOImpl implements UserDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Finance");

    @Override
    public User login(String username, String password) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.username = :username AND u.password = :password AND u.status = 'Active'",
                User.class
            );
            query.setParameter("username", username);
            query.setParameter("password", password);

            return query.getSingleResult();

        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
    
    @Override
    public User getUserByUsername(String username) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", username);
            List<User> users = query.getResultList();
            return users.isEmpty() ? null : users.get(0);
        } finally {
            em.close();
        }
    }
    
    public void printAllUsers() {
        EntityManager em = emf.createEntityManager();
        try {
            List<User> users = em.createQuery("SELECT u FROM User u", User.class).getResultList();
            if (users.isEmpty()) {
                System.out.println("❌ No users found in the database.");
            } else {
                System.out.println("📋 Users found in database:");
                for (User user : users) {
                    System.out.println("👤 Username: " + user.getUsername() +
                                       " | Password: " + user.getPassword() +
                                       " | Status: " + user.getStatus());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    
    @Override
    public void addUser(User user) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(user);
            tx.commit();
            System.out.println("✅ User added successfully: " + user.getUsername());
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }


}
