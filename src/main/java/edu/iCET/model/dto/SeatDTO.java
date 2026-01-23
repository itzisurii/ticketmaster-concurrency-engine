package edu.iCET.model.dto;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SeatDTO {

    private Long id;
    private Long eventId;
    private String seatNumber;
    private String status; // "AVAILABLE", "HELD", "SOLD"
    private Long heldByUserId;
    private LocalDateTime holdExpiry;
}

