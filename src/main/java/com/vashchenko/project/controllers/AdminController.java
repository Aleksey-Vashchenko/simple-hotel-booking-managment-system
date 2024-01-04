package com.vashchenko.project.controllers;

import com.vashchenko.project.enums.Role;
import com.vashchenko.project.models.entities.Booking;
import com.vashchenko.project.models.entities.Image;
import com.vashchenko.project.models.entities.RoomType;
import com.vashchenko.project.models.entities.User;
import com.vashchenko.project.services.BookingService;
import com.vashchenko.project.services.ImageService;
import com.vashchenko.project.services.RoomTypeService;
import com.vashchenko.project.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Component
@Controller
@RequestMapping("admin/*")
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoomTypeService roomTypeService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/{login}")
    String getMainAdminPage(@PathVariable String login, Model model, Principal principal){
        model.addAttribute("listOfBookings",bookingService.getActiveBookings());
        model.addAttribute("fragmentType", "booking");
        return "adminViews/mainPage";
    }

    @GetMapping("/{login}/account")
    String getAccountPage(@PathVariable String login, Model model){

        return "adminViews/mainPage";
    }

    @GetMapping("/users")
    String getUsersPage(Model model, Principal principal){
        Iterable<User> allUsers = userService.findAllByLoginNot(principal.getName());
        model.addAttribute("listOfUsers",allUsers);
        model.addAttribute("fragmentType", "user");
        return "adminViews/mainPage";
    }

    @DeleteMapping("/users/delete")
    String deleteUser(@RequestParam long id, Model model,Principal principal){
        try {
            userService.deleteUserById(id);
            model.addAttribute("SuccessAction","Пользователь был успешно удален");
        }
        catch (IllegalArgumentException illegalArgumentException){
            model.addAttribute("Exception",illegalArgumentException.getMessage());
        }
        catch (RuntimeException runtimeException){
            model.addAttribute("Exception",runtimeException.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/users/edit")
    String editUserPage(@RequestParam(name = "id",required = true) String id, Model model, Principal principal){
        try {
            User userToEdit = userService.findById(Long.valueOf(id));
            model.addAttribute("fragmentType", "user");
            model.addAttribute("userToEdit",userToEdit);
            model.addAttribute("allRoles", new Role[]{Role.ADMIN,Role.CLIENT});
        }
        catch (IllegalArgumentException exception){
            model.addAttribute("Exception",exception.getMessage());
            return "redirect:"+principal.getName();
        }catch (RuntimeException runtimeException){
            model.addAttribute("Exception",runtimeException.getMessage());
        }
        return "adminViews/editEntityPage";
    }

    @PostMapping("/users/edit")
    String acceptEditedUser(@ModelAttribute User changedUser, Model model){
        try {
            userService.save(changedUser);
            model.addAttribute("SuccessAction","Пользователь был успешно обновлен");
        }
        catch (IllegalArgumentException exception){
            model.addAttribute("Exception",exception.getMessage());
        }catch (RuntimeException runtimeException){
            model.addAttribute("Exception",runtimeException.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/users/create")
    String createUserPage(Model model,Principal principal){
        model.addAttribute("newUser",new User());
        model.addAttribute("allRoles", new Role[]{Role.ADMIN,Role.CLIENT});
        model.addAttribute("fragmentType", "user");
        return "adminViews/createEntityPage";
    }

    @PostMapping("/users/create")
    String createUser(@ModelAttribute User newUser, Model model){
        User userFromDb = userService.getUserByLogin(newUser.getLogin());

        if (userFromDb!=null) {
            model.addAttribute("existUserError", "User exists!");
            return "adminViews/createEntityPage";
        }
        else {
            newUser.setActive(true);
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            newUser.setRegistered(true);
            userService.save(newUser);
        }
        return "redirect:/admin/users";
    }



    @GetMapping("/types")
    String getTypesPage(Model model){
        model.addAttribute("listOfTypes",roomTypeService.findAll());
        model.addAttribute("fragmentType", "type");
        return "/adminViews/mainPage";
    }

    @DeleteMapping("/types/delete")
    String deleteType(@RequestParam Long id){
        imageService.deleteImagesByRoomTypeId(id);
        roomTypeService.deleteRoomTypeById(id);
        return "redirect:/admin/types";
    }

    @GetMapping("/types/create")
    String getCreateTypePage(Model model){
        model.addAttribute("roomType",new RoomType());
        model.addAttribute("fragmentType","type");
        return "/adminViews/createEntityPage";
    }

    @PostMapping("/types/create")
    @Transactional
    public String handleRoomType(@ModelAttribute RoomType roomType, @RequestParam("photos") MultipartFile[] images){
        RoomType existed = roomTypeService.findById(roomType.getId()).get();
        existed.setRoomAmount(roomType.getRoomAmount());
        existed.setName(roomType.getName());
        existed.setPrice(roomType.getPrice());
        existed.setDescription(roomType.getDescription());
        existed.setAmountOfAdult(roomType.getAmountOfAdult());
        roomTypeService.save(roomType);
        roomType.getImages().forEach(image -> {image.getUrl();} );
        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                existed.getImages().clear();
                roomTypeService.save(existed);
                try {
                    Image myImage = new Image();
                    myImage.setType(existed);
                    imageService.saveImage(myImage);
                    String originalFilename = image.getOriginalFilename();
                    String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                    File newImage = new File("D:\\University\\sem_5\\Course_project2\\src\\main\\resources\\static\\images\\roomTypes\\"+myImage.getId()+fileExtension);
                    newImage.createNewFile();
                    myImage.setUrl("\\static\\images\\roomTypes\\"+myImage.getId()+fileExtension);
                    Files.write(newImage.toPath(),image.getBytes());
                    imageService.saveImage(myImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                roomTypeService.save(existed);
            }
        }
        return "redirect:/admin/types";
    }

    @GetMapping("types/edit")
    String getEditTypePage(@RequestParam(name = "id",required = true) String id,Model model){
        Optional<RoomType> roomType = roomTypeService.findById(Long.valueOf(id));
        if (roomType.isPresent()){
            model.addAttribute("roomType",roomType.get());
            model.addAttribute("fragmentType","type");
        }
        return "/adminViews/editEntityPage";
    }

    @PatchMapping("types/edit")
    String handleEditingType(@ModelAttribute RoomType roomType, Model model){
        roomTypeService.save(roomType);
        return "redirect:/admin/types";
    }

    @GetMapping("/bookings")
    String getBookingPage(Model model, Principal principal){
        Iterable<Booking> bookings = bookingService.getActiveBookings();
        model.addAttribute("listOfBookings",bookings);
        model.addAttribute("fragmentType", "booking");
        List<RoomType> typeSet = roomTypeService.findAll();
        for (RoomType roomType:typeSet){
            roomType.setRoomAmount(roomTypeService.findAvailableRoomsOfType(roomType));
        }
        model.addAttribute("listOfTypes",typeSet);
        return "adminViews/mainPage";
    }

    @DeleteMapping("/bookings/delete")
    String deleteBooking(@RequestParam Long id){
        bookingService.deleteBookingById(id);
        return "redirect:/admin/bookings";
    }

    @PostMapping("/bookings/close")
    String closeBooking(@RequestParam Long id) {
        bookingService.closeBooking(id);
        return "redirect:/admin/bookings";
    }

    private void deleteImagesFromServer(List<String> images){
        for(String path : images){
            try {
                if (Files.exists(Paths.get("D:\\University\\sem_5\\Course_project2\\"+path))){
                    Files.delete(Paths.get("D:\\University\\sem_5\\Course_project2\\"+path));
                }
            } catch (IOException e) {

            }
        }
    }
}
