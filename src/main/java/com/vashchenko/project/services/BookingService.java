package com.vashchenko.project.services;

import com.vashchenko.project.enums.BookingStatus;
import com.vashchenko.project.models.entities.Booking;
import com.vashchenko.project.models.entities.User;
import com.vashchenko.project.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    public void saveBooking(Booking booking){
        bookingRepository.save(booking);
    }

    public List<Booking> getActiveBookings(){
        return bookingRepository.findByStatusIs(BookingStatus.ACTIVE);
    }

    public void deleteBookingById(Long id) {
        bookingRepository.deleteById(id);
    }

    public Optional<Booking> findBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> findActiveBookingsByUserId(Long id){
        return bookingRepository.findActiveUserBookings(BookingStatus.ACTIVE,id);
    }

    public List<Booking> findNotActiveBookingsByUserId(Long id){
        return bookingRepository.findNotActiveUserBookings(BookingStatus.ACTIVE,id);
    }

    public void closeBooking(Long id) {
        LocalDate nowDate = LocalDate.now();
        bookingRepository.closeBookingById(id,nowDate,BookingStatus.COMPLETED);
    }

    public void closeEndedBookings(){
        LocalDate localDate = LocalDate.now();
        bookingRepository.closeEndedBookings(localDate,BookingStatus.COMPLETED);
    }
}
