package split.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table
public class GroupMapper {

    @Id
    long id;

    @Column(nullable =false)
    String userId;

    @Column(nullable =false)
    String groupId;


}
