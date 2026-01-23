package edu.iCET.service;

import edu.iCET.annotation.AuditFailure;
import edu.iCET.model.entity.*;
import edu.iCET.service.PriceCalculatorService;
import edu.iCET.model.dto.PriceResult;
import edu.iCET.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BookingService {

    @Autowired
    SeatRepository seatRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    PriceCalculatorService priceCalculatorService;

    @AuditFailure
    @Transactional
    public Booking confirmBooking(Long seatId, Long userId) {

        Seat seat = seatRepository.findSeatForUpdate(seatId);

        // Seat must be HELD
        if (!"HELD".equals(seat.getStatus())) {
            throw new RuntimeException("Seat is not held");
        }

        // Must be same user
        if (!userId.equals(seat.getHeldByUserId())) {
            throw new RuntimeException("Seat is held by another user");
        }

        // Hold must be valid
        if (seat.getHoldExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Seat hold expired");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Event event = eventRepository.findById(seat.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Calculate price
        PriceResult priceResult = priceCalculatorService.calculatePrice(user, event);

        //  Create booking
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setSeatId(seatId);
        booking.setAmountPaid(priceResult.getFinalPrice());
        booking.setStatus("CONFIRMED");

        bookingRepository.save(booking);

        // Mark seat SOLD
        seat.setStatus("SOLD");
        seat.setHeldByUserId(null);
        seat.setHoldExpiry(null);
        seatRepository.save(seat);

        return booking;
    }


}
