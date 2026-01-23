package edu.iCET.controller;

import edu.iCET.model.entity.Booking;
import edu.iCET.model.entity.Event;
import edu.iCET.model.entity.Seat;
import edu.iCET.model.entity.User;
import edu.iCET.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    BookingService bookingService;

    @PostMapping("/confirm")
    public Booking confirmBooking(@RequestBody Map<String, Long> request) {
        Long seatId = request.get("seatId");
        Long userId = request.get("userId");
        return bookingService.confirmBooking(seatId, userId);
    }

    @PostMapping("/create")
    public Booking createBooking(@RequestParam Long userId, @RequestParam Long eventId, @RequestParam Long seatId) {
        User user = null; // fetch from userRepo
        Event event = null; // fetch from eventRepo
        Seat seat = null;
        return bookingService.createBooking(user, event, seat);
    }

}
