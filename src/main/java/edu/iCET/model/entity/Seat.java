package edu.iCET.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long eventId;
    private String seatNumber;
    private String status; // "AVAILABLE", "HELD", "SOLD"
    private Long heldByUserId;
    private LocalDateTime holdExpiry;
}
