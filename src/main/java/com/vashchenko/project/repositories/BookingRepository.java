package com.vashchenko.project.repositories;

import com.vashchenko.project.enums.BookingStatus;
import com.vashchenko.project.models.entities.Booking;
import com.vashchenko.project.models.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface BookingRepository extends JpaRepository<Booking,Long> {
    List<Booking> findAllByUser(User user);

    List<Booking> findByStatusIs(BookingStatus bookingStatus);

    @Query("select t from Booking t where t.status=:status and t.user.id=:id")
    List<Booking> findActiveUserBookings(@Param("status") BookingStatus bookingStatus,
                                         @Param("id") Long id);

    @Query("select t from Booking t where t.status!=:status and t.user.id=:id")
    List<Booking> findNotActiveUserBookings(@Param("status") BookingStatus bookingStatus,
                                         @Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Booking b SET b.status = :status, b.dateOf = :date WHERE b.id = :id")
    void closeBookingById(@Param("id") Long id,
                          @Param("date") LocalDate date,
                          @Param("status") BookingStatus status);

    @Modifying
    @Query("update Booking  b set b.status=:status where b.dateOf=:date")
    void closeEndedBookings(@Param("date") LocalDate date,
                            @Param("status") BookingStatus bookingStatus);
}
