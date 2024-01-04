package com.vashchenko.project.dbase;

import com.vashchenko.project.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {
    @Autowired
    private BookingService bookingService;
    @Scheduled(cron = "0 0 0 * * *") // Запуск каждый день в 00:00
    public void setCompleteStatus() {
        bookingService.closeEndedBookings();
    }
    @Scheduled(cron = "0 0 12 */7 * *") // Запуск каждую неделю в 12:00
    public void makeReserveCopyOfDatabase() {
        bookingService.closeEndedBookings();
    }
}
