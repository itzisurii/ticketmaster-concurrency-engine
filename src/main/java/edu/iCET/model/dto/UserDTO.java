package edu.iCET.model.dto;

import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {

    @Id
    private long id;
    private String name;
    private String tier;
    private String email;
}
