package split.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;


import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;


import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class Users {

    @Id @NotNull(message = "Id is null")
    private String id;  // Uses UUID as primary key

    @Column(nullable = false)@Size(min=1,max =20)
    private String userName;

    @Pattern(regexp = "^[A-Za-z0-9]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Invalid email format")
    private String email;


    @Column @NotNull(message = "password should not be null")
    private String password;



}
