package com.vashchenko;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            // Создаем конфигурацию Hibernate из файла hibernate.cfg.xml
            Configuration configuration = new Configuration().configure();

            // Создаем реестр сервисов (ServiceRegistry)
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure()
                    .build();

            // Создаем фабрику сессий (SessionFactory)
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            // Обработка исключения, если что-то пошло не так
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
