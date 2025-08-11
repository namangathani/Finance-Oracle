package com.oracle.dao.impl;

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
}
