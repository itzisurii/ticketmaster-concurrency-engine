package edu.iCET.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EventDTO {

    private Long id;
    private String name;
    private double basePrice;
    @JsonProperty("isHighDemand")
    private boolean isHighDemand;
    private LocalDateTime eventDate;
}
