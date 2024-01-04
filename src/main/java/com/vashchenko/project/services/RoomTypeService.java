package com.vashchenko.project.services;

import com.vashchenko.project.enums.BookingStatus;
import com.vashchenko.project.models.entities.Booking;
import com.vashchenko.project.models.entities.RoomType;
import com.vashchenko.project.repositories.BookingRepository;
import com.vashchenko.project.repositories.RoomTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoomTypeService {
    @Autowired
    private RoomTypeRepository roomTypeRepository;
    @Autowired
    private BookingRepository bookingRepository;

    public List<RoomType> findAvailableRoomTypes(Booking booking) {
        List<RoomType> roomTypes = roomTypeRepository
                .findAvailableRoomTypes(booking.getDateOn(),booking.getDateOf(),
                        BookingStatus.ACTIVE.name(), booking.getGuestAmount());
        for (RoomType roomType: roomTypes){
            roomType.setRoomAmount(roomTypeRepository.findAvailableRooms(booking.getDateOn(),booking.getDateOf(),
                    BookingStatus.ACTIVE.name(), roomType.getId()));
        }
        return roomTypes;
    }

    public List<RoomType> findAll() {
        return roomTypeRepository.findAll();
    }

    public void save(RoomType roomType) {
        roomTypeRepository.save(roomType);
    }

    public Optional<RoomType> findById(Long id) {
        return roomTypeRepository.findById(id);
    }

    public void deleteRoomTypeById(Long id){
        roomTypeRepository.deleteById(id);
    }

    public Set<Integer> getSetOfRoomTypesPeopleAmount(){
        return roomTypeRepository.findDistinctAmountOfPeople();
    }

    public Integer findAvailableRoomsOfType(RoomType roomType){
        return roomTypeRepository.getFreeAmount(roomType.getId());
    }
}
