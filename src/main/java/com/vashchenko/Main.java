package com.vashchenko;

import com.vashchenko.entities.Room;
import com.vashchenko.entities.Type;
import org.hibernate.Session;

public class Main {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Room room2 = session.get(Room.class,1);
        System.out.println(room2);
        session.close();
        HibernateUtil.getSessionFactory().close();
    }
}
