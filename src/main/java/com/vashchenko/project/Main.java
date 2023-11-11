package com.vashchenko.project;

import com.vashchenko.project.dbase.HibernateUtil;
import com.vashchenko.project.enums.Role;
import com.vashchenko.project.models.entities.*;
import com.vashchenko.project.enums.BookingStatus;
import org.hibernate.Session;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) {
        Session session =HibernateUtil.getSessionFactory().openSession();
        HashSet<Role> roles = new HashSet<>(Arrays.asList(Role.ADMIN));
        User admin = User.builder()
                .login("admin")
                .password("admin")
                .phoneNumber("123")
                .roles(roles)
                .build();
        /*User manager = User.builder()
                .phoneNumber("123")
                .login("manager")
                .password("manager")
                .roles(new HashSet<>(Arrays.asList(Role.MANAGER,Role.CLIENT)))
                .build();
        User client = User.builder()
                .phoneNumber("123")
                .login("client")
                .password("client")
                .roles(new HashSet<>(Arrays.asList(Role.CLIENT)))
                .build();
        session.getTransaction().begin();
        session.save(manager);
        session.save(client);
        session.save(admin);
        session.getTransaction().commit();*/
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        session.getTransaction().begin();
        session.save(admin);
        session.getTransaction().commit();








    }
}
