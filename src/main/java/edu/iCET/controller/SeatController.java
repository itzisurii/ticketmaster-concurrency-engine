package edu.iCET.controller;

import edu.iCET.model.dto.SeatDTO;
import edu.iCET.model.entity.Seat;
import edu.iCET.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seat")
public class SeatController {

    @Autowired
    SeatService seatService;

    @GetMapping()
    public String getSeatController(){
        return "load seat controller...";
    }


    // Get all seats for an event
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<SeatDTO>> getSeatsByEvent(@PathVariable Long eventId) {
        List<SeatDTO> seats = seatService.getSeatsByEvent(eventId);
        return ResponseEntity.ok(seats);
    }

    // Hold a seat (POST since it's changing state)
    @PostMapping("/{seatId}/hold")
    public ResponseEntity<?> holdSeat(@PathVariable Long seatId, @RequestParam Long userId) {
        try {
            SeatDTO seat = seatService.holdSeat(seatId, userId);
            return ResponseEntity.ok(seat);
        } catch (SeatService.SeatLockedException e) {
            return ResponseEntity.status(423).body(e.getMessage()); // 423 Locked
        }
    }

    // Release a seat
    @PostMapping("/{seatId}/release")
    public ResponseEntity<?> releaseSeat(@PathVariable Long seatId, @RequestParam Long userId) {
        try {
            SeatDTO seat = seatService.releaseSeat(seatId, userId);
            return ResponseEntity.ok(seat);
        } catch (SeatService.SeatLockedException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // Check seat status
    @GetMapping("/{seatId}/status")
    public ResponseEntity<SeatDTO> checkSeatStatus(@PathVariable Long seatId) {
        SeatDTO seat = seatService.getSeatStatus(seatId);
        return ResponseEntity.ok(seat);
    }

}
