package edu.iCET.service;

import edu.iCET.model.entity.Seat;
import edu.iCET.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SeatScheduleService {

    @Autowired
    private SeatRepository seatRepository;

    // Run every minute
    @Scheduled(fixedRate = 60000) //
    public void releaseExpiredHolds() {
        LocalDateTime now = LocalDateTime.now();

        List<Seat> expiredSeats = seatRepository.findAll().stream()
                .filter(seat -> "HELD".equals(seat.getStatus())
                        && seat.getHoldExpiry() != null
                        && seat.getHoldExpiry().isBefore(now))
                .toList();

        for (Seat seat : expiredSeats) {
            seat.setStatus("AVAILABLE");
            seat.setHeldByUserId(null);
            seat.setHoldExpiry(null);
        }

        if (!expiredSeats.isEmpty()) {
            seatRepository.saveAll(expiredSeats);
            System.out.println("Released " + expiredSeats.size() + " expired held seats.");
        }
    }
}
