package com.vashchenko.project.controllers;

import com.vashchenko.project.enums.BookingStatus;
import com.vashchenko.project.enums.Role;
import com.vashchenko.project.models.entities.Booking;
import com.vashchenko.project.models.entities.RoomType;
import com.vashchenko.project.models.entities.User;
import com.vashchenko.project.repositories.RoomTypeRepository;
import com.vashchenko.project.repositories.UserRepository;
import com.vashchenko.project.services.BookingService;
import com.vashchenko.project.services.RoomTypeService;
import com.vashchenko.project.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.awt.print.Book;
import java.net.http.HttpRequest;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
public class HotelMainController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private RoomTypeService roomTypeService;

    @GetMapping("/")
    public String getMainPage(Model model, HttpSession session) {
        Booking booking = new Booking();
        booking.setDateOn(LocalDate.now());
        session.setAttribute("currentBooking",booking);
        model.addAttribute("booking",booking);
        model.addAttribute("listOfTypes", roomTypeService.findAll());
        model.addAttribute("amountsOfPeople", roomTypeService.getSetOfRoomTypesPeopleAmount());
        return "index";
    }

    @GetMapping("/booking")
    public String getBookingPage(Model model, HttpSession session){
        Booking booking = new Booking();
        booking.setDateOn(LocalDate.now());
        session.setAttribute("currentBooking",booking);
        model.addAttribute("booking",booking);
        model.addAttribute("amountsOfPeople", roomTypeService.getSetOfRoomTypesPeopleAmount());
        return "bookingPage";
    }

    @PostMapping("/booking")
    public String handleBookingDates(@ModelAttribute Booking booking, Model model,HttpSession session, RedirectAttributes redirectAttributes){
        Booking currentBooking = (Booking) session.getAttribute("currentBooking");
        currentBooking.setDateOn(booking.getDateOn());
        currentBooking.setDateOf(booking.getDateOf());
        currentBooking.setGuestAmount(booking.getGuestAmount());
        session.setAttribute("currentBooking",currentBooking);
        redirectAttributes.addFlashAttribute("booking",booking);
        return "redirect:/booking/room";
    }

    @GetMapping("/booking/room")
    public String getBookingRoomPage(@ModelAttribute Booking booking,Model model){
        List<RoomType> roomTypes = roomTypeService
                .findAvailableRoomTypes(booking);
        model.addAttribute("roomTypes", roomTypes);
        model.addAttribute("booking",booking);
        return "bookingRoomPage";
    }

    @PostMapping("/booking/room")
    public String handleBookingRoomPage(@RequestParam(name = "typeId") Long id,
                                        Model model, HttpSession session,RedirectAttributes redirectAttributes){
        Booking currentBooking = (Booking) session.getAttribute("currentBooking");
        currentBooking.setStatus(BookingStatus.ACTIVE);
        currentBooking.setRoomType(roomTypeService.findById(id).get());
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.getAuthority()))
        ||SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")){
            session.setAttribute("currentBooking",currentBooking);
            return "redirect:/booking/customer";
        }
        else {
            try {
                currentBooking.setUser(userService.getUserByLogin(SecurityContextHolder.getContext().getAuthentication().getName()));
                bookingService.saveBooking(currentBooking);
                redirectAttributes.addFlashAttribute("booking",currentBooking);
                return "redirect:/booking/success";
            }
            catch (RuntimeException e){
                return "main";
            }
        }
    }

    @GetMapping("/booking/customer")
    public String getCustomerPage(Model model){
        model.addAttribute("newUser",new User());
        return "inputUserPage";
    }

    @PostMapping("/booking/customer")
    public String handleCustomerPage(@ModelAttribute User newUser,Model model, HttpSession session,RedirectAttributes redirectAttributes) {
        newUser.setRegistered(false);
        Booking currentBooking = (Booking) session.getAttribute("currentBooking");
        currentBooking.setUser(newUser);
        bookingService.saveBooking(currentBooking);
        session.setAttribute("currentBooking",currentBooking);
        return "redirect:/booking/success";
    }

    @GetMapping("/booking/confirm")
    public String getConfirmPage(@ModelAttribute Booking booking,Model model){
        model.addAttribute("booking",booking);
        return "successBookingPage";
    }

    @PostMapping("/booking/confirm")
    public String handleConfirm(@ModelAttribute Booking booking,Model model){
        model.addAttribute("booking",booking);
        return "redirect:/booking/success";
    }

    @GetMapping("/booking/success")
    public String successBookingPage(HttpSession session, Model model){
        Booking currentBooking = (Booking) session.getAttribute("currentBooking");
        model.addAttribute("booking",currentBooking);
        model.addAttribute("days", ChronoUnit.DAYS.between(currentBooking.getDateOn(), currentBooking.getDateOf()));
        return "successBookingPage";
    }


    @PostMapping("/booking/delete")
    public String deleteBooking(@RequestParam Long id, HttpServletRequest request){
        bookingService.deleteBookingById(id);
        return "redirect:"+request.getHeader("Referer");
    }
}
