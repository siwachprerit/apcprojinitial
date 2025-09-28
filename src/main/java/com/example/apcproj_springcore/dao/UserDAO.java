package com.example.apcproj_springcore.dao;

import com.example.apcproj_springcore.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class UserDAO {

    @PersistenceContext
    private EntityManager em;

    public User findByUsername(String username) {
        List<User> users = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getResultList();
        return users.isEmpty() ? null : users.get(0);
    }

    @Transactional
    public User createUser(String username, String hashedPassword, double initialBalance) {
        User user = new User(username, hashedPassword, initialBalance);
        em.persist(user);
        return user;
    }

    @Transactional
    public void updateBalance(User user, double newBalance) {
        user.setBalance(newBalance);
        em.merge(user);
    }
}
