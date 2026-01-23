package edu.iCET.service;

import edu.iCET.model.dto.SeatDTO;
import edu.iCET.model.entity.AuditLog;
import edu.iCET.model.entity.Seat;
import edu.iCET.repository.AuditLogRepository;
import edu.iCET.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatService {

    @Autowired
    SeatRepository seatRepository;

    @Autowired
    AuditLogRepository auditLogRepository;

    // Convert Seat entity to DTO
    private SeatDTO toDTO(Seat seat) {
        return new SeatDTO(
                seat.getId(),
                seat.getEventId(),
                seat.getSeatNumber(),
                seat.getStatus(),
                seat.getHeldByUserId(),
                seat.getHoldExpiry()
        );
    }


    public List<SeatDTO> getSeatsByEvent(Long eventId) {
        List<Seat> seats = seatRepository.findByEventId(eventId);
        return seats.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public SeatDTO releaseSeat(Long seatId, Long userId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new IllegalArgumentException("Seat not found"));

        // Only the user who holds the seat can release it
        if (!userId.equals(seat.getHeldByUserId())) {
            throw new SeatLockedException("You cannot release this seat, it is held by another user.");
        }

        seat.setHeldByUserId(null);
        seat.setStatus("AVAILABLE");
        seat.setHoldExpiry(null);
        seatRepository.save(seat);

        return toDTO(seat);

    }

    public SeatDTO getSeatStatus(Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new IllegalArgumentException("Seat not found"));

        // Automatically release expired holds
        if ("HELD".equals(seat.getStatus()) && seat.getHoldExpiry() != null
                && seat.getHoldExpiry().isBefore(LocalDateTime.now())) {
            seat.setStatus("AVAILABLE");
            seat.setHeldByUserId(null);
            seat.setHoldExpiry(null);
            seatRepository.save(seat);
        }

        return toDTO(seat);
    }


    public class SeatLockedException extends RuntimeException {
        public SeatLockedException(String message) {
            super(message);
        }
    }


}
