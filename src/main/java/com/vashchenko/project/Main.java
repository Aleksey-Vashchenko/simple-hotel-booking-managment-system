package com.vashchenko.project;

import com.vashchenko.project.dbase.HibernateUtil;
import com.vashchenko.project.enums.Role;
import com.vashchenko.project.models.entities.*;
import com.vashchenko.project.enums.BookingStatus;
import com.vashchenko.project.repositories.RoomTypeRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

@Component
public class Main {

    @Autowired
    public static RoomTypeRepository repository;
    public static void main(String[] args) {

        File file = new File("D:\\University\\sem_5\\Course_project2\\src\\main\\webapp\\static\\images\\1.txt");






    }
}
