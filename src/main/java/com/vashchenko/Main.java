package com.vashchenko;

import com.vashchenko.entities.Room;
import com.vashchenko.entities.Type;
import org.hibernate.Session;

public class Main {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
    }
}
