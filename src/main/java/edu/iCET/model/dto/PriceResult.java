package edu.iCET.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PriceResult {

    private double finalPrice;
    private boolean priorityAccess;

}
