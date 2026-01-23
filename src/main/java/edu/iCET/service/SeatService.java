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

    @Transactional
    public SeatDTO holdSeat(Long seatId, Long userId) {
        // Pessimistic lock to prevent concurrent holds
        Seat seat = seatRepository.findSeatForUpdate(seatId);

        LocalDateTime now = LocalDateTime.now();

        // Check if seat is AVAILABLE or previous hold expired
        if ("AVAILABLE".equals(seat.getStatus()) ||
                ("HELD".equals(seat.getStatus()) && seat.getHoldExpiry().isBefore(now))) {

            seat.setStatus("HELD");
            seat.setHeldByUserId(userId);
            seat.setHoldExpiry(now.plusMinutes(1)); // 10-minute hold

            Seat savedSeat = seatRepository.save(seat); // save to DB

            // Convert to DTO
            SeatDTO dto = new SeatDTO();
            dto.setId(savedSeat.getId());
            dto.setEventId(savedSeat.getEventId());
            dto.setSeatNumber(savedSeat.getSeatNumber());
            dto.setStatus(savedSeat.getStatus());
            dto.setHeldByUserId(savedSeat.getHeldByUserId());
            dto.setHoldExpiry(savedSeat.getHoldExpiry());

            return dto;

        } else {
            if (seat.getHoldExpiry() == null) {
                // seat is HELD but expiry missing, treat as AVAILABLE
                seat.setStatus("AVAILABLE");
                seat.setHeldByUserId(null);
                seatRepository.save(seat);

                // now retry hold
                return holdSeat(seatId, userId);
            }

            // Seat is currently held by someone else
            long secondsLeft = java.time.Duration.between(now, seat.getHoldExpiry()).getSeconds();

            AuditLog log = new AuditLog();
            log.setAction("FAILED_HOLD_ATTEMPT");
            log.setUserId(userId);
            log.setDetails("Seat " + seat.getSeatNumber() + " already held by user " + seat.getHeldByUserId());
            log.setTimestamp(LocalDateTime.now());
            auditLogRepository.save(log); // <-- you need to @Autowired AuditLogRepository

            throw new SeatLockedException(
                    "Seat is currently held. Try again in " + secondsLeft + " seconds."
            );
        }
    }
}
