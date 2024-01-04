package com.vashchenko.project.repositories;

import com.vashchenko.project.Main;
import com.vashchenko.project.enums.BookingStatus;
import com.vashchenko.project.models.entities.RoomType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public interface RoomTypeRepository extends JpaRepository<RoomType,Long> {
    @Query(value = "SELECT *\n" +
            " FROM type\n" +
            " WHERE type.room_amount - (\n" +
            "     SELECT COUNT(*)\n" +
            "     FROM booking\n" +
            "     WHERE booking.type_id= type.id and\n" +
            "             booking.status=:status AND\n" +
            "         (:startDate BETWEEN booking.date_on AND booking.date_of or\n" +
            "          :endDate BETWEEN booking.date_on AND booking.date_of or\n" +
            "          booking.date_on between :startDate and :endDate)) > 0\n" +
            "   and type.amount_of_people>=:guestAmount", nativeQuery = true)
    List<RoomType> findAvailableRoomTypes(@Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate,
                                          @Param("status") String status,
                                          @Param("guestAmount") Integer guestAmount);


    @Query(value = "SELECT (SELECT SUM(room_amount) FROM type where id = :id) -\n" +
            "                 (SELECT COUNT(*) FROM booking where booking.status=:status\n" +
            "                               and (:startDate BETWEEN booking.date_on AND booking.date_of) or\n" +
            "                              :endDate BETWEEN booking.date_on AND booking.date_of or\n" +
            "                              booking.date_on between :startDate and :endDate)", nativeQuery = true)
    Integer findAvailableRooms(@Param("startDate") LocalDate startDate,
                               @Param("endDate") LocalDate endDate,
                               @Param("status") String status,
                               @Param("id") Long id);

    @Query(value = "select n from number where n<=(select MAX(amount_of_people) from type)",nativeQuery = true)
    Set<Integer> findDistinctAmountOfPeople();

    @Query(value = "SELECT t.room_amount - COUNT(b.status) as result\n" +
            "FROM type t\n" +
            "         LEFT JOIN booking b ON t.id = b.type_id AND b.status = 'ACTIVE'\n" +
            "WHERE t.id =:id \n" +
            "GROUP BY t.id, t.room_amount", nativeQuery = true)
    Integer getFreeAmount(@Param("id") Long id);


}
