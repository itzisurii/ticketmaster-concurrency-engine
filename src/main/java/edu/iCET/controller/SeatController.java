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



}
