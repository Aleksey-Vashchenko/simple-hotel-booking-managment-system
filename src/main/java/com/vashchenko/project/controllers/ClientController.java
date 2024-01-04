package com.vashchenko.project.controllers;

import com.vashchenko.project.models.entities.User;
import com.vashchenko.project.repositories.UserRepository;
import com.vashchenko.project.services.BookingService;
import com.vashchenko.project.services.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@Setter
@RequestMapping("/client/*")
public class ClientController {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private UserService userService;

    @GetMapping("/bookings")
    public String getBookings(Model model, HttpSession session){
        User currentUser = (User) session.getAttribute("currentUser");
        model.addAttribute("currentBookings",bookingService.findActiveBookingsByUserId(currentUser.getId()));
        model.addAttribute("expiredBookings",bookingService.findNotActiveBookingsByUserId(currentUser.getId()));
        return "clientViews/bookingAccPage";
    }
}
