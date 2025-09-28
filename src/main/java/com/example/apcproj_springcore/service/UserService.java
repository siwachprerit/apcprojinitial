package com.example.apcproj_springcore.service;

import com.example.apcproj_springcore.dao.UserDAO;
import com.example.apcproj_springcore.model.User;
import com.example.apcproj_springcore.util.PasswordUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Scanner;

@Service
public class UserService {

    private final UserDAO userDAO;
    private User currentUser;
    private final Scanner scanner = new Scanner(System.in);

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Transactional
    public User loginOrRegister(String username, boolean isSignup) throws Exception {
        User u = userDAO.findByUsername(username);

        if (u == null && isSignup) { // signup
            System.out.print("Create a password: ");
            String password = scanner.nextLine();
            String hashed = PasswordUtils.hashPassword(password);
            u = userDAO.createUser(username, hashed, 100.0);
            System.out.println("Account created successfully!");
        } else if (u != null && !isSignup) { // login
            boolean success = false;
            for (int i = 0; i < 3; i++) {
                System.out.print("Enter password: ");
                String input = scanner.nextLine();
                if (PasswordUtils.hashPassword(input).equals(u.getPassword())) {
                    success = true;
                    break;
                } else System.out.println("Incorrect password. Try again.");
            }
            if (!success) throw new Exception("Failed login: wrong password");
            System.out.println("Welcome back, " + username + "!");
        } else if (u == null) {
            throw new Exception("User does not exist. Please sign up first.");
        } else {
            throw new Exception("User already exists. Please login.");
        }

        this.currentUser = u;
        return u;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean canBet(double amount) {
        return currentUser != null && amount > 0 && currentUser.getBalance() >= amount;
    }

    @Transactional
    public void applyBalanceChange(double delta) {
        if (currentUser == null) throw new IllegalStateException("No logged-in user");
        double updated = currentUser.getBalance() + delta;
        if (updated < 0) updated = 0;
        userDAO.updateBalance(currentUser, updated);
    }

    public void logout() {
        this.currentUser = null;
    }
}
