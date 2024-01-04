package com.vashchenko.project.controllers;

import com.vashchenko.project.enums.Role;
import com.vashchenko.project.models.entities.User;
import com.vashchenko.project.repositories.UserRepository;
import com.vashchenko.project.services.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Controller
@Setter
public class AuthenticationController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String getLoginPage(){
            return "login";
}

    @GetMapping("/registration")
    public String getRegistrationPage(Model model) {
        model.addAttribute("newUser",new User());
        return "registration";

    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("newUser") User newUser, Model model) {
        Optional<User> userFromDb = userRepository.findUserByLogin(newUser.getLogin());

        if (userFromDb.isPresent()) {
            model.addAttribute("existUserError", "User exists!");
            return "registration";
        }
        else {
            newUser.setActive(true);
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            newUser.addRole(Role.CLIENT);
            newUser.setRegistered(true);
            userRepository.save(newUser);
            model.addAttribute("successRegistration","Вы успешно зарегистрировались");
        }
        return "redirect:/login";
    }

    @GetMapping("acc/{login}")
    public String returnProfilePage(@PathVariable String login, HttpSession session) {
        User user = userService.getUserByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
        session.setAttribute("currentUser", user);
        ArrayList<Role> authorities = (ArrayList<Role>) user.getAuthorities();
        if(authorities.contains(new SimpleGrantedAuthority(Role.ADMIN.getAuthority()))){
            setRoleInSession(session,"admin");
            return "redirect:/admin/"+login;
        }else{
            setRoleInSession(session,"user");
            return "redirect:/client/bookings";
        }
    }

    @GetMapping("/acc")
    public String getAccountPage(Model model, HttpSession session){
        User user = (User) session.getAttribute("currentUser");
        model.addAttribute("user",user);
        ArrayList<Role> authorities = (ArrayList<Role>) user.getAuthorities();
        if(authorities.contains(new SimpleGrantedAuthority(Role.ADMIN.getAuthority()))){
            setRoleInSession(session,"admin");
            return "page_acc";
        }else{
            setRoleInSession(session,"user");
            return "clientViews/mainPage";
        }

    }

    @PostMapping("/acc")
    public String saveAccountChanges(@ModelAttribute User user, Model model){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        model.addAttribute("user",user);
        return "page_acc";
    }

    private void setRoleInSession(HttpSession session, String role){
        session.setAttribute("role",role);
    }
}
